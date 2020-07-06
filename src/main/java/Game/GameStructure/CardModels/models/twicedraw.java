package Game.GameStructure.CardModels.models;

import Game.CommandAndResponse.Command;
import Game.CommandAndResponse.EndTurnCommand;
import Game.CommandAndResponse.GameProcessor;
import Game.GameStructure.CardModels.PassiveModel;
import Game.GameStructure.Cards.Card;
import Game.GameStructure.Player;

public class twicedraw extends PassiveModel {
    public twicedraw(GameProcessor processor, Player player, Card card) {
        super(processor, player, card);
    }

    @Override
    protected boolean achieved(Command command) {
        if (command instanceof EndTurnCommand)
            if (player != processor.getGame().getPlayerOnTurn())
                return true;
        return false;
    }

    @Override
    public void act(Command command) {
        try {
            player.drawFromDeck();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
