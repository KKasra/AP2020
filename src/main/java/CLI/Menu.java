package CLI;

import User.User;
import User.UserDB;

import java.util.Map;
import java.util.TreeMap;

public abstract class Menu {
    public static Map<String, String> color = new TreeMap<>();
    static {
        color.put("Black", "\u001b[30m");
        color.put("Red", "\u001b[31m");
        color.put("Green", "\u001b[32m");
        color.put("Yellow", "\u001b[33m");
        color.put("Blue", "\u001b[34m");
        color.put("White","\u001b[37m");
        color.put("Reset", "\u001b[0m");
    }
    User user;
    public Menu(User user) {
        this.user = user;
    }
    public void exec() {
        while(true) {
            System.out.print(color.get("Blue") + this.getClass().getSimpleName() + " >> Please Enter Command: " + color.get("Reset"));
            String command = myScanner.getMyScanner().read();
            System.out.println();
            if (command.equals("exit"))
                return;
            if (command.equals("exit -a")) {
                user.getLog().writeEvent("sign_out", "");
                UserDB.saveChanges(user);
                System.exit(0);
            }
            if (command.equals("HearthStone --help")) {
                System.out.println(color.get("Yellow"));
                showCommandList();
                System.out.println();
                System.out.println(
                          "exit                | back to previous menu" +
                        "\nexit -a             | exit from the program");
                System.out.println(color.get("Reset"));
                continue;
            }
            String error = run(command);
            System.out.println(color.get("Red") + error + color.get("Reset"));

        }
    }
    protected abstract String run(String command);
    protected abstract void showCommandList();

}
