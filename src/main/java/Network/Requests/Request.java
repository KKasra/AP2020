package Network.Requests;

import java.io.Serializable;

public class Request implements Serializable {
    private String key;
    public Request(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
