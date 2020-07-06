package Game.GameStructure.CardModels.models;

import Game.CommandAndResponse.Command;
import Game.CommandAndResponse.GameProcessor;
import Game.CommandAndResponse.PlayCommand;
import Game.GameStructure.CardModels.SpecialPower;
import Game.GameStructure.Cards.Card;
import Game.GameStructure.Player;

public class warlockspecialpower extends SpecialPower {
    public warlockspecialpower(GameProcessor processor, Player player, Card card) {
        super(processor, player, card);
    }

    @Override
    public void play(PlayCommand command) throws Exception {
        super.play(command);
        player.getHero().setHp(35);
        die();
    }
}
