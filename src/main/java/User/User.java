package User;

import Game.Cards.Card;
import Game.Heros.Hero;

import java.util.ArrayList;

public class User {
    private int userID;
    private String userName;
    private String passWord;
    private int coins;
    private ArrayList<Hero> heroes;

    public int getIndexOfHero() {
        return indexOfHero;
    }

    public void setIndexOfHero(int indexOfHero) {
        this.indexOfHero = indexOfHero;
    }

    private int indexOfHero;
    private ArrayList<Card> availableCards;

    public User(String userName, String passWord) {
        this.userName = userName;
        this.passWord = passWord;
        coins = 50;
        createDefaultAvailableCardSet();
        createDefaultHeroSet();
    }

    void createDefaultHeroSet() {
        heroes = new ArrayList<>();
    }

    void createDefaultAvailableCardSet() {

    }

    void addHero(Hero newHero) {
        heroes.add(newHero);
    }

    void removeDeck(int indx) {
        heroes.remove(indx);
    }

    public ArrayList<Hero> getHeroes() {
        return (ArrayList<Hero>) heroes.clone();
    }

    public ArrayList<Card> getAvailableCards() {
        return (ArrayList<Card>)availableCards.clone();
    }


    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public void setHeroes(ArrayList<Hero> heroes) {
        heroes = heroes;
    }

    public void setAvailableCards(ArrayList<Card> availableCards) {
        this.availableCards = availableCards;
    }
}
