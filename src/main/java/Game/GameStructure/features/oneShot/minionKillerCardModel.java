package Game.GameStructure.features.oneShot;

import Game.CommandAndResponse.AttackCommand;
import Game.CommandAndResponse.Command;
import Game.CommandAndResponse.GameProcessor;
import Game.CommandAndResponse.SelectionCommand;
import Game.GameStructure.CardModels.CardModel;
import Game.GameStructure.Cards.Card;
import Game.GameStructure.Cards.MinionCard;
import Game.GameStructure.Player;

import java.util.List;

public class minionKillerCardModel extends CardModel {
    public minionKillerCardModel(GameProcessor processor, Player player, Card card) {
        super(processor, player, card);
    }

    private Object target;

    @Override
    public void observeAfter(Command command) {
        super.observeAfter(command);
        if (command instanceof AttackCommand)
            if (target instanceof MinionCard)
                ((MinionCard) target).die();
        if (command instanceof SelectionCommand)
            target = SelectionCommand.getTarget(processor.getGame(), (SelectionCommand) command);

    }
}
