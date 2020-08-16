package Network.Requests;

public class StoreRequest extends Request{
    private String card;
    public StoreRequest(String key, String card) {
        super(key);
        this.card = card;
    }

    public String getCard() {
        return card;
    }
}
