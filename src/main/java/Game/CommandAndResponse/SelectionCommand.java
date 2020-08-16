package Game.CommandAndResponse;

import Game.GameStructure.Game;
import Game.GameStructure.Player;

public class SelectionCommand extends Command{
    private final Section section;

    public enum Section{
        hero, deck, hand, battleGround
    }
    private int indexOfPlayer;
    private int index;
    private Object object;

    public SelectionCommand(Object o) {
        object = o;
        section = null;
    }

    public SelectionCommand(Section section, int indexOfPlayer) {
        this.section = section;
        this.indexOfPlayer = indexOfPlayer;
    }

    public Section getSection() {
        return section;
    }

    public int getIndexOfPlayer() {
        return indexOfPlayer;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public static Object getTarget(Game game, SelectionCommand instance) {
        if (instance.object != null)
            return instance.object;
        Player player = game.getPlayers().get(instance.indexOfPlayer);
        switch (instance.section) {
            case deck: return player.getDeck();
            case hand: return player.getHand().get(instance.index);
            case hero: return player.getHero();
            case battleGround: return player.getCardsOnBoard().get(instance.index);
            default: return null;
        }
    }

    @Override
    public String toString() {
        return "SelectionCommand{" +
                "section=" + section +
                '}';
    }
}
