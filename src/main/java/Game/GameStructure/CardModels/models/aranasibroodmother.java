package Game.GameStructure.CardModels.models;

import Game.CommandAndResponse.Command;
import Game.CommandAndResponse.GameProcessor;
import Game.GameStructure.CardModels.MinionModel;
import Game.GameStructure.Cards.Card;
import Game.GameStructure.Player;

public class aranasibroodmother extends MinionModel {
    public aranasibroodmother(GameProcessor processor, Player player, Card card) {
        super(processor, player, card);
        taunt = true;
    }

    boolean wasInHand;
    @Override
    public void observeBefore(Command command) throws Exception {
        super.observeBefore(command);
        wasInHand = player.getHand().contains(this.card);
    }

    @Override
    public void observeAfter(Command command) {
        super.observeAfter(command);
        if (wasInHand ^ player.getHand().contains(this.card)) {
            int hp = player.getHero().getHeroData().getHp();
            hp = Math.min(hp, player.getHero().getHp() + 4);
            player.getHero().setHp(hp);
        }

    }
}
