package Game.Cards;

import Game.Gamestructure.*;

public abstract class Card {
    public Hero getHero() {
        return hero;
    }

    public void setHero(Hero hero) {
        this.hero = hero;
    }

    public void setBoard(Board board) {
        this.board = board;
    }



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

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
        this.board = player.getBoard();
    }

    public Board getBoard() {
        return board;
    }

    public String getCardClass() {
        return Class;
    }

    public void setClass(String aClass) {
        this.Class = aClass;
    }

    public String getName(){return name;}

    private Rarity rarity;
    private int manaCost;
    private int coinCost;
    private String Description;
    private Player player;
    private Hero hero;
    private Board board;
    private String Class;
    private String name;


    public Card(Hero hero,String name, String description, Rarity rarity, int manaCost){
        this.name = name;
        setDescription(description);
        setHero(hero);
        setPlayer(hero.getPlayer());
        setBoard(getPlayer().getBoard());
        setManaCost(manaCost);

    }


    public void play() throws Exception{
        getPlayer().getMyHero().getHand().add(this);
    }



}
