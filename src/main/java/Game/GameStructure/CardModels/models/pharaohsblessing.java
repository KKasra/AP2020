package Game.GameStructure.CardModels.models;

import Game.CommandAndResponse.GameProcessor;
import Game.CommandAndResponse.PlayCommand;
import Game.GameStructure.CardModels.MinionModel;
import Game.GameStructure.CardModels.SpellModel;
import Game.GameStructure.Cards.Card;
import Game.GameStructure.Cards.MinionCard;
import Game.GameStructure.Player;

public class pharaohsblessing extends SpellModel {
    public pharaohsblessing(GameProcessor processor, Player player, Card card) {
        super(processor, player, card);
    }

    @Override
    public void play(PlayCommand command) throws Exception {
        super.play(command);

        MinionCard target = (MinionCard)processor.getTargetReceiver().receiveTarget(o -> o instanceof MinionCard);

        target.setHealth(target.getHealth() + 4);
        target.setDamge(target.getDamage() + 4);
        ((MinionModel)target.getCardModel()).setTaunt(true);
        ((MinionModel)target.getCardModel()).setDivineShield(true);
    }
}
