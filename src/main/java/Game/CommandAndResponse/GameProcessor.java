package Game.CommandAndResponse;

import DB.Managment.GameConfigManager;
import DB.components.cards.Deck;
import DB.components.cards.Passive;
import Game.GameStructure.Attackable;
import Game.GameStructure.CardModels.CardModel;
import Game.GameStructure.CardModels.Loader;
import Game.GameStructure.Cards.Card;
import Game.GameStructure.Game;
import Game.GameStructure.Player;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.List;

public class GameProcessor extends Thread{
    private Map <Player, ObjectInput> commandStreams;
    private Map<Player, ObjectOutput> responseStreams;

    private Map<Player, ObjectOutput> commandInputs;
    private Map<Player, ObjectInput> responseOutputs;

    private Game game;
    private boolean training = false;

    private List<CardModel> observers = new ArrayList<>();
    private TargetReceiver targetReceiver;
    private TurnTimer turnTimer;
    private ManaSetter manaSetter;
    private Dealer dealer;

    public static class TurnTimer extends Thread {
        public static final int delayInSeconds = 60;
        protected boolean flag = false;
        protected GameProcessor processor;
        protected Game game;
        @Override
        public void run() {
            super.run();
            processor.endTurn();
            while (true) {
                flag = false;
                for (int i = 0; i < delayInSeconds * 5; ++i) {
                    try {
                        sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (flag) {
                        break;
                    }
                }
                if (!flag)
                    processor.endTurn();
            }
        }

        public TurnTimer(GameProcessor processor) {
            this.processor = processor;
            this.game = processor.game;
        }

        public void endTurn() {
            flag = true;
            processor.endTurn();
        }

    }
    public static class ManaSetter{
        protected final GameProcessor processor;
        protected final Game game;

        public void setMana() {
            game.getPlayerOnTurn().setMana(Math.min(10, (game.getNumberOfTurns() + 1) / 2));
        }

        public ManaSetter(GameProcessor processor) {
            this.processor = processor;
            this.game = processor.game;
        }
    }
    public static class Dealer{
        protected final GameProcessor processor;
        protected final Game game;

        public void deal() {
            try {
                if (game.getNumberOfTurns() <= 2) {
                    Player player = game.getPlayerOnTurn();
                        try {
                            for (int i = 0; i < 3; ++i) {
                                player.drawFromDeck();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                }
                else{
                    game.getPlayerOnTurn().drawFromDeck();
                }

            } catch (Exception exception) {
                exception.printStackTrace();
            }

            if (game.getNumberOfTurns() <= 2) {
               setPlayerPassive(game.getPlayerOnTurn());
            }
        }

        public void setPlayerPassive(Player player) {
            List<Passive> passives = new ArrayList<>();
            for (int i = 0; i < 3; ++i) {
                int indx = new Random().nextInt(5);
                if (passives.contains(Passive.map.get(indx))) --i;
                else passives.add(Passive.map.get(indx));
            }

            Passive passive = (Passive) processor.targetReceiver.discover(passives);
            try {
                DB.components.cards.Card myPassive = new DB.components.cards.Card();
                myPassive.setName(passive.name());
                Card myPassiveCard = new Card(myPassive, player){};
                Constructor passiveConstructor = Loader.getInstance()
                        .getModel(myPassiveCard);

                CardModel card = (CardModel)passiveConstructor
                        .newInstance(this, player, myPassiveCard);

                card.play(new PlayCommand(player.getName(), -1, 0));


            } catch (Exception ignore) {
                ignore.printStackTrace();
            }

        }

        public Dealer(GameProcessor processor) {
            this.processor = processor;
            this.game = processor.game;
        }
    }
    //---------------
    // process commands

    @Override
    public void run() {
        super.run();
        while(true){
            Command command  = receiveCommand();
            try {
               validateCommand(command);
            }catch (Exception e) {
                System.out.println(e.getMessage());
                //TODO log the error
                respond(Response.Message.reject);
                continue;
            }
            if (command instanceof EndTurnCommand)
                processEndTurnCommand((EndTurnCommand) command);
            if (command instanceof PlayCommand)
                processPlayCommand((PlayCommand) command);
            if (command instanceof AttackCommand)
                processAttackCommand((AttackCommand)command);
            if (command instanceof HeroPowerCommand)
                processUsePowerCommand((HeroPowerCommand) command);
            notifyObservers(command);

            sendToAll(Response.Message.update);

        }
    }
    public void validateCommand(Command command) throws Exception{
        for (CardModel observer : observers) {
            observer.observeBefore(command);
        }
    }
    public void notifyObservers(Command command) {
        for (int i = 0; i < observers.size(); i++) {
            observers.get(i).observeAfter(command);
        }
    }

    private void processAttackCommand(AttackCommand command) {
        command.getAttacker(game, command).attack((Attackable)getTargetReceiver().receiveTarget(o -> {
            if (!(o instanceof Attackable))
                return false;
            if (((Attackable) o).getPlayer().equals(command.getAttacker(game, command).getPlayer()))
                return false;
            return true;
        }));

    }
    private void processPlayCommand(PlayCommand command) {
        if (!command.getPlayerName().equals(game.getPlayerOnTurn().getName())) {
            respond(Response.Message.reject);
            return;
        }
        try {
            game.getPlayerOnTurn().playCard(command);
            sendToAll(Response.Message.update);
        } catch (Exception exception) {
            exception.printStackTrace();
            respond(Response.Message.reject);
        }

    }
    private void processEndTurnCommand(EndTurnCommand command) {
        turnTimer.endTurn();
    }
    private void processUsePowerCommand(HeroPowerCommand command) {
        try {
            game.getPlayerOnTurn().usePower(command.getPower());
        } catch (Exception exception) {
            respond(Response.Message.reject);
        }
    }

    public void sendToAll(Response.Message message) {
        responseStreams.forEach((player, stream) ->
        {
            try {
                List<Integer> list = new ArrayList<>();
                if (!training)
                  list.add(1 - game.getPlayers().indexOf(player));
                stream.writeObject(new Response(game, message,list));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
    public void respond(Response.Message message) {
        List<Integer> list = new ArrayList<>();
        list.add(1 - game.getPlayers().indexOf(game.getPlayerOnTurn()));
        sendResponse(new Response(game, message, list));
    }
    public void sendResponse(Response response) {
        try {
            responseStreams.get(game.getPlayerOnTurn()).writeObject(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Command receiveCommand() {
        try {
            if (training) {
                while(commandStreams.get(game.getPlayerOnTurn()).available() < 1 && commandStreams.get(game.getPlayers().get(1 - game.getPlayers().indexOf(game.getPlayerOnTurn()))).available() < 1)
                    Thread.sleep(200);
                return (Command)commandStreams.get(game.getPlayerOnTurn()).readObject();
            }
            while(commandStreams.get(game.getPlayerOnTurn()).available() < 1)
                Thread.sleep(200);
            return (Command)commandStreams.get(game.getPlayerOnTurn()).readObject();
        } catch (IOException | ClassNotFoundException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
    public void finishGame(Player winner) {
        for (Player player : game.getPlayers()) {
            try {
                List<Integer> list = new ArrayList<>();
                list.add(1 - game.getPlayers().indexOf(player));
                responseStreams.get(player).writeObject(new EndOfGame(game,winner, list));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        turnTimer.stop();
    }
    public void endTurn() {
        game.setNumberOfTurns(game.getNumberOfTurns() + 1);

        try {
            commandStreams.get(game.getPlayerOnTurn()).skip(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        sendToAll(Response.Message.update);
        manaSetter.setMana();
        dealer.deal();


        sendToAll(Response.Message.endTurn);
    }
    //----------------
    // constructors
    public GameProcessor(Game game, Deck[] playerDecks, String[] playerNames, List<Class> features) throws Exception{
        this.game = game;
        this.targetReceiver = new TargetReceiver(this);

        this.turnTimer = new TurnTimer(this);
        this.dealer = new Dealer(this);
        this.manaSetter = new ManaSetter(this);

        Player player = new Player(playerNames[0], game, playerDecks[0], this);
        Player enemy = new Player(playerNames[1], game,playerDecks[1], this);

        game.getPlayers().add(player);
        game.getPlayers().add(enemy);

        List<Player> players = new ArrayList<>();
        players.add(player);
        players.add(enemy);

        initStreams(players);

        if (features != null)
            addFeatures(features);


        sendToAll(Response.Message.update);
    }

    public GameProcessor(String configName) throws Exception {
        this(new Game(), GameConfigManager.getInstance()
                .load(configName), new String[]{"player1", "player2"}, null);

    }

    public GameProcessor(Game game, Deck deck, String playerName) throws Exception {
        this(game, new Deck[]{deck, deck}, new String[]{playerName, playerName + "1"}, null);
        commandStreams.put(game.getPlayers().get(1), commandStreams.get(game.getPlayers().get(0)));
        commandInputs.put(game.getPlayers().get(1), commandInputs.get(game.getPlayers().get(0)));
        responseStreams.put(game.getPlayers().get(1), responseStreams.get(game.getPlayers().get(0)));
        responseOutputs.put(game.getPlayers().get(1), responseOutputs.get(game.getPlayers().get(0)));
        training = true;

    }

    private void initStreams(List<Player> players) {

        commandStreams = new HashMap<>();
        responseStreams = new HashMap<>();
        commandInputs = new HashMap<>();
        responseOutputs = new HashMap<>();

        for (Player player : players) {
            Queue queue = new LinkedList();

            commandInputs.put(player, new ObjectOutput() {
                @Override
                public void writeObject(Object obj) {
                    synchronized (queue) {
                        queue.add(obj);
                    }
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
            });
            commandStreams.put(player, new ObjectInput() {
                @Override
                public synchronized Object readObject() {
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
                public synchronized long skip(long n) {
                    queue.clear();
                    return 0;
                }

                @Override
                public int available() {
                    return queue.size();
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
            });
        }
        for (Player player : players) {
            Queue queue = new LinkedList();

            responseStreams.put(player, new ObjectOutput() {
                @Override
                public synchronized void writeObject(Object obj) throws IOException {

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
            });
            responseOutputs.put(player, new ObjectInput() {
                @Override
                public synchronized Object readObject()  {
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
            });
        }

    }
    private void addFeatures(List<Class> features) throws Exception {
        for (Class featureClass : features) {
            if (featureClass.getSimpleName().contains("TurnTimer")) {
                turnTimer = (TurnTimer)featureClass
                        .getConstructor(new Class[]{GameProcessor.class})
                        .newInstance(new Object[]{this});
            }
            if (featureClass.getSimpleName().contains("ManaSetter")) {
                manaSetter = (ManaSetter)featureClass
                        .getConstructor(new Class[]{GameProcessor.class})
                        .newInstance(new Object[]{this});
            }
            if (featureClass.getSimpleName().contains("Dealer")) {
                dealer = (Dealer)featureClass
                        .getConstructor(new Class[]{GameProcessor.class})
                        .newInstance(new Object[]{this});
            }

            if (featureClass.getSimpleName().contains("CardModel")) {
                observers.add((CardModel) featureClass
                        .getConstructor(new Class[]{GameProcessor.class, Player.class, Card.class})
                        .newInstance(new Object[]{this, null, null}));
            }
        }
    }
    //--------------
    //start processing commands

    public void startGame() {
        turnTimer.start();
        start();
    }
    //--------------
    // getters
    public Game getGame() {
        return game;
    }

    public List<CardModel> getObservers() {
        return observers;
    }

    public TargetReceiver getTargetReceiver() {
        return targetReceiver;
    }

    public Map<Player, ObjectOutput> getCommandInputs() {
        return commandInputs;
    }

    public Map<Player, ObjectInput> getResponseOutputs() {
        return responseOutputs;
    }
}
