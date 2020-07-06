package Game.GameStructure.CardModels.models;

import Game.CommandAndResponse.Command;
import Game.CommandAndResponse.GameProcessor;
import Game.GameStructure.CardModels.SpecialPower;
import Game.GameStructure.Cards.Card;
import Game.GameStructure.Player;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class roguespecialpower extends SpecialPower {
    public roguespecialpower(GameProcessor processor, Player player, Card card) {
        super(processor, player, card);
    }
    Set<Card> saturatedCards = new HashSet<>();

    @Override
    public void observeAfter(Command command) {
        super.observeAfter(command);
        for (Card card1 : player.getHand()) {
            if (!card1.getHero().equals(player.getHero()))
                if (!saturatedCards.contains(card1)){
                    card.setManaCost(Math.max(0, card1.getManaCost() - 2));
                    saturatedCards.add(card1);
                }
        }

    }
}
