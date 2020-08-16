package Network.Requests;

public class KeyRequest extends Request{
    private String name;
    private String password;
    public KeyRequest(String name, String password) {
        super(null);
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}

