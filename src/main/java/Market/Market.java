package Market;

import Game.Cards.Card;

import java.util.Set;

public  class Market {
    public static Set<String> getCardsForSale() {
        return cardsForSale;
    }

    public static void setCardsForSale(Set<String> cardsForSale) {
        Market.cardsForSale = cardsForSale;
    }

    private static Set<String> cardsForSale;

}
