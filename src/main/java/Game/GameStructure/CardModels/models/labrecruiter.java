package Game.GameStructure.CardModels.models;

import Game.CommandAndResponse.*;
import Game.GameStructure.CardModels.MinionModel;
import Game.GameStructure.Cards.Card;
import Game.GameStructure.Cards.CardFactory;
import Game.GameStructure.Cards.MinionCard;
import Game.GameStructure.Player;

import java.util.Collection;
import java.util.Collections;

public class labrecruiter extends MinionModel {

    public labrecruiter(GameProcessor processor, Player player, Card card) {
        super(processor, player, card);
    }

    @Override
    public void play(PlayCommand command) throws Exception {
        super.play(command);


        MinionCard target = (MinionCard)processor.getTargetReceiver()
                .receiveTarget(o -> (o instanceof MinionCard && ((MinionCard) o).getPlayer() == player));

        for (int i = 0; i < 3; ++i) {
            player.getDeck().add(CardFactory.produce(processor,  target.getCardData(), player));
            Collections.shuffle(player.getDeck());
        }
    }
}
