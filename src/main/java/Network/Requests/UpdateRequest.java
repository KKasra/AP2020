package Network.Requests;

public class UpdateRequest extends Request{
    private State state;
    public UpdateRequest(String key, State state) {
        super(key);
        this.state = state;
    }

    public State getState() {
        return state;
    }
}
