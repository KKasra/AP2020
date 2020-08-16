package Network.Server;

import DB.Managment.CardManager;
import DB.Managment.HeroManager;
import DB.Managment.Market;
import DB.UserDB;
import DB.components.User;
import DB.components.cards.Card;
import DB.components.cards.Deck;
import DB.components.cards.DeckInfo;
import DB.components.heroes.Hero;
import Game.CommandAndResponse.EndOfGame;
import Network.Requests.*;
import Network.Responses.*;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientHandler extends Thread{
    private Socket socket;
    private ObjectInputStream input;
    private ObjectOutputStream output;

    private User user;
    private String key;

    private ObjectInput gameResponseInput;
    private ObjectOutput gameCommandOutput;

    private int indexOfPlayer;
    private final Thread gameResponseReader = new Thread(){
        @Override
        public void run() {
            super.run();
            while(true) {
                try {
                    Game.CommandAndResponse.Response response =
                            (Game.CommandAndResponse.Response) gameResponseInput.readObject();

                    writeResponse(new GameResponse(response));

                    if (response instanceof EndOfGame) {
                        DeckInfo info = user.getCurrentDeck().getInfo();
                        info.setNumberOfPlayedGames(info.getNumberOfPlayedGames() + 1);
                        if (response.getGame().getPlayers().indexOf(((EndOfGame) response).getWinner()) == indexOfPlayer){
                            info.setNumberOfWins(info.getNumberOfWins() + 1);
                        }

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    public ClientHandler(Socket socket) throws IOException {
        this.socket = socket;
        output = new ObjectOutputStream(socket.getOutputStream());
        input = new ObjectInputStream(socket.getInputStream());

    }

    @Override
    public void run() {
        super.run();
        while(true) {
            Request request = getNextRequest();
            if (user != null)
                user.getLog().writeEvent("request",request.getClass().getSimpleName());
            if (request == null)
                break;
            if (request instanceof DeleteAccount)
                handleDeleteAccount((DeleteAccount) request);
            if (request instanceof KeyRequest)
                handleKeyRequest((KeyRequest)request);
            if (request instanceof RegisterRequest)
                handleRegisterRequest((RegisterRequest)request);
            if (request instanceof UpdateRequest)
                handleUpdateRequest((UpdateRequest) request);
            if (request instanceof StoreRequest)
                handleStoreRequest((StoreRequest) request);
            if (request instanceof CollectionRequest)
                handleCollectionRequest((CollectionRequest)request);
            if (request instanceof ChatRequest)
                handleChatRequest((ChatRequest) request);
            if (request instanceof PlayRequest)
                handlePlayRequest((PlayRequest) request);
            if (request instanceof GameRequest)
                handleGameRequest((GameRequest)request);
        }

        //TODO after client disconnected
        Server.getInstance().logout(user, this);
    }

    private void handleDeleteAccount(DeleteAccount request) {
        try {
            Server.getInstance().deleteAccount(request.getName(), request.getPassword());
            writeResponse(new AccountDeleted());
        } catch (Exception e) {
            e.printStackTrace();
            writeResponse(new RejectResponse(e.getMessage()));
        }
    }
    private void handleGameRequest(GameRequest request) {
        try {
            gameCommandOutput.writeObject(request.getCommand());
        } catch (Exception e) {
            e.printStackTrace();
            writeResponse(new RejectResponse("you are not inside battle Ground"));
        }
    }
    private void handlePlayRequest(PlayRequest request) {
        if (gameResponseReader.isAlive()) {
            writeResponse(new RejectResponse("already inside battle Ground"));
            return;
        }
        Server.getInstance().addToMatchQueue(this, request);
    }
    private void handleChatRequest(ChatRequest request) {
        Server.getInstance().writeMessage(user.getUserName(), request.getMessage());
    }
    private void handleCollectionRequest(CollectionRequest request) {
        Deck deck = null;
        Card card = null;
        Hero hero = null;

        for (Deck deck1 : user.getDecks())
            if (request.getDeckName().equals(deck1.getName())) {
                deck = deck1;
                break;
            }
        for (Card card1 : Market.getInstance().getCardsForSale())
            if (card1.getName().equals(request.getCardName())) {
                card = card1;
                break;
            }
        for (Hero hero1 : HeroManager.getInstance().getHeroes())
            if (hero1.getHeroName().equals(request.getHeroName())) {
                hero = hero1;
                break;
            }
        try {
            switch (request.getMessage()) {
                case addToDeck:
                    deck.addCard(card);
                    break;
                case changeHero:
                    deck.setHero(hero);
                case changeName:
                    if (user.getDecks().stream().anyMatch(deck1 -> deck1.getName().equals(request.getNewName())))
                        deck.setName(request.getNewName());
                    else
                        throw new Exception("deck with given name already exist");
                    break;
                case chooseDeck:
                    user.setCurrentDeck(user.getDecks().indexOf(deck));
                    break;
                case createDeck:
                    user.addDeck(new Deck(request.getDeckName(), hero));
                    break;
                case removeDeck:
                    user.removeDeck(deck);
                    break;
                case removeFromDeck:
                    deck.removeCard(card);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            writeResponse(new RejectResponse(e.getMessage()));
        }
    }
    private void handleStoreRequest(StoreRequest request) {
        try {
            Card card = CardManager.getInstance().getCard(request.getCard());
            if (user.hasCard(card))
                Market.getInstance().sell(card, user);
            else
                Market.getInstance().buy(card, user);
        }catch (Exception e) {
            e.printStackTrace();
        }


    }
    private void handleUpdateRequest(UpdateRequest request) {
        switch (request.getState()) {
            case store:
                Map<Card, Boolean> map = new HashMap<>();
                for (Card card : Market.getInstance().getCardsForSale()) {
                    map.put(card, user.hasCard(card));
                }
                writeResponse(new StoreData(Market.getInstance().getCardsForSale(), map, user.getCoins()));
                break;
            case collection:
                writeResponse(
            new CollectionData(user.getDecks(),Market.getInstance().getCardsForSale(), HeroManager.getInstance().getHeroes(), user.getIndexOfDeck()));
                break;
            case status:
                List<User> users = new ArrayList<>();
                for (User user1 : UserDB.getUsers())
                    if (!user1.getUserName().contains("Default"))
                        users.add(user1);

                writeResponse(new StatusData(user.getDecks(), users));


        }
    }
    private void handleRegisterRequest(RegisterRequest request) {
        try {
            key = Server.getInstance().register(request.getName(), request.getPassword());
            user = UserDB.getUser(request.getName(), request.getPassword());
            writeResponse(new KeyResponse(key));
        } catch (Exception e) {
            e.printStackTrace();
            writeResponse(new RejectResponse(e.getMessage()));
        }
    }
    private void handleKeyRequest(KeyRequest request) {
        try {
             key = Server.getInstance().login(request.getName(), request.getPassword());
             user = UserDB.getUser(request.getName(), request.getPassword());
             writeResponse(new KeyResponse(key));
        } catch (Exception e) {
            e.printStackTrace();
            writeResponse(new RejectResponse(e.getMessage()));
        }
    }


    public Request getNextRequest() {
        try {
            return (Request) input.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public synchronized void writeResponse(Response response) {
        try {
            output.reset();
            output.writeObject(response);
            if (user != null)
                user.getLog().writeEvent("responded", response.getClass().getSimpleName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void connectToGame(ObjectInput gameResponseInput, ObjectOutput gameCommandOutput, int index) {
        this.gameCommandOutput = gameCommandOutput;
        this.gameResponseInput = gameResponseInput;
        this.indexOfPlayer = index;
        try {
            Game.CommandAndResponse.Response response = (Game.CommandAndResponse.Response) gameResponseInput.readObject();
            writeResponse(new OpponentFound(response.getGame(), index));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        gameResponseReader.start();
    }

    public User getUser() {
        return user;
    }
}
