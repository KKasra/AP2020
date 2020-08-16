package Game.GameStructure.CardModels.models;

import Game.CommandAndResponse.GameProcessor;
import Game.GameStructure.Attackable;
import Game.GameStructure.Attacker;
import Game.GameStructure.CardModels.HeroPower;
import Game.GameStructure.Cards.Card;
import Game.GameStructure.Player;

public class magepowermodel extends HeroPower implements Attacker {
    private int damage = 2;
    public magepowermodel(GameProcessor processor, Player player, Card card) {
        super(processor, player, card);
        setManaCost(2);
    }

    @Override
    public void usePower() {
        Attackable target = (Attackable) processor.getTargetReceiver().receiveTarget(o -> o instanceof Attackable);
        target.receiveDamage(1);
    }

    @Override
    public void setDamge(int damage) {
        this.damage = damage;
    }

    @Override
    public int getDamage() {
        return damage;
    }

    @Override
    public void attack(Attackable attackable) {
        attackable.receiveDamage(damage);
    }

    @Override
    public Player getPlayer() {
        return player;
    }
}
