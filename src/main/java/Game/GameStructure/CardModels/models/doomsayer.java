package Game.GameStructure.CardModels.models;

import Game.CommandAndResponse.Command;
import Game.CommandAndResponse.EndTurnCommand;
import Game.CommandAndResponse.GameProcessor;
import Game.GameStructure.Attackable;
import Game.GameStructure.CardModels.MinionModel;
import Game.GameStructure.Cards.Card;
import Game.GameStructure.Player;

public class doomsayer extends MinionModel {
    public doomsayer(GameProcessor processor, Player player, Card card) {
        super(processor, player, card);
    }


    @Override
    public void observeAfter(Command command) {
        super.observeAfter(command);
        if (command instanceof EndTurnCommand)
            if (processor.getGame().getNumberOfTurns() == turnPlayed + 2) {
                for (Player p : processor.getGame().getPlayers()) {
                    for (Card c : p.getCardsOnBoard()) {
                        ((Attackable) c).die();
                    }
                }
            }

    }
}
