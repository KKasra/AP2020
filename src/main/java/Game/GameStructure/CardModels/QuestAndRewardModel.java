package Game.GameStructure.CardModels;

import Game.CommandAndResponse.Command;
import Game.CommandAndResponse.GameProcessor;
import Game.GameStructure.Cards.Card;
import Game.GameStructure.Player;

public abstract class QuestAndRewardModel extends CardModel{
    protected abstract boolean achieved(Command command);
    protected abstract void getReward(Command command);

    @Override
    public void observeAfter(Command command) {
        super.observeAfter(command);
        if (achieved(command)) {
            getReward(command);
            die();
        }
    }

    public QuestAndRewardModel(GameProcessor processor, Player player, Card card) {
        super(processor,player, card);
    }
}
