package Game.Cards;

import Game.Gamestructure.Board;
import Game.Gamestructure.Player;
import Game.Gamestructure.UserInterface;

public abstract class Card {
    public Rarity getRarity() {
        return rarity;
    }

    public void setRarity(Rarity rarity) {
        this.rarity = rarity;
    }

    public int getManaCost() {
        return manaCost;
    }

    public void setManaCost(int manaCost) {
        this.manaCost = manaCost;
    }

    public int getCoinCost() {
        return coinCost;
    }

    public void setCoinCost(int coinCost) {
        this.coinCost = coinCost;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }



    private Rarity rarity;
    private int manaCost;
    private int coinCost;
    private String Description;

    public Card(){}


}
