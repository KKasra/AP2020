package Network.Responses;

public class GameResponse extends Response{
    private final Game.CommandAndResponse.Response response;

    public GameResponse(Game.CommandAndResponse.Response response) {
        this.response = response;
    }

    public Game.CommandAndResponse.Response getResponse() {
        return response;
    }
}
