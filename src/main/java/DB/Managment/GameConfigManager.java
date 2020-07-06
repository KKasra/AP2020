package DB.Managment;

import DB.components.cards.Deck;
import com.google.gson.Gson;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class GameConfigManager {
    private static final String PATH = "./Data/";
    private final Gson gson;

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        ArrayList<String> player = new ArrayList<>();
        ArrayList<String> enemy = new ArrayList<>();

        String name = scanner.nextLine();

        System.out.println("write Card names in order:");

        System.out.println("player:");
        int index = 1;
        while(true) {
            System.out.print((index++) + ":");
            String s = scanner.nextLine();
            if (s.equals(""))
                break;
            player.add(s);
        }

        System.out.println("enemy:");
        index = 1;
        while(true) {
            System.out.print((index++) + ":");
            String s = scanner.nextLine();
            if (s.equals(""))
                break;
            enemy.add(s);
        }

        Config res = new Config();
        res.playerCards = player;
        res.enemyCards = enemy;

        System.out.println("adding config Y/N :");
        System.out.println(res);
        FileWriter fileWriter = new FileWriter(PATH + name + ".json");
        if (scanner.nextLine().toLowerCase().equals("y")) {
            getInstance().gson.toJson(res, fileWriter);
            fileWriter.flush();
            fileWriter.close();
        }

    }

    public Deck[] load(String configName) throws Exception {
       Config config =  gson.fromJson(new FileReader(PATH + configName + ".json"), Config.class);
       Deck[] res = new Deck[2];
       res[0] = new Deck("player", HeroManager.getInstance().getHero("Mage"));
       res[1] = new Deck("enemy", HeroManager.getInstance().getHero("Mage"));
        for (String playerCard : config.playerCards) {
            res[0].getCards().add(CardManager.getInstance().getCard(playerCard));
        }
        for (String enemyCard : config.enemyCards) {
            res[1].getCards().add(CardManager.getInstance().getCard(enemyCard));
        }
        return res;
    }

    public static class Config{
        private List<String> playerCards;
        private List<String> enemyCards;

        @Override
        public String toString() {
            return "Config{" +
                    "playerCards=" + playerCards +
                    ", enemyCards=" + enemyCards +
                    '}';
        }
    }

    private GameConfigManager() {
        gson = new Gson();
    }

    private static GameConfigManager instance;
    public static GameConfigManager getInstance() {
        if (instance == null)
            instance = new GameConfigManager();
        return instance;
    }
}
