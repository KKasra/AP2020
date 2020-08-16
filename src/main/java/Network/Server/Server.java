package Network.Server;

import DB.UserDB;
import DB.components.User;
import DB.components.cards.Deck;
import Game.CommandAndResponse.GameProcessor;
import Game.GameStructure.Game;
import Game.GameStructure.Player;
import Network.Requests.PlayRequest;
import Network.Responses.ChatUpdate;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server extends Thread{
    public static final String DOMAIN = "localhost";
    public static final int PORT = 8000;

    private ServerSocket serverSocket;

    private List<ClientHandler> handlerList;
    private Map<User, String> userKeys;
    private Queue<ClientHandler> deckReaderMatchingQueue;
    private Queue<ClientHandler> classicMatchingQueue;
    private Queue <ClientHandler> tavernMatchingQueue;

    private List<Class> tavernBrawlFeatures;
    private Thread playerMatcher = new Thread(){
        @Override
        public void run() {
            super.run();
            while(true) {
                while(deckReaderMatchingQueue.size() < 2 && classicMatchingQueue.size() < 2 && tavernMatchingQueue.size() < 2) {
                    try {
                        sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (deckReaderMatchingQueue.size() >= 2)
                    matchPlayers(PlayRequest.PlayMode.deckReader);
                if (classicMatchingQueue.size() >= 2)
                    matchPlayers(PlayRequest.PlayMode.classic);
                if (tavernMatchingQueue.size() >= 2)
                    matchPlayers(PlayRequest.PlayMode.tavernBrawl);
            }
        }
    };

    @Override
    public void run() {
        super.run();
        playerMatcher.start();
        while(true) {
            try {
                Socket socket = serverSocket.accept();
                ClientHandler handler = new ClientHandler(socket);
                handler.start();
                handlerList.add(handler);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        getInstance().start();

        Scanner scanner = new Scanner(System.in);
        while(true) {
            try {
                getInstance().tavernBrawlFeatures = FeatureLoader.getInstance().load(scanner.next());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private Server() {
        try {
            serverSocket = new ServerSocket(PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        userKeys = new HashMap<>();
        handlerList = new ArrayList<>();
        deckReaderMatchingQueue = new LinkedList<>();
        classicMatchingQueue = new LinkedList<>();
        tavernMatchingQueue = new LinkedList<>();
    }

    public synchronized String login(String name, String password) throws Exception{
        User user = UserDB.getUser(name, password);
        if (userKeys.containsKey(user))
            throw new Exception("user already logged in");
        userKeys.put(user, Integer.toString(new Random().nextInt()));
        return userKeys.get(user);
    }
    public synchronized String register(String name, String password) throws Exception{
        UserDB.addUser(new User(name, password));
        return login(name, password);
    }
    public synchronized void deleteAccount(String name, String password) throws Exception {
        UserDB.deleteUser(UserDB.getUser(name, password));
    }
    public synchronized void logout(User user, ClientHandler handler){
        UserDB.saveChanges(user);

        handlerList.remove(handler);
        userKeys.remove(user);
        classicMatchingQueue.remove(handler);
        deckReaderMatchingQueue.remove(handler);
        tavernMatchingQueue.remove(handler);
    }

    public synchronized void addToMatchQueue(ClientHandler handler, PlayRequest request) {
        if (deckReaderMatchingQueue.contains(handler) || classicMatchingQueue.contains(handler))
            return;
        switch (request.getPlayMode()) {
            case deckReader:
                deckReaderMatchingQueue.add(handler);
                break;
            case classic:
                classicMatchingQueue.add(handler);
                break;
            case tavernBrawl:
                tavernMatchingQueue.add(handler);
                break;
            case training:
                try {
                    GameProcessor processor = new GameProcessor(new Game(), handler.getUser().getCurrentDeck(), handler.getUser().getUserName());
                    Player player = processor.getGame().getPlayers().get(0);
                    handler.connectToGame(processor.getResponseOutputs().get(player), processor.getCommandInputs().get(player), 0);
                    processor.startGame();
                } catch (Exception e) {
                    e.printStackTrace();
                }

        }
    }
    private synchronized void matchPlayers(PlayRequest.PlayMode playMode) {

        ClientHandler playerClient1 = null;
        ClientHandler playerClient2 = null;
        try {
            GameProcessor processor = null;
            switch (playMode) {
                case deckReader:
                    playerClient1 = deckReaderMatchingQueue.poll();
                    playerClient2 = deckReaderMatchingQueue.poll();
                    processor = new GameProcessor("config1");
                    break;
                case classic:
                    playerClient1 = classicMatchingQueue.poll();
                    playerClient2 = classicMatchingQueue.poll();
                    processor = new GameProcessor(
                            new Game(), new Deck[]{playerClient1.getUser().getCurrentDeck(), playerClient2.getUser().getCurrentDeck()}, new String[]{"p1", "p2"}, null);
                    break;
                case tavernBrawl:
                    playerClient1 = tavernMatchingQueue.poll();
                    playerClient2 = tavernMatchingQueue.poll();
                    processor = new GameProcessor(
                            new Game(), new Deck[]{playerClient1.getUser().getCurrentDeck(), playerClient2.getUser().getCurrentDeck()}, new String[]{"p1", "p2"}, tavernBrawlFeatures);
            }
            Player player1 = processor.getGame().getPlayers().get(0);
            playerClient1.connectToGame(
                    processor.getResponseOutputs().get(player1), processor.getCommandInputs().get(player1), 0);

            Player player2 = processor.getGame().getPlayers().get(1);
            playerClient2.connectToGame(
                    processor.getResponseOutputs().get(player2), processor.getCommandInputs().get(player2), 1);

            processor.startGame();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static Server instance;
    public static Server getInstance() {
        if (instance == null)
            instance = new Server();
        return instance;
    }

    public void writeMessage(String userName, String message) {
        for (ClientHandler handler : handlerList) {
            handler.writeResponse(new ChatUpdate(userName, message));
        }
    }
}
