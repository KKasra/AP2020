package Game.CommandAndResponse;

import DB.Managment.GameConfigManager;
import DB.Managment.HeroManager;
import DB.components.User;
import DB.components.cards.Deck;
import Game.GameStructure.Attackable;
import Game.GameStructure.CardModels.CardModel;
import Game.GameStructure.Game;
import Game.GameStructure.Player;

import java.util.*;

public class GameProcessor extends Thread{
    private ObjectInput commandInput;
    private ObjectOutput responseOutput;

    private ObjectOutput commandOutput;
    private ObjectInput responseInput;

    private Game game;
    private TurnTimer currentTimer;

    private List<CardModel> observers = new ArrayList<>();
    private TargetReceiver targetReceiver;

    @Override
    public void run() {
        super.run();
        while(true){
            Command command  = receiveCommand();

            try {
                for (CardModel observer : observers) {
                    observer.observeBefore(command);
                }
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

            for (int i = 0; i < observers.size(); i++) {
                observers.get(i).observeAfter(command);
            }
//            respond(Response.Message.update);
        }
    }
    private void processAttackCommand(AttackCommand command) {
        if (!command.getAttacker().getPlayer().equals(game.getPlayerOnTurn())) {
            respond(Response.Message.reject);
            return;
        }
        command.getAttacker().attack((Attackable)getTargetReceiver().receiveTarget(o -> {
            if (!(o instanceof Attackable))
                return false;
            if (((Attackable) o).getPlayer() == command.getAttacker().getPlayer())
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
            respond(Response.Message.update);
        } catch (Exception exception) {
            exception.printStackTrace();
            respond(Response.Message.reject);
        }

    }
    private void processEndTurnCommand(EndTurnCommand command) {
        if (command.getProducer() instanceof TurnTimer && command.getProducer() != currentTimer)
            return;

        game.setNumberOfTurns(game.getNumberOfTurns() + 1);
        game.getPlayerOnTurn().setMana(Math.min(10, (game.getNumberOfTurns() + 1) / 2));

        try {
            if (game.getNumberOfTurns() == 1) {
                //TODO three first Cards in PLayers Hand
                for (Player player : game.getPlayers()) {
                    try {
                        for (int i = 0; i < 3; ++i) {
                            player.drawFromDeck();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            else{
                game.getPlayerOnTurn().drawFromDeck();
            }

        } catch (Exception exception) {}



        respond(Response.Message.endTurn);
        if (currentTimer != null)
            currentTimer.stop();
        currentTimer = new TurnTimer(this);
    }
    private void processUsePowerCommand(HeroPowerCommand command) {
        if (command.getPlayer() != game.getPlayerOnTurn() ||
                command.getPower().getCard().getPlayer() != command.getPlayer()){
            respond(Response.Message.reject);
            return;
        }


        try {
            command.getPlayer().usePower(command.getPower());
        } catch (Exception exception) {
            respond(Response.Message.reject);
            User.user.getLog().writeEvent("game", exception.getMessage());
        }
    }

    public void finishGame(Player winner) {
        responseOutput.write(new EndOfGame(game, winner));
    }

    public void respond(Response.Message message) {
        responseOutput.write(new Response(game, message));
    }
    public Command receiveCommand() {
        return (Command)commandInput.read();
    }


    public static class TurnTimer extends Thread{
        public static final int turnLengthMS = 60 * 1000;
        private GameProcessor processor;
        @Override
        public synchronized void run() {
            super.run();
            try {
                wait(turnLengthMS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            processor.getCommandOutput().write(new EndTurnCommand(this));
        }
        public TurnTimer(GameProcessor processor) {
            this.processor = processor;
            start();
        }

    }


    private class ObjectReader implements ObjectInput{
        private Deque deque;
        ObjectReader(Deque deque){
            this.deque = deque;
        }
        @Override
        public synchronized Object read() {
            while(deque.size() == 0) {
                try {
                    sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return deque.pollFirst();
        }

        @Override
        public synchronized Object observe() {
            while (deque.isEmpty()) {
                try {
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return deque.getFirst();
        }

        @Override
        public boolean isEmpty() {
            return deque.isEmpty();
        }
    }
    private class ObjectWriter implements ObjectOutput{
        private Deque deque;
        ObjectWriter(Deque deque) {
            this.deque = deque;
        }
        @Override
        public synchronized void write(Object o) {
            deque.add(o);
        }
    }

    public GameProcessor(Game game, Deck[] playerDecks, String[] playerNames) throws Exception{
        this.game = game;
        this.targetReceiver = new TargetReceiver(this);

        Player player = new Player(playerNames[0], game, playerDecks[0], this);
        Player enemy = new Player(playerNames[1], game,playerDecks[1], this);

        game.getPlayers().add(player);
        game.getPlayers().add(enemy);

        ArrayDeque commands = new ArrayDeque();
        ArrayDeque responses = new ArrayDeque();


        commandInput = new ObjectReader(commands);
        commandOutput = new ObjectWriter(commands);

        responseInput = new ObjectReader(responses);
        responseOutput = new ObjectWriter(responses);

        commandOutput.write(new EndTurnCommand(this));
        start();
    }
    public GameProcessor(String configName) throws Exception {
        this(new Game(), GameConfigManager.getInstance()
                .load(configName), new String[]{"player", "enemy"});

    }
    public ObjectOutput getCommandOutput() {
        return commandOutput;
    }
    public ObjectInput getResponseInput() {
        return responseInput;
    }
    public Game getGame() {
        return game;
    }
    public List<CardModel> getObservers() {
        return observers;
    }

    public TargetReceiver getTargetReceiver() {
        return targetReceiver;
    }
}
