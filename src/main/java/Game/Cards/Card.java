package Game.Cards;

import Game.Gamestructure.Board;
import Game.Gamestructure.Player;
import Game.Gamestructure.UserInterface;

public abstract class Card {
    private Rarity rarity;
    private int manaCost;

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

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public UserInterface getUserInterface() {
        return userInterface;
    }

    public void setUserInterface(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    private int coinCost;
    private String Description;
    protected Board board;
    protected UserInterface userInterface;
    private Player player;
    public Card(Player player) {
        this.player = player;
        this.board = player.getBoard();
        this.userInterface = board.getUserInterface();
    }

    public abstract void play();

}
