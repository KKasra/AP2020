package Network.Responses;

public class KeyResponse extends Response{
    private String key;

    public KeyResponse(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

}
