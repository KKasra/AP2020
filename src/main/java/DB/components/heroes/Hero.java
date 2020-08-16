package DB.components.heroes;



import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Hero implements Serializable {
    public String getHeroName() {
        return HeroName;
    }

    public void setHeroName(String heroName) {
        HeroName = heroName;
    }

    public HeroPower getPower() {
        return power;
    }

    public void setPower(HeroPower power) {
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

    @Embedded
    @Column(name = "HEROES_POWER")
    private HeroPower power;

    @Column(name = "HP")
    private int hp;


}
