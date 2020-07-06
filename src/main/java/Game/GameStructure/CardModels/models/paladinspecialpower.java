package Game.GameStructure.CardModels.models;

import Game.CommandAndResponse.Command;
import Game.CommandAndResponse.EndTurnCommand;
import Game.CommandAndResponse.GameProcessor;
import Game.GameStructure.CardModels.MinionModel;
import Game.GameStructure.CardModels.SpecialPower;
import Game.GameStructure.Cards.Card;
import Game.GameStructure.Cards.MinionCard;
import Game.GameStructure.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class paladinspecialpower extends SpecialPower {
    public paladinspecialpower(GameProcessor processor, Player player, Card card) {
        super(processor, player, card);
    }

    @Override
    public void observeAfter(Command command) {
        super.observeAfter(command);
        if (command instanceof EndTurnCommand) {
            List<MinionCard> cards = new ArrayList<>();
            for (Card card1 : player.getCardsOnBoard()) {
                if (card1 != null)
                    cards.add((MinionCard) card1);
            }
            try {
                Collections.shuffle(cards);
                cards.get(0).setDamge(cards.get(0).getDamage() + 1);
                cards.get(0).setHealth(cards.get(0).getHealth() + 1);
            }catch (Exception e){}
        }
    }
}
