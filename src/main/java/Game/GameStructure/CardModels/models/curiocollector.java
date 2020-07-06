package Game.GameStructure.CardModels.models;

import Game.CommandAndResponse.Command;
import Game.CommandAndResponse.GameProcessor;
import Game.GameStructure.CardModels.MinionModel;
import Game.GameStructure.Cards.Card;
import Game.GameStructure.Cards.MinionCard;
import Game.GameStructure.Player;

public class curiocollector extends MinionModel {
    public curiocollector(GameProcessor processor, Player player, Card card) {
        super(processor, player, card);
    }

    int numberOfDeckCards = 0;

    @Override
    public void observeBefore(Command command) throws Exception {
        super.observeBefore(command);
        numberOfDeckCards = player.getHand().size();
    }

    @Override
    public void observeAfter(Command command) {
        super.observeAfter(command);

        int dif = player.getHand().size() - numberOfDeckCards;
        ((MinionCard)card).setDamge( ((MinionCard)card).getDamage() + dif);
        ((MinionCard)card).setHealth( ((MinionCard)card).getHealth() + dif);
    }
}
