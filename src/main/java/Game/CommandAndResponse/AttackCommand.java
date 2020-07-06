package Game.CommandAndResponse;

import Game.GameStructure.Attacker;

public class AttackCommand extends Command{
    private Attacker attacker;

    public AttackCommand(Attacker attacker) {
        this.attacker = attacker;
    }

    public Attacker getAttacker() {
        return attacker;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
