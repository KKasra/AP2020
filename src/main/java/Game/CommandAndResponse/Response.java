package Game.CommandAndResponse;

import Game.GameStructure.Game;

import java.io.Serializable;

public class Response implements Serializable {
    public static enum  Message{
        reject, selectATarget, update, endTurn, endOfGame, targetIsValid
    }
    private Message message;


    public Message getMessage() {
        return message;
    }

    public Response(Game game, Message message) {
        this.message = message;
    }

    public Response() {
        super();
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
