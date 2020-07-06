package Game.GameStructure.CardModels.models;

import Game.CommandAndResponse.Command;
import Game.CommandAndResponse.EndTurnCommand;
import Game.CommandAndResponse.GameProcessor;
import Game.GameStructure.CardModels.HeroPower;
import Game.GameStructure.Cards.Card;
import Game.GameStructure.Cards.MinionCard;
import Game.GameStructure.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class warlockpowermodel extends HeroPower {
    public warlockpowermodel(GameProcessor processor, Player player, Card card) {
        super(processor, player, card);
    }

    @Override
    public void usePower() {
    }

    @Override
    public void observeAfter(Command command) {
        super.observeAfter(command);
        if (command instanceof EndTurnCommand)
            if (processor.getGame().getPlayerOnTurn() == player){
                player.getHero().setHp(player.getHero().getHp() - 2);
                int action = new Random().nextInt(2);

                try {
                    switch (action) {
                        case 0:
                            player.drawFromDeck();
                            break;
                        case 1:
                            List<MinionCard> cards = new ArrayList<>();
                            for (Card card1 : player.getCardsOnBoard()) {
                                if (card1 != null && card1 instanceof MinionCard)
                                    cards.add((MinionCard)card1);
                            }
                            int index = new Random().nextInt(cards.size());
                            cards.get(index).setHealth(cards.get(index).getHealth() + 1);
                            cards.get(index).setDamge(cards.get(index).getDamage() + 1);
                            break;
                    }
                }catch (Exception e) {}
            }
    }
}
