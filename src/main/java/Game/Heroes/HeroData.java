package Game.Heroes;

import Game.Cards.CardFactory;
import Game.Cards.Deck;
import Game.Cards.WeaponCard;
import Game.Gamestructure.Hero;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Set;

@Entity
public class HeroData {
    public Deck getDeck() {
        return deck;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }



    public String getHeroName() {
        return HeroName;
    }

    public void setHeroName(String heroName) {
        HeroName = heroName;
    }

    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    @Override
    public String toString() {
        return getHeroName();
    }


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "HERO_ID")
    int id;

    @Column(name =  "NAME_OF_HERO")
    private String HeroName = this.getClass().getSimpleName();

    @Column(name = "HEROS_POWER")
    private String power;

    @Column(name = "HP")
    private int hp;

    @Embedded
    private  Deck deck = new Deck();
}
