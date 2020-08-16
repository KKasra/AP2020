package Network.Requests;

public class ChatRequest extends Request{
    private String message;
    public ChatRequest(String key, String message) {
        super(key);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
