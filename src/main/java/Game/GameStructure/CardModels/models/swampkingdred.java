package Game.GameStructure.CardModels.models;

import Game.CommandAndResponse.Command;
import Game.CommandAndResponse.GameProcessor;
import Game.CommandAndResponse.PlayCommand;
import Game.GameStructure.CardModels.MinionModel;
import Game.GameStructure.Cards.Card;
import Game.GameStructure.Cards.MinionCard;
import Game.GameStructure.Player;

public class swampkingdred extends MinionModel {
    @Override
    public void observeAfter(Command command) {
        super.observeAfter(command);
        if (command instanceof PlayCommand) {
            if (((PlayCommand) command).getCard() instanceof MinionCard)
                if (!((PlayCommand) command).getPlayerName().equals(player.getName()))
                    attack((MinionCard)((PlayCommand) command).getCard());
        }
    }

    public swampkingdred(GameProcessor processor, Player player, Card card) {
        super(processor, player, card);
    }
}
