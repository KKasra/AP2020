package Game.Cards;

import java.util.ArrayList;
import java.util.Collection;

public class Deck {
    public int countCard(String card) {
        //TODO
        return 0;
    }
    public ArrayList<String> getCards() {
        return cards;
    }

    public void setCards(ArrayList<String> cards) {
        this.cards = cards;
    }
    public void addCard(String card) throws Exception{
        if (countCard(card) >= 2)
            throw new Exception("this Deck already has two cards of this type");
        cards.add(card.toString());
    }
    public void removeCard(String card) {
        for (String i : cards)
            if (i.toString().equals(card.toString())) {
                cards.remove(i);
                break;
            }
    }

    private Collection<String> cardsInDB = new ArrayList<String>();
    private ArrayList<String> cards = (ArrayList<String>)cardsInDB;
}
