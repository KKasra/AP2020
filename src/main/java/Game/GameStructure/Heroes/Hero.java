package Game.GameStructure.Heroes;


import DB.components.cards.Card;
import Game.CommandAndResponse.GameProcessor;
import Game.CommandAndResponse.PlayCommand;
import Game.GameStructure.Attackable;
import Game.GameStructure.CardModels.Loader;
import Game.GameStructure.CardModels.HeroPower;
import Game.GameStructure.CardModels.SpecialPower;
import Game.GameStructure.Cards.WeaponCard;
import Game.GameStructure.Player;

import java.io.Serializable;
import java.lang.reflect.Constructor;

public class Hero implements Attackable, Serializable {
    transient GameProcessor gameProcessor;
    private DB.components.heroes.Hero heroData;
    private String HeroName;
    transient private HeroPower power;
    transient private SpecialPower specialPower;
    transient private WeaponCard weapon;
    transient private Player player;
    private int hp;

    public Hero(Player player, DB.components.heroes.Hero heroData, GameProcessor processor) {
        this.heroData = heroData;
        this.gameProcessor = processor;
        this.player = player;
        this.HeroName = heroData.getHeroName();
        initPower();
        this.hp = heroData.getHp();

        weapon = null;
    }


    private void initPower() {
        try {
            Card cardData = new Card();
            cardData.setName(getHeroName() + "PowerModel");
            Game.GameStructure.Cards.Card card = new Game.GameStructure.Cards.Card(cardData, player) {};
            Constructor constructor = Loader.getInstance().getModel(card);
            power = (HeroPower) constructor.newInstance(gameProcessor, player, card);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void initSpecialPower() {
        try {
            Card cardData = new Card();
            cardData.setName(getHeroName() + "SpecialPowerModel");
            Game.GameStructure.Cards.Card card = new Game.GameStructure.Cards.Card(cardData, player) {};
            Constructor constructor = Loader.getInstance().getModel(card);
            specialPower = (SpecialPower) constructor.newInstance(gameProcessor, player, card);
            specialPower.play(new PlayCommand(player.getName(), -1, 0));
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getHeroName() {
        return HeroName;
    }

    public HeroPower getPower() {
        return power;
    }

    public int getHp() {
        return hp;
    }


    @Override
    public void receiveDamage(int amount) {
        hp -= amount;
        if (hp <= 0)
            die();
    }

    @Override
    public void die() {
        //TODO not an accurate approach to find the winner
        gameProcessor.finishGame(gameProcessor.getGame().getPlayerOnTurn());
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    public WeaponCard getWeapon() {
        return weapon;
    }

    public void setWeaponCard(WeaponCard weapon) {
        this.weapon = weapon;
    }

    public DB.components.heroes.Hero getHeroData() {
        return heroData;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }
}
