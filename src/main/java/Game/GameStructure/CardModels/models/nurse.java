package Game.GameStructure.CardModels.models;

import Game.CommandAndResponse.Command;
import Game.CommandAndResponse.EndTurnCommand;
import Game.CommandAndResponse.GameProcessor;
import Game.GameStructure.CardModels.PassiveModel;
import Game.GameStructure.Cards.Card;
import Game.GameStructure.Cards.MinionCard;
import Game.GameStructure.Player;

import java.util.*;

public class nurse extends PassiveModel {
    public nurse(GameProcessor processor, Player player, Card card) {
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
        List<MinionCard> minions = new ArrayList<>();
        for (Card card1 : player.getCardsOnBoard()) {
            if (card1 != null)
                minions.add((MinionCard) card1);
        }

        Collections.shuffle(minions);
        if (!minions.isEmpty())
            minions.get(0).setHealth(minions.get(0).getHealth() + 1);
    }
}
