package Game.GameStructure.CardModels;

import Game.CommandAndResponse.*;
import Game.GameStructure.Attackable;
import Game.GameStructure.Cards.Card;
import Game.GameStructure.Cards.MinionCard;
import Game.GameStructure.Player;

public abstract class MinionModel extends CardModel implements Attackable{
    protected int turnPlayed;
    protected boolean charge = false;
    protected boolean taunt = false;
    protected boolean rush = false;
    protected boolean divineShield = false;
    public MinionModel(GameProcessor processor, Player player, Card card) {
        super(processor, player, card);
    }

    public void play(PlayCommand command) throws Exception{
        super.play(command);
        if (player.getCardsOnBoard().get(command.getIndexOnBoard()) != null)
            throw new Exception("occupied space");
        turnPlayed = processor.getGame().getNumberOfTurns();
        player.getCardsOnBoard().set(command.getIndexOnBoard(), command.getCard());
    }

    public void attack(Attackable target) {
        target.receiveDamage(((MinionCard)card).getAttack());
        if (target instanceof MinionCard) {
            receiveDamage(((MinionCard) target).getDamage());
        }
        lastTurnAttacked = processor.getGame().getNumberOfTurns();
    }


    boolean attacking = false;
    int lastTurnAttacked = 0;
    @Override
    public void observeBefore(Command command) throws Exception {
        super.observeBefore(command);
        if (command instanceof AttackCommand) {
            if (((AttackCommand) command).getAttacker() == this.card) {
                //first turn minions can't attack, unless they have charge
                if (turnPlayed == processor.getGame().getNumberOfTurns() && !charge && !rush)
                    throw new Exception("not this turn");
                //each turn a minion can attack at most once
                if (lastTurnAttacked == processor.getGame().getNumberOfTurns()) {
                    throw new Exception("this minion has already attacked");
                }


            }

            attacking = true;
        }

        //taunts should be attacked first
        if (taunt && command instanceof SelectionCommand) {
            Attackable target = (Attackable) ((SelectionCommand) command).getTarget();
            if (target.getPlayer() == player) {
                if (!(target instanceof MinionCard))
                    throw new Exception("Taunt");
                else if (!((MinionModel)((MinionCard) target).getCardModel()).taunt)
                    throw new Exception("Taunt");
            }

        }


    }

    @Override
    public void receiveDamage(int amount) {
        if (divineShield)
            divineShield = false;
        else
            ((MinionCard)card).setHealth(((MinionCard)card).getHealth() - amount);

        if (((MinionCard)card).getHealth() <= 0)
            die();

    }

    @Override
    public void die() {
        super.die();
        player.getCardsOnBoard().set(player.getCardsOnBoard().indexOf(card), null);
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public void observeAfter(Command command) {
        super.observeAfter(command);
        if (command instanceof SelectionCommand && attacking)
            attacking = false;
    }

    public boolean isTaunt() {
        return taunt;
    }

    public void setTaunt(boolean taunt) {
        this.taunt = taunt;
    }

    public void setCharge(boolean b) {
        this.charge = b;
    }

    public boolean isDivineShield() {
        return divineShield;
    }

    public void setDivineShield(boolean divineShield) {
        this.divineShield = divineShield;
    }

    public boolean isRush() {
        return rush;
    }

    public void setRush(boolean rush) {
        this.rush = rush;
    }
}
