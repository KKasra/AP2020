package Game.Gamestructure;

import Game.Cards.Card;
import Game.Cards.Deck;
import Game.Cards.MinionCard;
import Game.Cards.WeaponCard;
import Game.Heros.Hero;

import java.util.ArrayList;

public class Player {

    private Board board;
    private Hero myHero;
    private Deck deck;
    private ArrayList<Card> hand;
    private ArrayList<MinionCard> minions;
    private WeaponCard weaponCard;
    public Board getBoard() {
        return board;
    }
    public Player (Board board, Hero GameHero, Deck deck) {
        this.board = board;
        myHero = GameHero;
        this.deck = deck;
        hand = new ArrayList<Card>();
        minions = new ArrayList<MinionCard>();
        weaponCard = null;
    }



}
