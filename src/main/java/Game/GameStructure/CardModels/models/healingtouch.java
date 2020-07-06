package Game.GameStructure.CardModels.models;

import Game.CommandAndResponse.GameProcessor;
import Game.CommandAndResponse.PlayCommand;
import Game.GameStructure.CardModels.SpellModel;
import Game.GameStructure.Cards.Card;
import Game.GameStructure.Player;

public class healingtouch extends SpellModel {
    public healingtouch(GameProcessor processor, Player player, Card card) {
        super(processor, player, card);
    }

    @Override
    public void play(PlayCommand command) throws Exception{
        super.play(command);
        int health = player.getHero().getHeroData().getHp();
        health = Math.min(health, player.getHero().getHp() + 8);
        player.getHero().setHp(health);
    }
}
