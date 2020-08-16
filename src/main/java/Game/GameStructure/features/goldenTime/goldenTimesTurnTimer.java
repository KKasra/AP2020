package Game.GameStructure.features.goldenTime;

import Game.CommandAndResponse.GameProcessor;
import Game.GameStructure.Player;

import java.util.HashMap;
import java.util.Map;

public class goldenTimesTurnTimer extends GameProcessor.TurnTimer {
    public static final int timeInSeconds = 120;
    private Map<Player, Integer> timeLeft;
    public goldenTimesTurnTimer(GameProcessor processor) {
        super(processor);

        timeLeft = new HashMap<>();
        for (Player player : game.getPlayers()) {
            timeLeft.put(player, timeInSeconds * 5);
        }
    }

    @Override
    public void run() {
        super.run();
        while (true) {
            flag = false;
            while(timeLeft.get(game.getPlayerOnTurn()) >= 0){
                timeLeft.put(game.getPlayerOnTurn(), timeLeft.get(game.getPlayerOnTurn()) - 1);
                try {
                    sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (flag) {
                    break;
                }
            }
            if (timeLeft.get(game.getPlayerOnTurn()) <= 0) {
                for (Player player : game.getPlayers()) {
                    if (!player.equals(game.getPlayerOnTurn()))
                        processor.finishGame(player);
                }
            }
            processor.endTurn();
        }

    }


}
