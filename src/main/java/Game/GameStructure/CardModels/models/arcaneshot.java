package Game.GameStructure.CardModels.models;

import Game.CommandAndResponse.*;
import Game.GameStructure.Attackable;
import Game.GameStructure.CardModels.SpellModel;
import Game.GameStructure.Cards.Card;
import Game.GameStructure.Cards.MinionCard;
import Game.GameStructure.Player;

public class arcaneshot extends SpellModel {
    @Override
    public void play(PlayCommand command) throws Exception{
        super.play(command);

        Attackable target = (Attackable)processor.getTargetReceiver().receiveTarget(object -> {
           if (object instanceof Attackable)
               return true;
           return false;
        });

        target.receiveDamage(2);
    }

    public arcaneshot(GameProcessor processor, Player player, Card card) {
        super(processor, player, card);
    }
}
