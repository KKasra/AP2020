package Game.CommandAndResponse;

import Game.GameStructure.Attacker;
import Game.GameStructure.Game;
import Game.GameStructure.Player;

public class AttackCommand extends Command{
    public enum Section {
        weapon, battleGround, heroPower
    }

    private int indexOfPlayer;
    private int index;
    private Section section;
    public AttackCommand(int indexOfPlayer, Section section, int index) {
        this.indexOfPlayer = indexOfPlayer;
        this.index = index;
        this.section = section;
    }

    public int getIndex() {
        return index;
    }

    public Section getSectoin() {
        return section;
    }

    public static Attacker getAttacker(Game game, AttackCommand command){
        Player player = game.getPlayers().get(command.indexOfPlayer);
        switch (command.section) {
            case weapon: return player.getHero().getWeapon();
            case heroPower: return (Attacker) player.getHero().getPower();
            case battleGround: return (Attacker) player.getCardsOnBoard().get(command.index);
            default: return null;
        }
    }

}
