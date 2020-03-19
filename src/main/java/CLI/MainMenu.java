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
                user.getLog().writeEvent("sign_out", "");
                System.exit(0);
            }
            else {
                return "Wrong PassWord";
            }
        }

        if (in.length == 1 && in[0].equals("collections")) {
            user.getLog().writeEvent("Navigate", "collections");
            new CollectionMenu(user).exec();
            user.getLog().writeEvent("Navigate", "Main Menu");
            return "";
        }
        if (in.length == 1 && in[0].equals("store")) {
            user.getLog().writeEvent("Navigate", "store");
            new StoreMenu(user).exec();
            user.getLog().writeEvent("Navigate", "Main Menu");
            return "";
        }


        return "invalid command!";
    }

    @Override
    protected void showCommandList() {
        System.out.println("\nMain Menu :" +
                           "\ncollections         | go to your collection (heroes, decks, etc)" +
                           "\nstore               | go to store Menu to buy or sell cards" +
                           "\ndelete-player       | delete your account");

    }
}
