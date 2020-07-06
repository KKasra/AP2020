package Game.CommandAndResponse;


import Game.GameStructure.Cards.Card;

public class PlayCommand extends Command{
    private Card card;
    private int indexOnBoard;
    private String playerName;

    public PlayCommand(String playerName, Card card, int indexOnBoard) {
        this.card = card;
        this.indexOnBoard = indexOnBoard;
        this.playerName = playerName;
    }

    public Card getCard() {
        return card;
    }

    public int getIndexOnBoard() {
        return indexOnBoard;
    }

    public String getPlayerName() {
        return playerName;
    }
}
