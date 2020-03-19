package CLI;

import User.User;
import User.UserDB;

public class Login extends Menu {
    public Login(User user) {
        super(user);
    }
    @Override
    public void exec() {
        System.out.println(color.get("Green"));
        System.out.println("+------------------------------------------+");
        System.out.println("|                                          |");
        System.out.println("|                 Welcome to               |");
        System.out.println("|                HearthStone!              |");
        System.out.println("|                                          |");
        System.out.println("+------------------------------------------+");
        System.out.println(color.get("Reset"));
        System.out.println("\n\nalready have an account ? Y/N");
        super.exec();
    }

    @Override
    protected String run(String command) {
        if (command.trim().equals("y") || command.trim().equals("Y")) {
            System.out.print("UserName: ");
            String name = myScanner.getMyScanner().read();
            System.out.print("PassWord: ");
            String pass = myScanner.getMyScanner().read();
            try {
                user = UserDB.getUser(name, pass);
                user.getLog().writeEvent("sign_in", "");
                new MainMenu(user).exec();
            } catch (Exception e) {
                return e.toString();
            }
        }
        if (command.trim().equals("n") || command.trim().equals("N")) {
            System.out.println("UserName: ");
            String name = myScanner.getMyScanner().read();
            System.out.println("PassWord: ");
            String pass = myScanner.getMyScanner().read();
            user = new User(name, pass);
            try{
                UserDB.addUser(user);
            } catch (Exception e) {
                return e.toString();
            }

            user.getLog().writeEvent("sign_in", "");
            new MainMenu(user).exec();
            }
        return "invalid Command!";
    }

    @Override
    protected void showCommandList() {

    }
}
