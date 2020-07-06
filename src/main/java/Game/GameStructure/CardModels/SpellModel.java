package Game.GameStructure.CardModels;

import Game.CommandAndResponse.GameProcessor;
import Game.CommandAndResponse.PlayCommand;
import Game.GameStructure.Cards.Card;
import Game.GameStructure.Player;

public abstract class SpellModel extends CardModel{
    @Override
    public void play(PlayCommand command) throws Exception{
        super.play(command);
        die();
    }

    public SpellModel(GameProcessor processor, Player player, Card card) {
        super(processor, player, card);
    }
}
