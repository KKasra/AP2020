package Network.Responses;

import DB.components.cards.Card;
import DB.components.cards.Deck;
import DB.components.heroes.Hero;

import java.util.List;

public class CollectionData extends Response{
    private List<Deck> decks;
    private int chosenDeck;
    private List<Hero> heroes;
    private List<Card> cards;

    public CollectionData(List<Deck> decks,List<Card> cards, List<Hero> heroes, int chosenDeck) {
        this.heroes = heroes;
        this.decks = decks;
        this.chosenDeck = chosenDeck;
        this.cards = cards;
    }

    public List<Deck> getDecks() {
        return decks;
    }

    public int getChosenDeck() {
        return chosenDeck;
    }

    public List<Hero> getHeroes() {
        return heroes;
    }

    public List<Card> getCards() {
        return cards;
    }
}
