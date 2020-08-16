package Network.Responses;

import DB.components.cards.Card;

import java.util.List;
import java.util.Map;

public class StoreData extends Response{
    private List<Card> cardList;
    private Map<Card, Boolean> hasCard;
    private int userCoins;

    public StoreData(List<Card> cardList, Map<Card, Boolean> hasCard, int userCoins) {
        this.cardList = cardList;
        this.userCoins = userCoins;
        this.hasCard = hasCard;
    }

    public List<Card> getCardList() {
        return cardList;
    }

    public int getUserCoins() {
        return userCoins;
    }
    public boolean hasCard(Card card) {
        return hasCard.get(card);
    }

}
