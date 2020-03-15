package Game.Gamestructure;

import java.util.ArrayList;

public class Board {
    private UserInterface userInterface;

    public UserInterface getUserInterface() {
        return userInterface;
    }

    ArrayList<Player> players;
    void registerPlayer(Player player) {
        players.add(player);
    }
    void unregisterPlayer(Player player) {
        players.remove(player);
    }

}
