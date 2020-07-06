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
    public Game() throws Exception{
        logs = new ArrayList<String>();

    }

    public List<String> getLogs() {
        return logs;
    }
    public void setLogs(List<String> logs) {
        this.logs = logs;
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
}
