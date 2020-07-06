package Game.GameStructure.Cards;

import Game.GameStructure.Game;
import Game.GameStructure.Player;

public class SpellCard extends Card{
    public SpellCard(DB.components.cards.SpellCard cardData, Player player) {
        super(cardData, player);
    }
}
