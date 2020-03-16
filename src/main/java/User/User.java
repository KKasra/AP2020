package User;

import Game.Cards.Card;
import Game.Heros.Hero;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int userID;
    @Column(name = "USERNAME")
    private String userName;
    @Column(name = "PASSWORD")
    private String passWord;
    @Column(name = "COINS")
    private int coins;

    @ManyToMany
    private Collection<Hero> heroes;

    @Column(name = "MY_HERO")
    private int indexOfHero;
    @Column(name = "AVAILABLE_CARDS")
    private ArrayList<String> availableCards;

    public User(String userName, String passWord) {
        this.userName = userName;
        this.passWord = passWord;
        coins = 50;
        createDefaultAvailableCardSet();
        createDefaultHeroSet();
    }

    void createDefaultHeroSet() {
        //TODO
        heroes = new ArrayList<>();
    }

    void createDefaultAvailableCardSet() {
        //TODO
    }

    public int getIndexOfHero() {
        return indexOfHero;
    }

    public void setIndexOfHero(int indexOfHero) {
        this.indexOfHero = indexOfHero;
    }

    void addHero(Hero newHero) {
        heroes.add(newHero);
    }

    void removeDeck(int index) {
        heroes.remove(index);
    }

    public ArrayList<Hero> getHeroes() {
        return (ArrayList<Hero>) heroes;
    }

    public ArrayList<String> getAvailableCards() {
        return availableCards;
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

    public void setAvailableCards(ArrayList<String> availableCards) {
        this.availableCards = availableCards;
    }
}
