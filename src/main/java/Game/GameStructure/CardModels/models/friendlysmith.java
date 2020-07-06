package Game.GameStructure.CardModels.models;

import DB.Managment.CardManager;
import Game.CommandAndResponse.GameProcessor;
import Game.CommandAndResponse.PlayCommand;
import Game.GameStructure.CardModels.SpellModel;
import Game.GameStructure.Cards.Card;
import Game.GameStructure.Cards.CardFactory;
import Game.GameStructure.Cards.WeaponCard;
import Game.GameStructure.Player;

import java.util.SplittableRandom;

public class friendlysmith extends SpellModel {
    public friendlysmith(GameProcessor processor, Player player, Card card) {
        super(processor, player, card);
    }

    @Override
    public void play(PlayCommand command) throws Exception {
        super.play(command);

        WeaponCard card = (WeaponCard) CardFactory.produce(processor,
                CardManager.getInstance().getCard("Gearblade"), player);
        card.setDurability(card.getDurability() + 2);
        card.setAttack(card.getAttack() + 2);
        player.getDeck().add(card);
    }
}
