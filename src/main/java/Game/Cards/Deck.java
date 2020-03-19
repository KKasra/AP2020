package Game.Cards;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.ArrayList;
import java.util.Collection;

@Embeddable
public class Deck {
    public int countCard(String card) {
        int cnt = 0;
        for (String i : cards)
            if (i.equals(card))
                ++cnt;
        return cnt;
    }
    public ArrayList<String> getCards() {
        return cards;
    }

    public void setCards(ArrayList<String> cards) {
        this.cards = cards;
    }
    public void addCard(String card) throws Exception{
        if(cards.size() >= 15)
            throw new Exception("your deck in full");
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


    @Column(name = "CARDS", length = 1000)
    private ArrayList<String> cards = new ArrayList<String>();
}
