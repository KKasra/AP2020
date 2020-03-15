package CLI;

import User.User;
import User.UserDB;

public class Login extends Menu {
    public Login(User user) {
        super(user);
    }
    @Override
    public void exec() {
        System.out.println("+------------------------------------------+");
        System.out.println("|                                          |");
        System.out.println("|                 Welcome to               |");
        System.out.println("|                HearthStone!              |");
        System.out.println("|                                          |");
        System.out.println("+------------------------------------------+");
        System.out.println("\n\nalready have an account ? Y/N");
        super.exec();
    }

    @Override
    protected String run(String command) {
        if (command.equals("y") || command.equals("Y")) {
            System.out.print("UserName: ");
            String name = myScanner.getMyScanner().read();
            System.out.print("PassWord: ");
            String pass = myScanner.getMyScanner().read();
            try {
                user = UserDB.getUser(name, pass);
                new MainMenu(user).exec();
            } catch (Exception e) {
                return e.toString();
            }
        }
        if (command.equals("n") || command.equals("N")) {
            System.out.println("UserName: ");
            String name = myScanner.getMyScanner().read();
            System.out.println("PassWord: ");
            String pass = myScanner.getMyScanner().read();
            user = new User(name, pass);
            new MainMenu(user).exec();
            }
        return "invalid Command!";
    }
}
