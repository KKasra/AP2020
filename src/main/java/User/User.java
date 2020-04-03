package User;

import User.Heroes.HeroData;
import User.Heroes.HeroDataFactory;

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
    private String passWord;
    @Column(name = "COINS")
    private int coins;

    @ManyToMany(fetch = FetchType.EAGER)
    @Column(name ="HEROES")
    private List<HeroData> heroes;

    private User() {

    }
    public int getIndexOfHero() {
        return indexOfHero;
    }

    public void setIndexOfHero(int indexOfHero) {
        this.indexOfHero = indexOfHero;

    }

    @Column(name = "MY_HERO")
    private int indexOfHero;
    @Column(name = "AVAILABLE_CARDS")
    private ArrayList<String> availableCards = new ArrayList<>();

    @Transient
    private Log log;
    void initLog(boolean append){
        log = new Log("./Data/Logs/"
                + getUserName()
                + "-" + getUserID()
                + ".log", append);
    }
    public Log getLog() {
        return log;
    }

    public User(String userName, String passWord) {
        this.userName = userName;
        this.passWord = passWord;
        coins = 50;
        createDefaultAvailableCardSet();
        createDefaultHeroSet();
    }

    void createDefaultHeroSet() {
        heroes = new ArrayList<>();

        HeroData hero = HeroDataFactory.build("Mage");
        hero.getDeck().getCards().add("Goldshire Footman");
        hero.getDeck().getCards().add("Murloc Raider");
        hero.getDeck().getCards().add("Stonetusk Boar");
        hero.getDeck().getCards().add("Starfire");
        hero.getDeck().getCards().add("Gift of the Wild");
        hero.getDeck().getCards().add("Arcane Explosion");
        hero.getDeck().getCards().add("Hunter's mark");
        hero.getDeck().getCards().add("Healing Touch");
        hero.getDeck().getCards().add("Manic Soulcaster");
        hero.getDeck().getCards().add("Arcane Amplifier");


        heroes.add(hero);
    }

    void createDefaultAvailableCardSet() {
        getAvailableCards().add("Goldshire Footman");
        getAvailableCards().add("Murloc Raider");
        getAvailableCards().add("Stonetusk Boar");
        getAvailableCards().add("Starfire");
        getAvailableCards().add("Gift of the Wild");
        getAvailableCards().add("Arcane Explosion");
        getAvailableCards().add("Hunter's mark");
        getAvailableCards().add("Healing Touch");
        getAvailableCards().add("Manic Soulcaster");
        getAvailableCards().add("Arcane Amplifier");
    }



    void addHero(HeroData newHeroData) {
        heroes.add(newHeroData);
    }

    void removeHero(int index) {
        heroes.remove(index);
    }

    public List<HeroData> getHeroes() {
        return  heroes;
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

    public void setHeroData(ArrayList<HeroData> heroData) {
        heroData = heroData;
    }

    public void setAvailableCards(ArrayList<String> availableCards) {
        this.availableCards = availableCards;
    }
}
