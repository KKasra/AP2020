package Game.GameStructure.CardModels;

import Game.CommandAndResponse.Command;
import Game.CommandAndResponse.GameProcessor;
import Game.CommandAndResponse.PlayCommand;
import Game.GameStructure.Cards.Card;
import Game.GameStructure.Player;

public abstract class CardModel {
    protected GameProcessor processor;
    protected Card card;
    protected Player player;

    public void observeBefore(Command command)throws Exception{}
    public void observeAfter(Command command){}

    public void play(PlayCommand command) throws Exception {
        processor.getObservers().add(this);
    }

    public CardModel(GameProcessor processor, Player player, Card card) {
        this.processor = processor;
        this.card = card;
        this.player = player;
    }
    public void die() {
        processor.getObservers().remove(this);
    }

    public Card getCard() {
        return card;
    }
}
