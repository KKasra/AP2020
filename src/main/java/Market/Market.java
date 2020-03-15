package Market;

import Game.Cards.Card;

import java.util.Set;

public  class Market {
    public static Set<Card> getCardsForSale() {
        return cardsForSale;
    }

    public static void setCardsForSale(Set<Card> cardsForsale) {
        cardsForSale = cardsForsale;
    }

    private static Set<Card> cardsForSale;

}
