package Game.CommandAndResponse;

import Game.GameStructure.Game;
import Game.GameStructure.Player;

import java.util.List;

public class EndOfGame extends Response {
    public EndOfGame(Game game, Player winner, List<Integer> list) {
        super(game, Message.endOfGame, list);
        this.winner = winner;
    }
    private Player winner;

    public Player getWinner() {
        return winner;
    }

    @Override
    public Message getMessage() {
        return super.getMessage();
    }
}
