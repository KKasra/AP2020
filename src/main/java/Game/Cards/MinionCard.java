package Game.Cards;

import Game.Gamestructure.Attackable;
import Game.Gamestructure.Hero;

public class MinionCard extends Card implements Attackable {
    private int attack;
    private int armor;
    boolean Taunt;
    boolean Charge;
    public MinionCard(Hero hero, String name, String description, MinionType minionType, Rarity rarity, int attack, int armor, int manacost) {
        super(hero, name, description, rarity, manacost);
        this.setAttack(attack);
        this.setArmor(armor);
    }

    void attack() throws Exception{
        Object target = getPlayer().getUserInterface().read();
        if (target instanceof Attackable) {
            ((Attackable) target).receiveDamage(attack);
            if (target instanceof MinionCard)
                receiveDamage(((MinionCard)target).getAttack());
        }
        else
            throw new Exception("not that one!");
    }


    @Override
    public void receiveDamage(int damage) {
         setArmor(getArmor() - damage);
    }

    public boolean isCharge() {
        return Charge;
    }

    public void setCharge(boolean charge) {
        Charge = charge;
    }

    public boolean isTaunt() {
        return Taunt;
    }

    public void setTaunt(boolean taunt) {
        Taunt = taunt;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getArmor() {
        return armor;
    }

    public void setArmor(int armor) {
        this.armor = armor;
    }
}
