package Game.GameStructure.CardModels.models;

import Game.CommandAndResponse.GameProcessor;
import Game.CommandAndResponse.PlayCommand;
import Game.GameStructure.CardModels.MinionModel;
import Game.GameStructure.CardModels.SpellModel;
import Game.GameStructure.Cards.Card;
import Game.GameStructure.Cards.MinionCard;
import Game.GameStructure.Player;

import java.util.Map;

public class gnomisharmyknife extends SpellModel {
    @Override
    public void play(PlayCommand command) throws Exception{
        super.play(command);
        MinionCard target = (MinionCard) processor.getTargetReceiver().receiveTarget(o -> o instanceof MinionCard);
        ((MinionModel)target.getCardModel()).setTaunt(true);
        ((MinionModel)target.getCardModel()).setCharge(true);

    }

    public gnomisharmyknife(GameProcessor processor, Player player, Card card) {
        super(processor, player, card);
    }
}
