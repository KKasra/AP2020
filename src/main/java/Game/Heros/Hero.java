package Game.Heros;

import Game.Cards.Card;
import Game.Cards.Deck;
import Game.Cards.WeaponCard;

import java.util.Set;

public class Hero {
    private String HeroName;
    private Set<String> specialCards;

    public Set<String> getSpecialCards() {
        return specialCards;
    }

    public void setSpecialCards(Set<String> specialCards) {
        this.specialCards = specialCards;
    }

    public Deck getDeck() {
        return deck;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    private  Deck deck;

    @Override
    public String toString() {
        return HeroName;
    }


    private WeaponCard weapon;
    private HeroPower power;
    void usePower() {
        power.usePower();
    }

}
