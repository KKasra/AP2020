package Game.GameStructure.CardModels.models;

import Game.CommandAndResponse.Command;
import Game.CommandAndResponse.GameProcessor;
import Game.GameStructure.CardModels.PassiveModel;
import Game.GameStructure.Cards.Card;
import Game.GameStructure.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class offcards extends PassiveModel {
    public offcards(GameProcessor processor, Player player, Card card) {
        super(processor, player, card);
    }

    Set<Card> saturatedCards = new HashSet<>();
    @Override
    protected boolean achieved(Command command) {
        return true;
    }

    @Override
    public void act(Command command) {
        for (Card card1 : player.getHand()) {
            if (!saturatedCards.contains(card1)){
                card1.setManaCost(card1.getManaCost() - 1);
                saturatedCards.add(card1);
            }
        }
    }
}
