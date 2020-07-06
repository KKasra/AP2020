package Game.GameStructure.CardModels.models;

import Game.CommandAndResponse.Command;
import Game.CommandAndResponse.GameProcessor;
import Game.CommandAndResponse.PlayCommand;
import Game.GameStructure.CardModels.QuestAndRewardModel;
import Game.GameStructure.Cards.Card;
import Game.GameStructure.Cards.MinionCard;
import Game.GameStructure.Player;


public class strengthinnumbers extends QuestAndRewardModel {
    int manaSpendOnMinions = 0;

    public strengthinnumbers(GameProcessor processor, Player player, Card card) {
        super(processor, player, card);
    }

    @Override
    protected boolean achieved(Command command) {
        if (command instanceof PlayCommand)
            if (((PlayCommand) command).getPlayerName().equals(player.getName()))
                if (((PlayCommand) command).getCard() instanceof MinionCard)
                    manaSpendOnMinions += ((PlayCommand) command).getCard().getManaCost();


        return manaSpendOnMinions >= 10;
    }

    @Override
    protected void getReward(Command command) {
        MinionCard card = null;
        for (Card card1 : player.getDeck())
            if (card1 instanceof MinionCard) {
                card = (MinionCard) card1;
                break;
            }

        for (int i = 0 ; i < player.getCardsOnBoard().size(); ++i) {
            if (player.getCardsOnBoard().get(i) == null) {
                player.getCardsOnBoard().set(i, card);
                break;
            }
        }

    }
}
