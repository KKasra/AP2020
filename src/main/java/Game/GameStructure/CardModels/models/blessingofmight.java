package Game.GameStructure.CardModels.models;

import Game.CommandAndResponse.GameProcessor;
import Game.CommandAndResponse.PlayCommand;
import Game.GameStructure.CardModels.MinionModel;
import Game.GameStructure.Cards.Card;
import Game.GameStructure.Cards.MinionCard;
import Game.GameStructure.Player;

public class blessingofmight extends MinionModel {
    public blessingofmight(GameProcessor processor, Player player, Card card) {
        super(processor, player, card);
    }

    @Override
    public void play(PlayCommand command) throws Exception {
        super.play(command);
        MinionCard target = (MinionCard)processor.getTargetReceiver().receiveTarget(o -> o instanceof MinionCard);
        target.setDamge(target.getDamage() + 3);
    }
}
