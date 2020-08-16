package Game.GameStructure.CardModels;

import Game.CommandAndResponse.AttackCommand;
import Game.CommandAndResponse.Command;
import Game.CommandAndResponse.GameProcessor;
import Game.CommandAndResponse.PlayCommand;
import Game.GameStructure.Attackable;
import Game.GameStructure.Cards.Card;
import Game.GameStructure.Cards.WeaponCard;
import Game.GameStructure.Player;

public abstract class WeaponModel extends CardModel{
    private int turnPlayed;
    protected int getDurability() {
        return ((WeaponCard)card).getDurability();
    }

    @Override
    public void play(PlayCommand command) throws Exception{
        super.play(command);
        turnPlayed = processor.getGame().getNumberOfTurns();
        player.getHero().setWeaponCard((WeaponCard)card);
    }
    public void attack(Attackable target) {
        target.receiveDamage(((WeaponCard)card).getAttack());
        lastTurnAttacked = processor.getGame().getNumberOfTurns();
    }


    private int lastTurnAttacked = 0;
    @Override
    public void observeBefore(Command command) throws Exception {
        super.observeBefore(command);
        if (processor.getGame().getNumberOfTurns() >= getDurability() * 2 + turnPlayed) {
            player.getHero().setWeaponCard(null);
            die();
        }

        if (command instanceof AttackCommand) {
            if (((AttackCommand) command).getAttacker(processor.getGame(), (AttackCommand) command) == card)
                if (lastTurnAttacked == processor.getGame().getNumberOfTurns())
                    throw new Exception("not this turn");
        }
    }

    public WeaponModel(GameProcessor processor, Player player, Card card) {
        super(processor, player, card);
    }
}
