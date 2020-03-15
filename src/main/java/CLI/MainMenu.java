package CLI;

import User.User;
import User.UserDB;

public class MainMenu extends Menu{
    public MainMenu(User user) {
        super(user);
    }
    @Override
    protected String run(String command) {
        String[] in = command.split(" ");

        if (in.length == 1 && in[0].equals("delete-player")) {
            System.out.println("PassWord");
            String pass = myScanner.getMyScanner().read();
            if (pass.equals(user.getPassWord())) {
                UserDB.deleteUser(user);
                System.exit(0);
            }
            else {
                return "Wrong PassWord";
            }
        }

        if (in.length == 1 && in[0].equals("collections")) {
            new CollectionMenu(user).exec();
            return "";
        }
        if (in.length == 1 && in[0].equals("store")) {
            new StoreMenu(user).exec();
            return "";
        }


        return "invalid command!";
    }
}
