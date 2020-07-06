package Game.GameStructure.CardModels.models;

import Game.CommandAndResponse.GameProcessor;
import Game.CommandAndResponse.PlayCommand;
import Game.GameStructure.Attacker;
import Game.GameStructure.CardModels.MinionModel;
import Game.GameStructure.Cards.Card;
import Game.GameStructure.CardModels.HeroPower;
import Game.GameStructure.Player;

public class arcaneamplifier extends MinionModel {
    public arcaneamplifier(GameProcessor processor, Player player, Card card) {
        super(processor, player, card);
    }

    @Override
    public void play(PlayCommand command) throws Exception {
        super.play(command);
        HeroPower power = player.getHero().getPower();
        if (power instanceof Attacker)
            ((Attacker) power).setDamge(((Attacker) power).getDamage() + 2);
    }

    @Override
    public void die() {
        super.die();

        HeroPower power = player.getHero().getPower();
        if (power instanceof Attacker)
            ((Attacker) power).setDamge(((Attacker) power).getDamage() - 2);
    }
}
