package Network.Responses;

public class RejectResponse extends Response{
    private String message;

    public RejectResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
