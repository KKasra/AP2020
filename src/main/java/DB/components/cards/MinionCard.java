package DB.components.cards;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class MinionCard extends Card{
    @Column(name = "ATTACK")
    private int Attack;
    @Column(name = "HEALTH")
    private int Health;

    @Column(name = "MINION_TYPE")
    private MinionType minionType;

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

    public MinionType getMinionType() {
        return minionType;
    }

    public void setMinionType(MinionType minionType) {
        this.minionType = minionType;
    }
}
