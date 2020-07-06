package Game.GameStructure.CardModels.models;

import Game.CommandAndResponse.Command;
import Game.CommandAndResponse.GameProcessor;
import Game.CommandAndResponse.PlayCommand;
import Game.GameStructure.CardModels.QuestAndRewardModel;
import Game.GameStructure.Cards.Card;
import Game.GameStructure.Cards.MinionCard;
import Game.GameStructure.Player;

public class caltrops extends QuestAndRewardModel {
    @Override
    protected boolean achieved(Command command) {
        if (command instanceof PlayCommand){
            if (!((PlayCommand) command).getPlayerName().equals(player.getName()))
                if (((PlayCommand) command).getCard() instanceof MinionCard)
                    return true;
        }
        return false;
    }

    @Override
    protected void getReward(Command command) {
        ((MinionCard)((PlayCommand)command).getCard()).receiveDamage(1);
    }

    public caltrops(GameProcessor processor, Player player, Card card) {
        super(processor, player, card);
    }

}
