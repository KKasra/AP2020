package Game.Gamestructure;

import Game.Cards.Card;

import java.io.File;
import java.util.ArrayList;

public class Board {

    ArrayList<Player> players;
    void registerPlayer(Player player) {
        players.add(player);
    }
    void unregisterPlayer(Player player) {
        players.remove(player);
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }
}
