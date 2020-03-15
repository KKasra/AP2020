package CLI;

import java.util.Scanner;

public class myScanner{
    private myScanner() {}
    private Scanner scanner = new Scanner(System.in);
    private static myScanner reference = null;
    static myScanner getMyScanner() {
        if (reference == null)
            reference = new myScanner();
        return reference;
    }

    String read() {
        return scanner.nextLine();
    }

}
