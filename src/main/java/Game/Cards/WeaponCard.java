package Game.Cards;

import Game.Gamestructure.Attackable;
import Game.Gamestructure.Hero;

public class WeaponCard extends Card{
    int attack;
    int durability;

    public WeaponCard(Hero hero, String name, Rarity rarity, int manaCost, int attack, int durability, String description) {
        super(hero, name, description, rarity, manaCost);
        this.attack = attack;
        this.durability = durability;
    }


    public void attack() throws Exception{
        Object target = getPlayer().getUserInterface().read();
        if (target instanceof Attackable)
            ((Attackable)target).receiveDamage(attack);
        else
            throw new Exception("not that one!");
    };

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }
    public int getDurability() {
        return durability;
    }

    public void setDurability(int durability) {
        this.durability = durability;
    }
}
