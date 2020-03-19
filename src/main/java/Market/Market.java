package Market;

import Game.Cards.Card;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public  class Market {

    private static Set<String> cardsForSale;
    private static Map<String, Integer> cardCost;
    private static Map<String, String> cardClass;

    static {
        cardsForSale = new TreeSet<>();
        cardCost = new TreeMap<String, Integer>();
        cardClass = new TreeMap<String, String>();
        addCard("Goldshire Footman", 21, "Neutral");
        addCard("Murloc Raider", 21, "Neutral");
        addCard("Stonetusk Boar", 23, "Neutral");
        addCard("Target dummy", 24, "Neutral");
        addCard("FrostWolf Grunt", 26, "Neutral");
        addCard("shieldbearer", 25, "Neutral");
        addCard("Doomsayer", 24, "Neutral");
        addCard("Manic Soulcaster", 28, "Mage");
        addCard("Arcane Amplifier", 33, "Mage");
        addCard("Blade of C'Thun", 28, "Rogue");
        addCard("Lab recruiter", 30, "Rogue");
        addCard("Voidwalker", 20, "Warlock");
        addCard("Aranasi Broodmother", 25, "Warlock");
        addCard("Chaos Nova", 22, "Neutral");
        addCard("Starfire", 12, "Neutral");
        addCard("Gift of the Wild", 35, "Neutral");
        addCard("Hunter's mark", 36,"Neutral");
        addCard( "Arcane Explosion", 40, "Neutral");
        addCard("Mark of the wild", 33, "Neutral");
        addCard("Healing Touch", 44, "Neutral");
        addCard("Assassin's Blade", 22, "Rogue");

    }

   static void addCard(String name, int cost, String Class) {
        cardsForSale.add(name);
        cardClass.put(name, Class);
        cardCost.put(name, cost);
   }
    public static Map<String, Integer> getCardCost() {
        return cardCost;
    }


    public static void setCardCost(Map<String, Integer> cardCost) {
        Market.cardCost = cardCost;
    }
    public static Set<String> getCardsForSale() {
        return cardsForSale;
    }

    public static void setCardsForSale(Set<String> cardsForSale) {
        Market.cardsForSale = cardsForSale;
    }

    public static Map<String, String> getCardClass() {
        return cardClass;
    }
}
