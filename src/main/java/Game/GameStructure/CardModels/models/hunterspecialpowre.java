package Game.GameStructure.CardModels.models;

import Game.CommandAndResponse.Command;
import Game.CommandAndResponse.GameProcessor;
import Game.GameStructure.CardModels.MinionModel;
import Game.GameStructure.CardModels.SpecialPower;
import Game.GameStructure.Cards.Card;
import Game.GameStructure.Cards.MinionCard;
import Game.GameStructure.Player;

import java.util.HashSet;
import java.util.Set;

public class hunterspecialpowre extends SpecialPower {

    public hunterspecialpowre(GameProcessor processor, Player player, Card card) {
        super(processor, player, card);
    }

    Set<MinionCard> saturatedCards = new HashSet<>();
    @Override
    public void observeAfter(Command command) {
        super.observeAfter(command);
        for (Card card1 : player.getHand()) {
            if (card1 instanceof MinionCard)
                if (!saturatedCards.contains(card1)) {
                    ((MinionModel)card1.getCardModel()).setRush(true);
                    saturatedCards.add((MinionCard) card1);
                }
        }
    }
}
