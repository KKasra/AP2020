package Game.GameStructure.CardModels.models;

import Game.CommandAndResponse.Command;
import Game.CommandAndResponse.GameProcessor;
import Game.CommandAndResponse.PlayCommand;
import Game.GameStructure.CardModels.HeroPower;
import Game.GameStructure.Cards.Card;
import Game.GameStructure.Cards.MinionCard;
import Game.GameStructure.Player;

public class hunterpowermodel extends HeroPower {
    public hunterpowermodel(GameProcessor processor, Player player, Card card) {
        super(processor, player, card);
    }

    @Override
    public void usePower() {
    }

    @Override
    public void observeAfter(Command command) {
        super.observeAfter(command);
        if (command instanceof PlayCommand)
            if (!((PlayCommand) command).getPlayerName().equals(player.getName()))
                if (((PlayCommand) command).getCard() instanceof MinionCard) {
                    ((MinionCard) ((PlayCommand) command).getCard()).receiveDamage(1);
                }
    }
}
