package Game.GameStructure.CardModels.models;

import Game.CommandAndResponse.GameProcessor;
import Game.GameStructure.CardModels.MinionModel;
import Game.GameStructure.Cards.Card;
import Game.GameStructure.Player;

public class guardbot extends MinionModel {
    public guardbot(GameProcessor processor, Player player, Card card) {
        super(processor, player, card);
        taunt = true;
    }

}
