package Game.GameStructure.CardModels.models;

import Game.CommandAndResponse.Command;
import Game.CommandAndResponse.EndTurnCommand;
import Game.CommandAndResponse.GameProcessor;
import Game.GameStructure.CardModels.MinionModel;
import Game.GameStructure.Cards.Card;
import Game.GameStructure.Cards.MinionCard;
import Game.GameStructure.Player;

public class dreadscale extends MinionModel {

    public dreadscale(GameProcessor processor, Player player, Card card) {
        super(processor, player, card);
    }


    @Override
    public void observeAfter(Command command) {
        super.observeAfter(command);
        if (command instanceof EndTurnCommand) {
            for (Player player1 : processor.getGame().getPlayers())
                for (Card card1 : player1.getCardsOnBoard())
                    if (card1 != null && card1 != card)
                        ((MinionCard) card1).receiveDamage(1);
            die();
        }
    }
}
