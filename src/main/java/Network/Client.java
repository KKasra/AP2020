package Network;

import DB.components.cards.Card;
import DB.components.cards.Deck;
import DB.components.heroes.Hero;
import GUI.Frames.AddressFrame;
import GUI.Frames.GameFrame;
import GUI.Frames.MenuFrame;
import GUI.Frames.WarningFrame;
import GUI.MenuPanels.*;
import Game.CommandAndResponse.Command;
import Network.Requests.*;
import Network.Responses.*;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

public class Client extends Thread{
    private final Socket socket;
    private final ObjectInput input;
    private final ObjectOutput output;

    private String key;
    private String name;

    private Network.Requests.State state;
    private ObjectOutput gameResponseOutput;
    private ObjectInput gameCommandInput;

    private Thread updateThread = new Thread() {
        @Override
        public void run() {
            super.run();
            while(true){
                try {
                    sleep(200);
                    if (state != Network.Requests.State.neutral && state != Network.Requests.State.game)
                        writeRequest(new UpdateRequest(key, state));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private Thread gameCommandReader = new Thread(){
        @Override
        public void run() {
            super.run();
            while(true) {
                try {
                    Command command = (Command)gameCommandInput.readObject();
                    writeRequest(new GameRequest(key, command));

                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private Client() throws IOException {
        state = Network.Requests.State.neutral;
        socket = new Socket();
        connectToServer();
        output = new ObjectOutputStream(socket.getOutputStream());
        input = new ObjectInputStream(socket.getInputStream());

        MenuFrame.getInstance().setVisible(true);
        ChatPanel.getInstance().setSendAction(a -> sendMessage(ChatPanel.getInstance().getMessage()));
        updateThread.start();
    }

    private void connectToServer() {
        AddressFrame.getInstance().setConnectionListener(e -> {
            try {
                socket.connect(new InetSocketAddress(
                        AddressFrame.getInstance().getDomain(), AddressFrame.getInstance().getPort()), 5000);
                synchronized (socket) {
                    socket.notify();
                }
            } catch (IOException ioException) {
                ioException.printStackTrace();
                WarningFrame.print("Connection time out");
            }
        });

        AddressFrame.getInstance().setVisible(true);

        synchronized (socket) {
            try {
                socket.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        AddressFrame.getInstance().setVisible(false);

    }

    //----------------------
    // command & response
    @Override
    public void run() {
        super.run();
        while(true) {
            Response response = getNextResponse();
            if (response instanceof RejectResponse)
                handleRejectResponse((RejectResponse) response);
            if (response instanceof KeyResponse)
                handleKeyResponse((KeyResponse)response);
            if (response instanceof StoreData)
                handleStoreData((StoreData) response);
            if (response instanceof CollectionData)
                handleCollectionData((CollectionData)response);
            if (response instanceof StatusData)
                handelStatusData((StatusData)response);
            if (response instanceof ChatUpdate)
                handleChatUpdate((ChatUpdate)response);
            if (response instanceof OpponentFound)
                handleOpponentFound((OpponentFound) response);
            if (response instanceof GameResponse)
                handleGameResponse((GameResponse)response);
            if (response instanceof AccountDeleted)
                handleAccountDeleted((AccountDeleted) response);
        }
    }

    private void handleAccountDeleted(AccountDeleted response) {
        name = null;
        key = null;
        MenuFrame.getInstance().setPanel(Loginpanel.getInstance());
    }
    private void handleGameResponse(GameResponse response) {
        try {
            gameResponseOutput.writeObject(response.getResponse());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void handleChatUpdate(ChatUpdate response) {
        ChatPanel.getInstance().receiveNewMassage(response.getSender(), response.getMessage());
    }
    private void handelStatusData(StatusData response) {
        StatusPanel.getInstance().update(response);
    }
    private void handleCollectionData(CollectionData response) {
        CollectionPanel.getInstance().update(response);
    }
    private void handleStoreData(StoreData data) {
        StorePanel.getInstance().update(data);
    }
    private void handleRejectResponse(RejectResponse response) {
        WarningFrame.print(response.getMessage());
    }
    private void handleKeyResponse(KeyResponse response) {
        if (key != null)
            return;
        key = response.getKey();
        MenuFrame.getInstance().setPanel(MainMenu.getInstance());
    }
    private void handleOpponentFound(OpponentFound response) {
        try {
            Queue queue = new LinkedList();

            gameResponseOutput = new ObjectOutput() {
                @Override
                public void writeObject(Object obj) throws IOException {
                        queue.add(obj);

                }

                @Override
                public void write(int b) throws IOException {

                }

                @Override
                public void write(byte[] b) throws IOException {

                }

                @Override
                public void write(byte[] b, int off, int len) throws IOException {

                }

                @Override
                public void flush() throws IOException {

                }

                @Override
                public void close() throws IOException {

                }

                @Override
                public void writeBoolean(boolean v) throws IOException {

                }

                @Override
                public void writeByte(int v) throws IOException {

                }

                @Override
                public void writeShort(int v) throws IOException {

                }

                @Override
                public void writeChar(int v) throws IOException {

                }

                @Override
                public void writeInt(int v) throws IOException {

                }

                @Override
                public void writeLong(long v) throws IOException {

                }

                @Override
                public void writeFloat(float v) throws IOException {

                }

                @Override
                public void writeDouble(double v) throws IOException {

                }

                @Override
                public void writeBytes(String s) throws IOException {

                }

                @Override
                public void writeChars(String s) throws IOException {

                }

                @Override
                public void writeUTF(String s) throws IOException {

                }
            };
            ObjectInput responseInput = new ObjectInput() {
                @Override
                public Object readObject() {
                    while(queue.isEmpty()) {
                        try {
                            sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    return queue.poll();
                }

                @Override
                public int read() throws IOException {
                    return 0;
                }

                @Override
                public int read(byte[] b) throws IOException {
                    return 0;
                }

                @Override
                public int read(byte[] b, int off, int len) throws IOException {
                    return 0;
                }

                @Override
                public long skip(long n) throws IOException {
                    return 0;
                }

                @Override
                public int available() throws IOException {
                    return 0;
                }

                @Override
                public void close() throws IOException {

                }

                @Override
                public void readFully(byte[] b) throws IOException {

                }

                @Override
                public void readFully(byte[] b, int off, int len) throws IOException {

                }

                @Override
                public int skipBytes(int n) throws IOException {
                    return 0;
                }

                @Override
                public boolean readBoolean() throws IOException {
                    return false;
                }

                @Override
                public byte readByte() throws IOException {
                    return 0;
                }

                @Override
                public int readUnsignedByte() throws IOException {
                    return 0;
                }

                @Override
                public short readShort() throws IOException {
                    return 0;
                }

                @Override
                public int readUnsignedShort() throws IOException {
                    return 0;
                }

                @Override
                public char readChar() throws IOException {
                    return 0;
                }

                @Override
                public int readInt() throws IOException {
                    return 0;
                }

                @Override
                public long readLong() throws IOException {
                    return 0;
                }

                @Override
                public float readFloat() throws IOException {
                    return 0;
                }

                @Override
                public double readDouble() throws IOException {
                    return 0;
                }

                @Override
                public String readLine() throws IOException {
                    return null;
                }

                @Override
                public String readUTF() throws IOException {
                    return null;
                }
            };

            Queue queue1 = new LinkedList();

            ObjectOutput commandOutput = new ObjectOutput() {
                @Override
                public void writeObject(Object obj) throws IOException {
                        queue1.add(obj);

                }

                @Override
                public void write(int b) throws IOException {

                }

                @Override
                public void write(byte[] b) throws IOException {

                }

                @Override
                public void write(byte[] b, int off, int len) throws IOException {

                }

                @Override
                public void flush() throws IOException {

                }

                @Override
                public void close() throws IOException {

                }

                @Override
                public void writeBoolean(boolean v) throws IOException {

                }

                @Override
                public void writeByte(int v) throws IOException {

                }

                @Override
                public void writeShort(int v) throws IOException {

                }

                @Override
                public void writeChar(int v) throws IOException {

                }

                @Override
                public void writeInt(int v) throws IOException {

                }

                @Override
                public void writeLong(long v) throws IOException {

                }

                @Override
                public void writeFloat(float v) throws IOException {

                }

                @Override
                public void writeDouble(double v) throws IOException {

                }

                @Override
                public void writeBytes(String s) throws IOException {

                }

                @Override
                public void writeChars(String s) throws IOException {

                }

                @Override
                public void writeUTF(String s) throws IOException {

                }
            };
            gameCommandInput = new ObjectInput() {
                @Override
                public Object readObject() {
                    while(queue1.isEmpty()) {
                        try {
                            sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    return queue1.poll();

                }

                @Override
                public int read() throws IOException {
                    return 0;
                }

                @Override
                public int read(byte[] b) throws IOException {
                    return 0;
                }

                @Override
                public int read(byte[] b, int off, int len) throws IOException {
                    return 0;
                }

                @Override
                public long skip(long n) throws IOException {
                    return 0;
                }

                @Override
                public int available() throws IOException {
                    return 0;
                }

                @Override
                public void close() throws IOException {

                }

                @Override
                public void readFully(byte[] b) throws IOException {

                }

                @Override
                public void readFully(byte[] b, int off, int len) throws IOException {

                }

                @Override
                public int skipBytes(int n) throws IOException {
                    return 0;
                }

                @Override
                public boolean readBoolean() throws IOException {
                    return false;
                }

                @Override
                public byte readByte() throws IOException {
                    return 0;
                }

                @Override
                public int readUnsignedByte() throws IOException {
                    return 0;
                }

                @Override
                public short readShort() throws IOException {
                    return 0;
                }

                @Override
                public int readUnsignedShort() throws IOException {
                    return 0;
                }

                @Override
                public char readChar() throws IOException {
                    return 0;
                }

                @Override
                public int readInt() throws IOException {
                    return 0;
                }

                @Override
                public long readLong() throws IOException {
                    return 0;
                }

                @Override
                public float readFloat() throws IOException {
                    return 0;
                }

                @Override
                public double readDouble() throws IOException {
                    return 0;
                }

                @Override
                public String readLine() throws IOException {
                    return null;
                }

                @Override
                public String readUTF() throws IOException {
                    return null;
                }
            };

            GameFrame.launch( commandOutput, responseInput, response.getIndexOfPlayer(), response.getGame());
            gameCommandReader.start();
            state = Network.Requests.State.game;
            MenuFrame.getTheme().clip.stop();
        }catch (Exception exception) {
            exception.printStackTrace();
            WarningFrame.print(exception.getMessage());
            MenuFrame.getInstance().setPanel(CollectionPanel.getInstance());
        }
    }

    public Response getNextResponse() {
        try {
            return (Response)input.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
    public void writeRequest(Request request) {
        try {
            output.writeObject(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //----------------------------
    //game requests

    public void sendPlayRequest(PlayRequest.PlayMode playMode) {
        writeRequest(new PlayRequest(key, playMode));
    }

    //-----------------------------
    // log in & register operations
    public void requestKey(String name, String password){
        writeRequest(new KeyRequest(name, password));
    }
    public void login(String name, String password) {
        requestKey(name, password);
        this.name = name;
    }

    public void register(String name, String password) {
        writeRequest(new RegisterRequest(name, password));
        this.name = name;
    }
    //----------------------------------
    // store requests
    public void sendBuyRequest(String cardName) {
        writeRequest(new StoreRequest(key, cardName));
    }

    //---------------------------------
    //collection requests

    public void setCurrentDeck(Deck deck) {
        CollectionRequest request = new CollectionRequest(key, CollectionRequest.Message.chooseDeck);
        request.setDeckName(deck.getName());
        writeRequest(request);
    }
    public void changeHeroOfDeck(Deck deck, Hero hero) {
        CollectionRequest request = new CollectionRequest(key, CollectionRequest.Message.changeHero);
        request.setDeckName(deck.getName());
        request.setHeroName(hero.getHeroName());
        writeRequest(request);
    }
    public void changeDeckName(Deck deck, String newName) {
        CollectionRequest request = new CollectionRequest(key, CollectionRequest.Message.changeName);
        request.setDeckName(deck.getName());
        request.setNewName(newName);
        writeRequest(request);
    }
    public void removeDeck(Deck deck) {
        CollectionRequest request = new CollectionRequest(key, CollectionRequest.Message.removeDeck);
        request.setDeckName(deck.getName());
        writeRequest(request);
    }
    public void addDeck(String name, Hero hero) {
        CollectionRequest request = new CollectionRequest(key, CollectionRequest.Message.createDeck);
        request.setDeckName(name);
        request.setHeroName(hero.getHeroName());
        writeRequest(request);
    }
    public void addCardToDeck(Deck deck, Card card) {
        CollectionRequest request = new CollectionRequest(key, CollectionRequest.Message.addToDeck);
        request.setDeckName(deck.getName());
        request.setCardName(card.getName());
        writeRequest(request);
    }
    public void removeCardFromDeck(Deck deck, Card card) {
        CollectionRequest request = new CollectionRequest(key, CollectionRequest.Message.removeFromDeck);
        request.setDeckName(deck.getName());
        request.setCardName(card.getName());
        writeRequest(request);
    }
    public void deleteAccount(String password) {
        writeRequest(new DeleteAccount(key, name, password));
    }

    //--------------------------------
    //chat request

    public void sendMessage(String message) {
        writeRequest(new ChatRequest(key, message));
    }

    //----------------------
    //getter

    public String getname() {
        return name;
    }

    //----------------
    //setter
    public void setState(Network.Requests.State state) {
        this.state = state;
    }

    //------------------------------------
    //singleton
    private static Client instance;
    public static Client getInstance() {
        if (instance == null) {
            try {
                instance = new Client();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }
    //------------------------------------
    // starting point
    public static void main(String[] args) {
        getInstance().start();
    }
}
