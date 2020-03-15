package CLI;

import User.User;

public abstract class Menu {
    User user;
    public Menu(User user) {
        this.user = user;
    }
    public void exec() {
        while(true) {
            System.out.print(this.getClass().getSimpleName() + " >> Please Enter Command: ");
            String command = myScanner.getMyScanner().read();
            System.out.println();
            if (command.equals("exit"))
                return;
            if (command.equals("exit -a"))
                System.exit(0);

            String error = run(command);
            System.out.println(error);

        }
    }
    protected abstract String run(String command);

    //TODO "HearthStone --help" Command
}
