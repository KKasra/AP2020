package Game.GameStructure.CardModels;

import Game.CommandAndResponse.GameProcessor;
import Game.GameStructure.Cards.Card;
import Game.GameStructure.Player;

import java.lang.reflect.Constructor;

public class Loader {
    private ClassLoader applicationLoader;

    private Loader() {
        this.applicationLoader = this.getClass().getClassLoader();
    }

    public Constructor getModel(Card card) throws ClassNotFoundException, NoSuchMethodException {
        String name = card.getName();
        name = name.toLowerCase();
        name = name.replace(" ", "");
        name = name.replace("\'", "");
        Class cardModelClass = applicationLoader.loadClass("Game.GameStructure.CardModels.models." + name);
        return cardModelClass.getConstructor(GameProcessor.class, Player.class, Card.class);
    }

    private static Loader instance;
    public static Loader getInstance() {
        if (instance == null)
            instance = new Loader();
        return instance;
    }
}
