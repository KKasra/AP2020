package Game.GameStructure.CardModels.models;

import Game.CommandAndResponse.GameProcessor;
import Game.CommandAndResponse.PlayCommand;
import Game.GameStructure.CardModels.MinionModel;
import Game.GameStructure.CardModels.SpellModel;
import Game.GameStructure.Cards.Card;
import Game.GameStructure.Cards.MinionCard;
import Game.GameStructure.Player;

public class arcaneexplosion extends SpellModel {
    public arcaneexplosion(GameProcessor processor, Player player, Card card) {
        super(processor, player, card);
    }

    @Override
    public void play(PlayCommand command) throws Exception{
        super.play(command);
        for (Player p : processor.getGame().getPlayers()) {
            if (p != player) {
                for (Card c : p.getCardsOnBoard())
                    if (c != null){
                        ((MinionCard) c).receiveDamage(1);
                    }
            }
        }
    }
}
