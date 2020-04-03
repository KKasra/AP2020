package Game.Gamestructure;

import Game.Cards.Card;
import User.Heroes.HeroData;

import java.util.ArrayList;

public class Player {

    private UserInterface userInterface;
    private Board board;
    private Hero myHero;
    private int mana;
    private ArrayList<Card> hand;
    public Board getBoard() {
        return board;
    }
    public Player (Board board, HeroData GameHero, UserInterface userInterface) {
        this.userInterface = userInterface;
        this.board = board;
        myHero = HeroFactory.build(GameHero);
    }

    public UserInterface getUserInterface() {
        return userInterface;
    }


    public void setUserInterface(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    public Hero getMyHero() {
        return myHero;
    }

    public void setMyHero(Hero myHero) {
        this.myHero = myHero;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public int getMana() {
        return mana;
    }

    public void setMana(int mana) {
        this.mana = mana;
    }
    public void drawACard() {
        //TODO
    }

    public boolean isMyEnemy(Player player) {
        //TODO
        if (player != this)
            return true;
        return false;
    }

}
