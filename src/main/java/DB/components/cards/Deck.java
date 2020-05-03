package DB.components.cards;

import DB.components.heroes.Hero;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Entity
public class Deck {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column(name = "NAME")
    private String name;

    @ManyToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinTable(name = "Deck_Card")
    private List<Card> cards;

    @Embedded
    @Column(name = "INFO", nullable = true)
    private DeckInfo info;

    @OneToOne
    private Hero hero;

    public int countCardInDeck(Card card){
        int res = 0;
        for (Card card1 : cards) {
            if (card1.getName().equals(card.getName()))
                ++res;
        }
        return res;
    }

    public void addCard(Card card) throws Exception{
        if (cards.size() >= 15)
            throw new Exception("deck already full");



        if (countCardInDeck(card) >= 2)
            throw new Exception("this deck already has two cards of this type");

        if (card.getHero() == null || card.getHero().getHeroName().equals(hero.getHeroName()))
            cards.add(card);
        else
            throw new Exception("Card's class does not match");
    }

    public void removeCard(Card card) {
        cards.remove(card);
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setHero(Hero hero) throws Exception{
        for (Card card : cards) {
            if (card.getHero() != null && !card.getHero().getHeroName().equals(hero.getHeroName()))
                throw new Exception("deck contains a card of class" + card.getHero().getHeroName());
        }

        this.hero = hero;
    }

    public Hero getHero() {
        return this.hero;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Deck(String name, Hero hero) {

        cards = new ArrayList<Card>();

        setName(name);
        this.hero = hero;
    }

    public DeckInfo getInfo() {
        if (info == null)
            info = new DeckInfo();
        return info;

    }

    @Override
    public String toString() {
        return "Deck{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", cards=" + cards +
                ", info=" + info +
                ", hero=" + hero +
                '}';
    }

    private Deck(){}
}
