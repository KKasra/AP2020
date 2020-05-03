package DB.Managment;

import DB.components.User;
import DB.components.cards.Card;
import DB.components.cards.Deck;

import java.util.ArrayList;
import java.util.List;

public class Market{
    private List<Card> cardsForSale;

    public List<Card> getCardsForSale() {
        return cardsForSale;
    }

    public void setCardsForSale(List<Card> cardsForSale) {
        this.cardsForSale = cardsForSale;
    }

    private Market() {
        cardsForSale = CardManager.getInstance().getCards();
    }

    public void buy(Card card) throws Exception{
        if (User.user.hasCard(card))
            throw new Exception("card is already in your collection");
        if (User.user.getCoins() < card.getCoinCost())
            throw new Exception("your coins are insufficient");
        User.user.getAvailableCards().add(card);
        User.user.setCoins(User.user.getCoins() - card.getCoinCost());
    }

    public void sell(Card card) throws Exception{
        if (!User.user.hasCard(card))
            throw new Exception("card is not in your collection");
        for (Deck deck : User.user.getDecks()) {
            if (deck.countCardInDeck(card) > 0)
                throw new Exception("card is contained in your deck(s)");
        }
        User.user.getAvailableCards().remove(card);
        User.user.setCoins(User.user.getCoins() + card.getCoinCost());
    }

    private static Market instance;
    public static Market getInstance() {
        if (instance == null)
            instance = new Market();
        return instance;
    }
}