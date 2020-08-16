package Game.GameStructure.Cards;

import DB.components.cards.Rarity;
import Game.CommandAndResponse.PlayCommand;
import Game.GameStructure.CardModels.CardModel;
import Game.GameStructure.Game;
import Game.GameStructure.Heroes.Hero;
import Game.GameStructure.Player;

import java.io.Serializable;

public abstract class Card implements Serializable {

    transient private Player player;
    private String name;
    private String description;
    private Rarity rarity;
    private int manaCost;
    private DB.components.heroes.Hero hero;
    private DB.components.cards.Card cardData;
    transient private CardModel cardModel;

    public Card (DB.components.cards.Card cardData, Player player) {
        this.cardData = cardData;
        this.player = player;
        this.name = cardData.getName();
        this.description = cardData.getDescription();
        this.rarity = cardData.getRarity();
        this.manaCost = cardData.getManaCost();
        this.hero = cardData.getHero();

    }

    public void play(PlayCommand command) throws Exception{
        cardModel.play(command);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Rarity getRarity() {
        return rarity;
    }

    public int getManaCost() {
        return manaCost;
    }

    public DB.components.heroes.Hero getHero() {
        return hero;
    }

    public Player getPlayer() {
        return player;
    }

    public DB.components.cards.Card getCardData() {
        return cardData;
    }

    public CardModel getCardModel() {
        return cardModel;
    }

    public void setCardModel(CardModel cardModel) {
        this.cardModel = cardModel;
    }

    public void setManaCost(int manaCost) {
        this.manaCost = manaCost;
    }
}
