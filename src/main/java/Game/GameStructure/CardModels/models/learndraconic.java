package Game.GameStructure.CardModels.models;

import Game.CommandAndResponse.Command;
import Game.CommandAndResponse.GameProcessor;
import Game.CommandAndResponse.PlayCommand;
import Game.GameStructure.CardModels.QuestAndRewardModel;
import Game.GameStructure.CardModels.SpellModel;
import Game.GameStructure.Cards.Card;
import Game.GameStructure.Cards.SpellCard;
import Game.GameStructure.Player;

public class learndraconic extends QuestAndRewardModel {
    public learndraconic(GameProcessor processor, Player player, Card card) {
        super(processor, player, card);
    }

    int manaSpentOnSpell = 0;

    @Override
    protected boolean achieved(Command command) {
        if (command instanceof PlayCommand)
            if (((PlayCommand) command).getPlayerName().equals(player.getName()))
                if (((PlayCommand) command).getCard() instanceof SpellCard)
                    manaSpentOnSpell += ((PlayCommand) command).getCard().getManaCost();
        return manaSpentOnSpell >= 8;
    }

    @Override
    protected void getReward(Command command) {

    }
}
