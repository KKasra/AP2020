package Game.GameStructure.CardModels.models;

import Game.CommandAndResponse.GameProcessor;
import Game.CommandAndResponse.PlayCommand;
import Game.GameStructure.CardModels.SpellModel;
import Game.GameStructure.Cards.Card;
import Game.GameStructure.Cards.SpellCard;
import Game.GameStructure.Player;

public class bookofspecters extends SpellModel {
    public bookofspecters(GameProcessor processor, Player player, Card card) {
        super(processor, player, card);
    }

    @Override
    public void play(PlayCommand command) throws Exception {
        super.play(command);

        for (int i = 0; i < 3; ++i)
            try {
                player.drawFromDeck();
            }catch (Exception ignore) {}

        for (int i = player.getHand().size() - 1; i >= 0; i--) {
            Card c = player.getHand().get(i);
            if (c instanceof SpellCard) {
                player.setMana(player.getMana() + c.getManaCost());
                player.playCard(new PlayCommand(player.getName(), player.getHand().indexOf(c), 0));
            }
        }
    }
}
