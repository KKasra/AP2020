package Game.GameStructure.Cards;

import DB.components.cards.MinionType;
import Game.GameStructure.Attackable;
import Game.GameStructure.Attacker;
import Game.GameStructure.CardModels.MinionModel;
import Game.GameStructure.Game;
import Game.GameStructure.Player;

import java.util.List;


public class MinionCard extends Card implements Attackable, Attacker {

    private int attack;
    private int health;
    private MinionType minionType;

    public MinionCard(DB.components.cards.MinionCard cardData, Player player) {
        super(cardData, player);
        this.attack = cardData.getAttack();
        this.health = cardData.getHealth();
        this.minionType = cardData.getMinionType();
    }

    @Override
    public void receiveDamage(int amount) {
        ((MinionModel)getCardModel()).receiveDamage(amount);
    }

    @Override
    public void die() {
        getCardModel().die();
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
    public void attack(Attackable target) {
        getModel().attack(target);
    }


    public int getAttack() {
        return attack;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public MinionType getMinionType() {
        return minionType;
    }

    private MinionModel getModel() {
        return (MinionModel) getCardModel();
    }



}
