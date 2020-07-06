package Game.GameStructure.CardModels.models;

import Game.CommandAndResponse.*;
import Game.GameStructure.CardModels.SpellModel;
import Game.GameStructure.Cards.Card;
import Game.GameStructure.Cards.MinionCard;
import Game.GameStructure.Player;

public class huntersmark extends SpellModel {
    public huntersmark(GameProcessor processor, Player player, Card card) {
        super(processor, player, card);
    }

    @Override
    public void play(PlayCommand command) throws Exception{
        super.play(command);
        MinionCard target = (MinionCard) processor.getTargetReceiver().receiveTarget(o -> o instanceof MinionCard);

        target.setHealth(1);
    }
}
