package Game.GameStructure;

import DB.Managment.HeroManager;
import DB.components.*;
import DB.components.cards.Deck;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Game implements Serializable {
    private List<Player> players = new ArrayList<>();
    private int NumberOfTurns;
    List<String> logs;
    public Game() {
        logs = new ArrayList<>();

    }

    public List<String> getLogs() {
        return logs;
    }

    public int getNumberOfTurns() {
        return NumberOfTurns;
    }

    public void setNumberOfTurns(int numberOfTurns) {
        NumberOfTurns = numberOfTurns;
    }

    public Player getPlayerOnTurn () {
        return players.get((getNumberOfTurns() + 1) % 2);
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Game restrictInfoOf(int playerIndex) {
        Game res = new Game();
        res.logs = logs;
        res.setNumberOfTurns(getNumberOfTurns());

        for (Player player : players) {
            res.players.add(player);
        }

        res.players.set(playerIndex, players.get(playerIndex).getRestrictedInfo());
        return res;
    }
}
