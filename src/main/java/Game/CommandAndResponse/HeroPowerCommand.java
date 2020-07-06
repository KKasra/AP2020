package Game.CommandAndResponse;

import Game.GameStructure.CardModels.HeroPower;
import Game.GameStructure.Player;

public class HeroPowerCommand extends Command{
    private HeroPower power;
    private Player player;
    public HeroPowerCommand(HeroPower power, Player player) {
        this.power = power;
        this.player = player;
    }

    public HeroPower getPower() {
        return power;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
