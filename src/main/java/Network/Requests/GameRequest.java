package Network.Requests;

import Game.CommandAndResponse.Command;

public class GameRequest extends Request{
    private final Command command;

    public GameRequest(String key, Command command) {
        super(key);
        this.command = command;
    }

    public Command getCommand() {
        return command;
    }
}
