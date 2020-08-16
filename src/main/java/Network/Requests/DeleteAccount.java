package Network.Requests;

public class DeleteAccount extends Request{
    private String name;
    private String password;

    public DeleteAccount(String key, String name, String password) {
        super(key);
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
