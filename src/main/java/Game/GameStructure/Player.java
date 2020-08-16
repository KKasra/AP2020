package Game.GameStructure;

import DB.components.cards.Deck;
import DB.components.cards.Passive;
import Game.CommandAndResponse.GameProcessor;
import Game.CommandAndResponse.PlayCommand;
import Game.GameStructure.Cards.Card;
import Game.GameStructure.Cards.CardFactory;
import Game.GameStructure.CardModels.HeroPower;
import Game.GameStructure.Cards.MinionCard;
import Game.GameStructure.Heroes.Hero;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Player implements Serializable {
    transient private Game game;
    private List<Card> deck;
    private List<Card> hand;
    private List<Card> cardsOnBoard;
    private Hero hero;
    private int mana;
    transient private Passive passive;
    private String name;

    public Player(String playerName, Game game, Deck deck, GameProcessor processor) {
        this.name = playerName;
        this.game = game;

        this.deck = new ArrayList<>();
        for (DB.components.cards.Card c : deck.getCards()) {
            this.deck.add(CardFactory.produce(processor, c, this));
        }

        hand = new ArrayList<>();

        cardsOnBoard = new ArrayList<>();
        for (int i = 0; i < 7; ++i)
            cardsOnBoard.add(null);

        hero = new Hero(this, deck.getHero(), processor);
    }

    public void playCard(PlayCommand command) throws Exception{
        if (command.getIndexInHand() >= hand.size())
            throw new Exception("index in hand is greater than hands size");
        Card card = hand.get(command.getIndexInHand());
        int index = command.getIndexOnBoard();
        if (mana < card.getManaCost())
            throw new Exception("insufficient mana");
        if (card instanceof MinionCard && cardsOnBoard.get(index) != null)
            throw new Exception("occupied place");
        hand.remove(card);
        card.getCardModel().play(command);
        command.setCard(card);
        mana -= card.getManaCost();
        game.logs.add(card.getName() + " by " + name);

    }

    public void drawFromDeck() throws Exception {
        if (deck.isEmpty())
            throw new Exception("empty deck");
        hand.add(deck.remove(0));
    }


    public List<Card> getDeck() {
        return deck;
    }

    public void setDeck(List<Card> deck) {
        this.deck = deck;
    }

    public List<Card> getHand() {
        return hand;
    }

    public List<Card> getCardsOnBoard() {
        return cardsOnBoard;
    }

    public Hero getHero() {
        return hero;
    }

    public void setHero(Hero hero) {
        this.hero = hero;
    }

    public int getMana() {
        return mana;
    }

    public void setMana(int mana) {
        this.mana = mana;
    }

    public Passive getPassive() {
        return passive;
    }

    public void setPassive(Passive passive) {
        this.passive = passive;
    }

    public String getName() {
        return name;
    }

    public void usePower(HeroPower power) throws Exception{
        if (power.getManaCost() > getMana()) {
            throw new Exception("insufficient mana");
        }
        power.usePower();
        mana -= power.getManaCost();
    }

    private Player(){}

    public Player getRestrictedInfo() {
        Player res = new Player();

        res.hero = getHero();

        res.hand = new ArrayList<>();
        for (int i = 0; i < getHand().size(); ++i)
            res.hand.add(null);

        res.deck = new ArrayList<>();
        for (int i = 0; i < getDeck().size(); ++i)
            res.deck.add(null);

        res.cardsOnBoard = getCardsOnBoard();
        return res;
    }
}
