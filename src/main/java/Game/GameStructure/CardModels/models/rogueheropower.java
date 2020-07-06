package Game.GameStructure.CardModels.models;

import Game.CommandAndResponse.GameProcessor;
import Game.GameStructure.CardModels.HeroPower;
import Game.GameStructure.Cards.Card;
import Game.GameStructure.Player;

import java.util.Collection;
import java.util.Collections;
import java.util.Random;

public class rogueheropower extends HeroPower {
    public rogueheropower(GameProcessor processor, Player player, Card card) {
        super(processor, player, card);
        setManaCost(3);
    }

    @Override
    public void usePower() {
        try {
            Player enemy = null;
            for (Player player1 : processor.getGame().getPlayers()) {
                if (player1 != player)
                    enemy = player1;
            }

            Collections.shuffle(enemy.getDeck());
            int index = new Random().nextInt(enemy.getDeck().size());

            Card card = enemy.getDeck().get(index);
            enemy.getDeck().remove(card);
            player.getHand().add(card);

            if (player.getHero().getWeapon() != null) {
                Collections.shuffle(enemy.getHand());
                index = new Random().nextInt(enemy.getHand().size());

                card = enemy.getHand().get(index);
                enemy.getHand().remove(card);
                player.getHand().add(card);
            }

        }catch (Exception ignore){}
    }
}
