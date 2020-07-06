package Game.GameStructure.CardModels;

import Game.CommandAndResponse.Command;
import Game.CommandAndResponse.GameProcessor;
import Game.GameStructure.Cards.Card;
import Game.GameStructure.Player;

public abstract class HeroPower extends CardModel {

    private int manaCost;

    public HeroPower(GameProcessor processor, Player player, Card card) {
        super(processor, player, card);
    }
    public void usePower(){
        lastNumberUsed = processor.getGame().getNumberOfTurns();
    };

    public int getManaCost() {
        return manaCost;
    }

    public void setManaCost(int manaCost) {
        this.manaCost = manaCost;
    }

    int lastNumberUsed = 0;
    @Override
    public void observeBefore(Command command) throws Exception {
        super.observeBefore(command);
        if (lastNumberUsed == processor.getGame().getNumberOfTurns())
            throw new Exception("hero power already used this turn");
    }
}
