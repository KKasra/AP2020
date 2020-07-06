package Game.GameStructure.CardModels.models;

import Game.CommandAndResponse.GameProcessor;
import Game.CommandAndResponse.PlayCommand;
import Game.GameStructure.CardModels.MinionModel;
import Game.GameStructure.CardModels.SpellModel;
import Game.GameStructure.Cards.Card;
import Game.GameStructure.Cards.CardFactory;
import Game.GameStructure.Cards.MinionCard;
import Game.GameStructure.Player;

public class swarmoflocusts extends SpellModel {
    public swarmoflocusts(GameProcessor processor, Player player, Card card) {
        super(processor, player, card);
    }

    @Override
    public void play(PlayCommand command) throws Exception {
        super.play(command);
        for (int i = 0; i < 7; ++i)
        try {
            MinionCard card = (MinionCard) CardFactory.produce(processor, getCard().getCardData(), player);
            card.setHealth(1);
            card.setDamge(1);
            player.getCardsOnBoard().add(card);
        } catch (Exception ignore){}
    }
}
