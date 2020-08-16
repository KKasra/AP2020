package Game.GameStructure.CardModels.models;

import DB.Managment.CardManager;
import Game.CommandAndResponse.DiscoverRequest;
import Game.CommandAndResponse.GameProcessor;
import Game.CommandAndResponse.PlayCommand;
import Game.GameStructure.CardModels.SpellModel;
import Game.GameStructure.Cards.Card;
import Game.GameStructure.Cards.CardFactory;
import Game.GameStructure.Cards.WeaponCard;
import Game.GameStructure.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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

        List<DB.components.cards.WeaponCard> cards = new ArrayList<>();
        for (DB.components.cards.Card card1 : CardManager.getInstance().getCards())
            if (card1 instanceof DB.components.cards.WeaponCard)
                cards.add((DB.components.cards.WeaponCard) card1);

        Collections.shuffle(cards);
        cards = cards.subList(0, 3);
        DB.components.cards.WeaponCard cardData =
                (DB.components.cards.WeaponCard) processor.getTargetReceiver().discover(cards);

        WeaponCard card1 = (WeaponCard) CardFactory.produce(processor, cardData,player);

        card1.setAttack(card1.getAttack() + 2);
        card1.setDurability(card1.getDurability() + 2);

        player.getDeck().add(card1);

    }
}
