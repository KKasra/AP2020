package Network.Requests;

public class CollectionRequest extends Request{
    public CollectionRequest(String key, Message message) {
        super(key);
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }

    public enum Message {
        addToDeck, removeFromDeck, createDeck, changeHero, changeName, chooseDeck, removeDeck
    }


    private Message message;
    private String deckName;
    private String CardName;
    private String heroName;
    private String newName;

    public String getDeckName() {
        return deckName;
    }

    public void setDeckName(String deckName) {
        this.deckName = deckName;
    }

    public String getCardName() {
        return CardName;
    }

    public void setCardName(String cardName) {
        CardName = cardName;
    }

    public String getHeroName() {
        return heroName;
    }

    public void setHeroName(String heroName) {
        this.heroName = heroName;
    }

    public String getNewName() {
        return newName;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }
}
