package Game.Heroes;

import Game.Cards.Deck;

import java.util.ArrayList;
import java.util.HashSet;

public class HeroDataFactory {
    public static HeroData build(String name) {
        HeroDataBuilder res = new HeroDataBuilder();
        switch (name) {
            case "Mage":
                return res.withName("Mage")
                        .withPower("FireBlast")
                        .withHp(30).build();

            case "Rogue" :
                return res.withName("Rogue")
                        .withPower("Rogue's Power :|")
                        .withHp(30).build();

            case "Warlock" :
                return res.withName("Warlock")
                        .withPower("Life Tap")
                        .withHp(35).build();


        }

        return null;
    }
}
