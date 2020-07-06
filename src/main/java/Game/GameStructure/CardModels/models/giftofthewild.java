package Game.GameStructure.CardModels.models;

import Game.CommandAndResponse.GameProcessor;
import Game.CommandAndResponse.PlayCommand;
import Game.GameStructure.CardModels.MinionModel;
import Game.GameStructure.CardModels.SpellModel;
import Game.GameStructure.Cards.Card;
import Game.GameStructure.Cards.MinionCard;
import Game.GameStructure.Player;

public class giftofthewild extends SpellModel {
    public giftofthewild(GameProcessor processor, Player player, Card card) {
        super(processor, player, card);
    }

    @Override
    public void play(PlayCommand command) throws Exception{
        super.play(command);
        for (Card c : player.getCardsOnBoard())
            if (c != null){
                MinionCard card = (MinionCard) c;
                card.setHealth(card.getHealth() + 2);
                card.setDamge(card.getDamage() + 2);
                ((MinionModel) card.getCardModel()).setTaunt(true);
            }
    }
}
