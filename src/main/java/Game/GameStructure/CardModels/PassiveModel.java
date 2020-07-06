package Game.GameStructure.CardModels;

import Game.CommandAndResponse.Command;
import Game.CommandAndResponse.GameProcessor;
import Game.GameStructure.Cards.Card;
import Game.GameStructure.Player;
import org.h2.command.CommandContainer;

public abstract class PassiveModel extends CardModel{

    public PassiveModel(GameProcessor processor, Player player, Card card) {
        super(processor, player, card);
    }

    //the command is the last command passed on to the processor
    protected abstract boolean achieved(Command command);

    //the command is the same command passed on to achieved method
    public abstract void act(Command command);

    @Override
    public void observeAfter(Command command) {
        super.observeAfter(command);
        if (achieved(command)) {
            act(command);
        }
    }
}
