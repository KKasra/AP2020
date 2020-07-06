package Game.GameStructure.CardModels;

import Game.CommandAndResponse.GameProcessor;
import Game.GameStructure.Cards.Card;
import Game.GameStructure.Player;

public class SpecialPower extends CardModel{
    public SpecialPower(GameProcessor processor, Player player, Card card) {
        super(processor, player, card);
    }
}
