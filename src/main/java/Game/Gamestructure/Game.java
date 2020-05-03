package Game.Gamestructure;

import DB.Managment.HeroManager;
import DB.components.User;
import DB.components.cards.*;
import DB.components.heroes.Hero;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game {
    public static class Player{
        private Game game;
        private List<Card> deck;
        private List<Card> hand;
        private List<Card> cardsOnBoard;
        private Hero hero;
        private WeaponCard weaponCard;
        private HeroPower power;
        private int mana;
        private Passive passive;
        public Player(Game game, Deck deck) throws Exception{
            this.game = game;
            this.deck = new ArrayList<Card>();
            for (Card card : deck.getCards()) {
                this.deck.add(card);
                //TODO cards should be cloned
            }
            hand = new ArrayList<Card>();
            cardsOnBoard = new ArrayList<Card>();
            hero = deck.getHero();
            power = new HeroPower();
        }

        public void drawCard(Card card) throws Exception{
            if (!hand.contains(card))
                throw new Exception("card is not in Player's hand");
            if (mana < card.getManaCost())
                throw new Exception("insufficient mana");
            hand.remove(card);
            if (card instanceof MinionCard)
                cardsOnBoard.add(card);
            mana -= card.getManaCost();
            game.logs.add("card: " + card.getName() + ">>");

        }

        public Card drawFromDeck() throws Exception {
            Card card = deck.get(new Random().nextInt(deck.size()));
            deck.remove(card);
            return card;
        }

        int turnCnt = 0;
        //TODO turnCnt should be managed by Game Object
        public void newTurn() {
            mana = ++turnCnt;

            if (turnCnt == 1) {
                ArrayList<Card> initialHand = new ArrayList<>();
                for (Card card : deck) {
                    if (card instanceof QuestAndRewardCard)
                        initialHand.add(card);
                }
                while (initialHand.size() < 3) {
                    Card card = deck.get(new Random().nextInt(deck.size()));
                    try {
                        initialHand.add(drawFromDeck());
                    } catch (Exception exception) {
                        exception.printStackTrace();
                        break;
                    }
                }

                for (Card card : initialHand) {
                    hand.add(card);
                }
                return;
            }
            try {
                hand.add(drawFromDeck());
            } catch (Exception exception) {
                exception.printStackTrace();
            }

        }

        public List<Card> getDeck() {
            return deck;
        }

        public void setDeck(List<Card> deck) {
            this.deck = deck;
        }

        public List<Card> getHand() {
            return hand;
        }

        public void setHand(List<Card> hand) {
            this.hand = hand;
        }

        public List<Card> getCardsOnBoard() {
            return cardsOnBoard;
        }

        public void setCardsOnBoard(List<Card> cardsOnBoard) {
            this.cardsOnBoard = cardsOnBoard;
        }

        public Hero getHero() {
            return hero;
        }

        public void setHero(Hero hero) {
            this.hero = hero;
        }

        public WeaponCard getWeaponCard() {
            return weaponCard;
        }

        public void setWeaponCard(WeaponCard weaponCard) {
            this.weaponCard = weaponCard;
        }

        public HeroPower getPower() {
            return power;
        }

        public void setPower(HeroPower power) {
            this.power = power;
        }

        public int getMana() {
            return mana;
        }

        public void setMana(int mana) {
            this.mana = mana;
        }

        public Game getGame() {
            return game;
        }

        public void setGame(Game game) {
            this.game = game;
        }

        public Passive getPassive() {
            return passive;
        }

        public void setPassive(Passive passive) {
            this.passive = passive;
        }

        public int getTurnCnt() {
            return turnCnt;
        }

        public void setTurnCnt(int turnCnt) {
            this.turnCnt = turnCnt;
        }
    }
    private Player player;
    private Player opponent;
    List<String> logs;
    public Game() throws Exception{
        player = new Player(this, User.user.getCurrentDeck());
        //TODO sufficiency of cards must be checked in Player constructor
        if (player.getDeck().size() < 12)
            throw new Exception("your deck is short of cards.");
        opponent = new Player(this, new Deck("opponent", HeroManager.getInstance().getHero("Warlock")));
        logs = new ArrayList<String>();

    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getOpponent() {
        return opponent;
    }

    public void setOpponent(Player opponent) {
        this.opponent = opponent;
    }

    public List<String> getLogs() {
        return logs;
    }

    public void setLogs(List<String> logs) {
        this.logs = logs;
    }


}
