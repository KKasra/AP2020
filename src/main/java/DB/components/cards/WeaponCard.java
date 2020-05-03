package DB.components.cards;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class WeaponCard extends Card{
    @Column(name = "ATTACK")
    private int Attack;
    @Column(name = "DURABILITY")
    private int Health;

    public int getAttack() {
        return Attack;
    }

    public void setAttack(int attack) {
        Attack = attack;
    }

    public int getHealth() {
        return Health;
    }

    public void setHealth(int health) {
        Health = health;
    }
}
