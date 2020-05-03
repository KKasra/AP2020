package DB.components.cards;


import DB.components.heroes.Hero;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "RARITY")
    private Rarity rarity;

    @Column(name = "MANA_COST")
    private int manaCost;

    @OneToOne
    private Hero hero;

    @Column(name = "COIN_COST")
    private int coinCost;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Hero getHero() {
        return hero;
    }

    public void setHero(Hero hero) {
        this.hero = hero;
    }
    //TODO save the play method as a field


    @Override
    public String toString() {
        return  this.getClass().getSimpleName() + "{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", rarity=" + rarity +
                ", manaCost=" + manaCost +
                ", hero=" + hero +
                ", coinCost=" + coinCost +
                '}';
    }
}
