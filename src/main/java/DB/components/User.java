package DB.components;

import DB.Log;
import DB.Managment.DefaultCollection;
import DB.UserDB;
import DB.components.cards.Card;
import DB.components.cards.Deck;
import DB.components.heroes.Hero;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class User {
    @Transient
    public static User user;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int userID;
    @Column(name = "USERNAME")
    private String userName;
    @Column(name = "PASSWORD")
    private int passWordHash;
    @Column(name = "COINS")
    private int coins;

    @ManyToMany(fetch = FetchType.EAGER)
    @Fetch(FetchMode.SELECT)
    @Column(name ="HEROES")
    private List<Hero> heroes;

    @ManyToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinTable(name = "User_Card")
    private List<Card> availableCards;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Fetch(FetchMode.SELECT)
    @Column(name = "Decks")
    private List<Deck> decks;

    @Column(name = "INDEX_OF_DECK")
    private int indexOfDeck;

    @Transient
    private Log log;
    public void initLog(boolean append){
        log = new Log("./Data/Logs/"
                + getUserName()
                + "-" + getUserID()
                + ".log", append);
    }
    public Log getLog() {
        return log;
    }

    public boolean hasCard(Card card) {
        for (Card availableCard : availableCards) {
            if (availableCard.getName().equals(card.getName()))
                return true;

        }
        return false;
    }

    public boolean hasHero(Hero hero) {
        for (Hero hero1 : heroes) {
          if (hero.getHeroName().equals(hero1.getHeroName()))
              return true;
        }
        return false;
    }

    public User(String userName, String passWord) {
        this.userName = userName;
        this.passWordHash = passWord.hashCode();

        User defaultUser = DefaultCollection.getUser();
        decks = new ArrayList<Deck>();
        for (Deck deck : defaultUser.getDecks()) {
            Deck copy = new Deck(deck.getName(), deck.getHero());
            for (Card card : deck.getCards()) {
                try {
                    copy.addCard(card);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            decks.add(copy);
        }

        setCurrentDeck(0);
        availableCards = new ArrayList<Card>();
        for (Card availableCard : defaultUser.getAvailableCards()) {
            availableCards.add(availableCard);
        }
        heroes = new ArrayList<Hero>();
        for (Hero hero : defaultUser.getHeroes()) {
            heroes.add(hero);
        }
        coins = defaultUser.getCoins();

        System.out.println(this);
    }





    void addHero(Hero newHero) {
        heroes.add(newHero);
    }

    void removeHero(int index) {
        heroes.remove(index);
    }

    public List<Hero> getHeroes() {
        return  heroes;
    }

    public List<Card> getAvailableCards() {
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

    public int getPassWord() {
        return passWordHash;
    }

    public void setPassWord(String passWord) {
        this.passWordHash = passWord.hashCode();
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public void setHeroes(ArrayList<Hero> heroes) {
        this.heroes = heroes;
    }

    public void setAvailableCards(ArrayList<Card> availableCards) {
        this.availableCards = availableCards;
    }

    @Override
    public String toString() {
        return "User{" +
                "userID=" + userID +
                ", userName='" + userName + '\'' +
                ", passWordHash=" + passWordHash +
                ", coins=" + coins +
                ", heroes=" + heroes +
                ", availableCards=" + availableCards +
                ", decks=" + decks +
                ", indexOfDeck=" + indexOfDeck +
                ", log=" + log +
                '}';
    }

    public Deck getCurrentDeck() {
        if (indexOfDeck >= decks.size() || indexOfDeck < 0)
            indexOfDeck = 0;

        if (decks.size() == 0)
            return null;
        return decks.get(indexOfDeck);
    }
    public void setCurrentDeck(int indx) {
        if (indx < 0 || indx >= decks.size())
            return;
        indexOfDeck = indx;
    }

    public void setDecks(List<Deck> decks) {
        this.decks = decks;
    }

    public void addDeck(Deck deck) throws Exception{
        if (!hasHero(deck.getHero()))
            throw new Exception("deck's hero is not open");
        for (Deck deck1 : decks) {
            if (deck1.getName().equals(deck.getName()))
                throw new Exception("deck with such name already exists");
        }
        decks.add(deck);
        UserDB.saveChanges(this);
    }
    public void removeDeck(Deck deck) {
        decks.remove(deck);
    }

    public List<Deck> getDecks() {
        return decks;
    }

    public User() {

    }
}
