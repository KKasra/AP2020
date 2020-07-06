package Game.GameStructure.CardModels.models;

import Game.CommandAndResponse.*;
import Game.GameStructure.CardModels.MinionModel;
import Game.GameStructure.CardModels.SpellModel;
import Game.GameStructure.Cards.Card;
import Game.GameStructure.Cards.MinionCard;
import Game.GameStructure.Player;

public class markofthewild extends SpellModel {
    public markofthewild(GameProcessor processor, Player player, Card card) {
        super(processor, player, card);
    }

    @Override
    public void play(PlayCommand command) throws Exception{
        super.play(command);


        MinionCard target = (MinionCard)processor.getTargetReceiver().receiveTarget(o -> o instanceof MinionCard);

        ((MinionModel)(target).getCardModel()).setTaunt(true);
        target.setHealth(target.getHealth() + 2);
         target.setDamge(target.getDamage() + 2);

    }
}
