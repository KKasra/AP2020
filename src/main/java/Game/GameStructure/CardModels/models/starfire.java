package Game.GameStructure.CardModels.models;

import Game.CommandAndResponse.*;
import Game.GameStructure.Attackable;
import Game.GameStructure.CardModels.SpellModel;
import Game.GameStructure.Cards.Card;
import Game.GameStructure.Cards.CardFactory;
import Game.GameStructure.Cards.MinionCard;
import Game.GameStructure.Player;

import java.util.Collections;

public class starfire extends SpellModel {
    public starfire(GameProcessor processor, Player player, Card card) {
        super(processor, player, card);
    }

    @Override
    public void play(PlayCommand command) throws Exception{
        super.play(command);

       Attackable target = (Attackable) processor.getTargetReceiver().receiveTarget(o -> o instanceof Attackable);

       target.receiveDamage(5);
        player.drawFromDeck();

    }
}
