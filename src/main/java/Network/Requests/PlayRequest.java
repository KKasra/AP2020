package Network.Requests;


public class PlayRequest extends Request {
    public enum PlayMode{
        classic, deckReader, tavernBrawl, training
    }
    private PlayMode playMode;
    public PlayRequest(String key, PlayMode playMode) {
        super(key);
        this.playMode = playMode;
    }

    public PlayMode getPlayMode() {
        return playMode;
    }
}
