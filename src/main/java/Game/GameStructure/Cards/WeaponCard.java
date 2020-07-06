package Game.GameStructure.Cards;

import Game.GameStructure.Attackable;
import Game.GameStructure.Attacker;
import Game.GameStructure.CardModels.WeaponModel;
import Game.GameStructure.Player;

public class WeaponCard extends Card implements Attacker {

    private int attack;
    private int durability;

    public WeaponCard(DB.components.cards.WeaponCard cardData, Player player) {
        super(cardData, player);
        this.attack = cardData.getAttack();
        this.durability = cardData.getHealth();
    }

    public int getAttack() {
        return attack;
    }

    public int getDurability() {
        return durability;
    }

    @Override
    public void setDamge(int damage) {
        attack = damage;
    }

    @Override
    public int getDamage() {
        return getAttack();
    }

    @Override
    public void attack(Attackable attackable) {
        getModel().attack(attackable);
    }

    public WeaponModel getModel() {
        return (WeaponModel)getCardModel();
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public void setDurability(int durability) {
        this.durability = durability;
    }
}
