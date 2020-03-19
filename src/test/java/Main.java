import java.util.Random;

public class Main {
    public static void main(String[] args) {
        for (int i = 0; i < 21; ++i)
            System.out.println(Math.abs(new Random().nextInt()) % 11);

    }
}
