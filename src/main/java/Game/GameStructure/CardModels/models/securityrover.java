package Game.GameStructure.CardModels.models;


import DB.Managment.CardManager;
import Game.CommandAndResponse.GameProcessor;
import Game.GameStructure.CardModels.MinionModel;
import Game.GameStructure.Cards.Card;
import Game.GameStructure.Cards.CardFactory;
import Game.GameStructure.Cards.MinionCard;
import Game.GameStructure.Player;

public class securityrover extends MinionModel {

    public securityrover(GameProcessor processor, Player player, Card card) {
        super(processor, player, card);
    }

    @Override
    public void receiveDamage(int amount) {
        super.receiveDamage(amount);
        MinionCard c = null;
        try {
            c = (MinionCard) CardFactory.produce(processor, CardManager.getInstance().getCard("Guard Bot"), player);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        for (int i = 0 ; i < player.getCardsOnBoard().size(); ++i)
            if (player.getCardsOnBoard().get(i) == null) {
                player.getCardsOnBoard().set(i, c);
                break;
            }
    }
}
