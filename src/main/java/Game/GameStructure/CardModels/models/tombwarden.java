package Game.GameStructure.CardModels.models;

import Game.CommandAndResponse.GameProcessor;
import Game.CommandAndResponse.PlayCommand;
import Game.GameStructure.CardModels.MinionModel;
import Game.GameStructure.Cards.Card;
import Game.GameStructure.Cards.CardFactory;
import Game.GameStructure.Player;

public class tombwarden extends MinionModel {
    public tombwarden(GameProcessor processor, Player player, Card card) {
        super(processor, player, card);
    }

    @Override
    public void play(PlayCommand command) throws Exception {
        super.play(command);

        Card copy = CardFactory.produce(processor, card.getCardData(), player);
        for (int i = 0 ; i < player.getCardsOnBoard().size(); ++i)
            if (player.getCardsOnBoard().get(i) == null) {
                player.getCardsOnBoard().set(i, copy);
                break;
            }
    }
}
