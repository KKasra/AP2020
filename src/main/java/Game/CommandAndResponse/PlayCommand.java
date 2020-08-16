package Game.CommandAndResponse;


import Game.GameStructure.Cards.Card;

public class PlayCommand extends Command{
    private int indexInHand;
    private int indexOnBoard;
    private String playerName;
    private Card card;

    public PlayCommand(String playerName, int indexInHand, int indexOnBoard) {
        this.indexInHand = indexInHand;
        this.indexOnBoard = indexOnBoard;
        this.playerName = playerName;
    }

    public int getIndexInHand() {
        return indexInHand;
    }

    public int getIndexOnBoard() {
        return indexOnBoard;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setCard(Card card) {
        this.card = card;
    }
    public Card getCard() {
        return card;
    }
}
