package Game.CommandAndResponse;

import Game.GameStructure.Game;
import Game.GameStructure.Player;

public class EndOfGame extends Response {
    public EndOfGame(Game game, Player winner) {
        super(game, Message.endOfGame);
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
