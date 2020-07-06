package Game.GameStructure.CardModels.models;

import Game.CommandAndResponse.GameProcessor;
import Game.CommandAndResponse.PlayCommand;
import Game.GameStructure.CardModels.MinionModel;
import Game.GameStructure.Cards.Card;
import Game.GameStructure.Cards.CardFactory;
import Game.GameStructure.Cards.MinionCard;
import Game.GameStructure.Player;

import java.util.Collection;
import java.util.Collections;

public class sathrovarr extends MinionModel {
    public sathrovarr(GameProcessor processor, Player player, Card card) {
        super(processor, player, card);
    }

    @Override
    public void play(PlayCommand command) throws Exception {
        super.play(command);

        MinionCard target = (MinionCard)processor.getTargetReceiver().receiveTarget(o -> {
           if (o instanceof MinionCard)
               if (((MinionCard) o).getPlayer() == player)
                   return true;
           return false;
        });

        MinionCard cards[] = new MinionCard[3];
        for (int i = 0; i < 3; ++i)
            cards[i] = (MinionCard) CardFactory.produce(processor,target.getCardData(), player);

        player.getHand().add(cards[0]);


        for (int i = 0 ; i < player.getCardsOnBoard().size(); ++i) {
            if (player.getCardsOnBoard().get(i) == null) {
                player.getCardsOnBoard().set(i, cards[1]);
                break;
            }
        }

        player.getDeck().add(cards[2]);
        Collections.shuffle(player.getDeck());
    }
}
