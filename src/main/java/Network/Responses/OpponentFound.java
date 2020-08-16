package Network.Responses;

import Game.GameStructure.Game;

public class OpponentFound extends Response{
    private Game game;
    private int indexOfPlayer;

    public OpponentFound(Game game, int indexOfPlayer) {
        this.game = game;
        this.indexOfPlayer = indexOfPlayer;
    }

    public Game getGame() {
        return game;
    }

    public int getIndexOfPlayer() {
        return indexOfPlayer;
    }
}
