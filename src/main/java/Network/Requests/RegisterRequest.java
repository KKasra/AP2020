package Network.Requests;

public class RegisterRequest extends Request{

    private String name;
    private String password;

    public RegisterRequest(String name, String password) {
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
