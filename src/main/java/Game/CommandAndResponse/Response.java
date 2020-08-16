package Game.CommandAndResponse;

import Game.GameStructure.Game;

import java.io.Serializable;
import java.util.List;

public class Response implements Serializable {
    public enum  Message{
        reject, selectATarget, update, endTurn, endOfGame, targetIsValid, discover
    }
    private Message message;
    private Game game;

    public Response(Game game, Message message, List<Integer> restrictedInfoList) {
        this.message = message;
        this.game = game;
        for (Integer integer : restrictedInfoList) {
            this.game = game.restrictInfoOf(integer);
        }
    }

    public Message getMessage() {
        return message;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }
}
