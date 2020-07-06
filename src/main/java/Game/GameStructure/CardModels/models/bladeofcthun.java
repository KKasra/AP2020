package Game.GameStructure.CardModels.models;

import Game.CommandAndResponse.*;
import Game.GameStructure.CardModels.MinionModel;
import Game.GameStructure.Cards.Card;
import Game.GameStructure.Cards.MinionCard;
import Game.GameStructure.Player;

public class bladeofcthun extends MinionModel {
    public bladeofcthun(GameProcessor processor, Player player, Card card) {
        super(processor, player, card);
    }

    @Override
    public void play(PlayCommand command) throws Exception {
        super.play(command);

        MinionCard target = (MinionCard) processor.getTargetReceiver().receiveTarget(o -> {
           if (o instanceof MinionCard)
               return true;
           return false;
        });
        target.die();
        ((MinionCard)card).setDamge(((MinionCard) card).getDamage() + target.getAttack());
        ((MinionCard)card).setHealth(((MinionCard) card).getHealth() + target.getHealth());

    }
}
