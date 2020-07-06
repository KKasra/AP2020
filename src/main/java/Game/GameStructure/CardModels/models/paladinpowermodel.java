package Game.GameStructure.CardModels.models;

import DB.Managment.CardManager;
import Game.CommandAndResponse.GameProcessor;
import Game.GameStructure.CardModels.HeroPower;
import Game.GameStructure.Cards.Card;
import Game.GameStructure.Cards.CardFactory;
import Game.GameStructure.Player;

public class paladinpowermodel extends HeroPower {
    public paladinpowermodel(GameProcessor processor, Player player, Card card) {
        super(processor, player, card);
        setManaCost(2);
    }

    @Override
    public void usePower() {
        super.usePower();
        try {
            DB.components.cards.Card card = CardManager.getInstance().getCard("Frostwolf Grunt");

            for (int i = 0; i < 2; ++i)
                for (int j = 0; j < player.getCardsOnBoard().size(); j++)
                    if (player.getCardsOnBoard().get(j) == null)
                        player.getCardsOnBoard()
                                .set(j, CardFactory.produce(processor, card, player));

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
