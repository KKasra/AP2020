package Game.Cards;

import java.util.ArrayList;

public class Deck {
    public int countCard(Card card) {
        //TODO
        return 0;
    }
    public ArrayList<Card> getCards() {
        return cards;
    }

    public void setCards(ArrayList<Card> cards) {
        this.cards = cards;
    }
    public void addCard(Card card) throws Exception{
        if (countCard(card) >= 2)
            throw new Exception("this Deck already has two cards of this type");
        cards.add(card);
    }
    public void removeCard(String card) {
        for (Card i : cards)
            if (i.toString().equals(card.toString())) {
                cards.remove(i);
                break;
            }
    }

    private ArrayList<Card> cards = new ArrayList<Card>();
}
