package Game.GameStructure.CardModels.models;

import Game.CommandAndResponse.Command;
import Game.CommandAndResponse.GameProcessor;
import Game.GameStructure.CardModels.PassiveModel;
import Game.GameStructure.Cards.Card;
import Game.GameStructure.Player;

public class freepower extends PassiveModel {
    public freepower(GameProcessor processor, Player player, Card card) {
        super(processor, player, card);
    }

    @Override
    protected boolean achieved(Command command) {
        return true;
    }

    @Override
    public void act(Command command) {
        player.getHero().getPower().setManaCost(player.getHero().getPower().getManaCost() - 1);
        die();
    }
}
