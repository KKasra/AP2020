package Network.Responses;

public class ChatUpdate extends Response{
    private String sender;
    private String message;

    public ChatUpdate(String sender, String message) {
        this.sender = sender;
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public String getMessage() {
        return message;
    }
}
