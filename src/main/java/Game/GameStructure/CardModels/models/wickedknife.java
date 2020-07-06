package Game.GameStructure.CardModels.models;

import Game.CommandAndResponse.GameProcessor;
import Game.GameStructure.CardModels.WeaponModel;
import Game.GameStructure.Cards.Card;
import Game.GameStructure.Cards.WeaponCard;
import Game.GameStructure.Player;

public class wickedknife extends WeaponModel {
    public wickedknife(GameProcessor processor, Player player, Card card) {
        super(processor, player, card);
    }
}
