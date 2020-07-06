package Game.GameStructure.CardModels.models;

import Game.CommandAndResponse.Command;
import Game.CommandAndResponse.GameProcessor;
import Game.GameStructure.CardModels.SpecialPower;
import Game.GameStructure.Cards.Card;
import Game.GameStructure.Cards.SpellCard;
import Game.GameStructure.Player;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class magespecialpower extends SpecialPower {
    public magespecialpower(GameProcessor processor, Player player, Card card) {
        super(processor, player, card);
    }
    Set<SpellCard> saturatedCards = new HashSet<>();

    @Override
    public void observeAfter(Command command) {
        super.observeAfter(command);
        for (Card card1 : player.getHand()) {
            if (card1 instanceof SpellCard && !saturatedCards.contains(card1)){
                card1.setManaCost(Math.max(0, card1.getManaCost() - 2));
                saturatedCards.add((SpellCard)card1);
            }
        }
    }
}
