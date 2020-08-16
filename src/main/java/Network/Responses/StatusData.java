package Network.Responses;

import DB.components.User;
import DB.components.cards.Deck;

import java.util.List;

public class StatusData extends Response{
    private List<Deck> decks;
    private List<User> users;

    public StatusData(List<Deck> decks, List<User> users) {
        this.decks = decks;
        this.users = users;
    }

    public List<Deck> getDecks() {
        return decks;
    }

    public List<User> getUsers() {
        return users;
    }
}
