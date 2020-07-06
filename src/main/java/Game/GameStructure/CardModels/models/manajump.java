package Game.GameStructure.CardModels.models;

import Game.CommandAndResponse.Command;
import Game.CommandAndResponse.EndTurnCommand;
import Game.CommandAndResponse.GameProcessor;
import Game.GameStructure.CardModels.PassiveModel;
import Game.GameStructure.Cards.Card;
import Game.GameStructure.Player;

public class manajump extends PassiveModel {
    public manajump(GameProcessor processor, Player player, Card card) {
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
        player.setMana(player.getMana() + 1);
    }
}
