package Game.Gamestructure;


import Game.Cards.Card;
import Game.Cards.WeaponCard;

import java.util.ArrayList;
import java.util.Random;

public class Hero implements Attackable{
    private Player player;
    private String HeroName;
    private ArrayList<Card> deck;
    private ArrayList<Card> hand;
    private WeaponCard weapon;
    private HeroPower power;
    private int hp;

    public void usePower()throws Exception {
        power.usePower();
    }

    public void useWeapon() throws Exception{
        weapon.attack();
    }

    public void shuffleInDeck(Card card) {

        int place = new Random().nextInt();
        place %= deck.size();
        getDeck().add(place, card);
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public String getHeroName() {
        return HeroName;
    }

    public void setHeroName(String heroName) {
        HeroName = heroName;
    }

    public ArrayList<Card> getDeck() {
        return deck;
    }

    public void setDeck(ArrayList<Card> deck) {
        this.deck = deck;
    }

    public ArrayList<Card> getHand() {
        return hand;
    }

    public WeaponCard getWeapon() {
        return weapon;
    }

    public void setWeapon(WeaponCard weapon) {
        this.weapon = weapon;
    }

    public HeroPower getPower() {
        return power;
    }

    public void setPower(HeroPower power) {
        this.power = power;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    @Override
    public void receiveDamage(int damage) {
        hp -= damage;
    }
}
