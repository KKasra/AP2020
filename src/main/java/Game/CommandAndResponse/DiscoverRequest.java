package Game.CommandAndResponse;

import Game.GameStructure.Game;

import java.util.List;

public class DiscoverRequest extends Response{
    private List objects;
    public DiscoverRequest(Game game, List objects, List<Integer> list) {
        super(game, Message.discover, list);
        this.objects = objects;
    }

    public List getObjects() {
        return objects;
    }
}
