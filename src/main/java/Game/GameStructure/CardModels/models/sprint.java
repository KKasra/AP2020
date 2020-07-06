package Game.GameStructure.CardModels.models;

import Game.CommandAndResponse.GameProcessor;
import Game.CommandAndResponse.PlayCommand;
import Game.GameStructure.CardModels.SpellModel;
import Game.GameStructure.Cards.Card;
import Game.GameStructure.Player;

public class sprint extends SpellModel {
    public sprint(GameProcessor processor, Player player, Card card) {
        super(processor, player, card);
    }

    @Override
    public void play(PlayCommand command) throws Exception {
        super.play(command);
        for (int i = 0; i < 4; ++i) {
            try {
                player.drawFromDeck();
            }catch (Exception ignore){
            }
        }
    }
}
