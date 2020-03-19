package Game.Cards;

import Game.Gamestructure.Attackable;
import Game.Gamestructure.Hero;
import Game.Gamestructure.Player;
import Market.Market;

import java.util.Random;

import static Game.Cards.MinionType.*;
import static Game.Cards.Rarity.*;

public class CardFactory {
    //TODO builder pattern instead of invoking constructor
    public static Card build(Hero hero, String cardName) throws Exception{
        Card res = null;
        switch (cardName) {

            //minion cards
                //neutral
            case "Goldshire Footman":
                res = new MinionCard(hero, cardName, "Taunt", null, Free, 1, 2, 1);
                ((MinionCard)res).setTaunt(true);
                break;
            case "Murloc Raider":
                res = new MinionCard(hero, cardName, "", Murloc, Free, 2, 1, 1);
                break;
            case "Stonetusk Boar":
                res = new MinionCard(hero, cardName, "Charge", Beast, Free, 1, 1, 1);
                ((MinionCard)res).setCharge(true);
                break;
            case "Target dummy":
                res = new MinionCard(hero, cardName, "Taunt", Mech, Rare, 0, 2, 0);
                break;
            case "FrostWolf Grunt":
                res = new MinionCard(hero, cardName, "Taunt", null, Free, 2, 2, 2);
                ((MinionCard)res).setTaunt(true);
                break;
            case "shieldbearer":
                res = new MinionCard(hero, cardName, "Taunt", null, Common, 0, 4, 1);
                ((MinionCard)res).setTaunt(true);
                break;
            case "Doomsayer":
                res = new MinionCard(hero, cardName, "At the start of your turn, destroy ALL minions.", null, Epic, 0, 7, 2){
                    @Override
                    public void play() throws Exception {
                        super.play();
                        for (Player player : getBoard().getPlayers())
                            for (Card card : player.getMyHero().getDeck())
                                if (card instanceof MinionCard)
                                    ((MinionCard) card).receiveDamage(((MinionCard) card).getArmor());
                    }
                };

                //Mage
            case "Manic Soulcaster":
                res = new MinionCard(hero, cardName, "Battlecry: Choose a friendly minion. Shuffle a copy into your deck.", null, Epic, 3, 4, 3){
                    @Override
                    public void play() throws Exception {
                        super.play();
                        Object target = getPlayer().getUserInterface().read();
                        if (target instanceof MinionCard)
                        if (((MinionCard) target).getHero() == hero){
                            getHero().shuffleInDeck(CardFactory.build(hero, ((MinionCard) target).getName()));
                            return;
                        }
                        throw new Exception("not that one!");
                    }
                };
                res.setClass("Mage");
                break;
            case "Arcane Amplifier" :
                res = new MinionCard(hero, cardName, "Your Hero Power deals 2 extra damage.", Elemental, Common, 2, 5, 3){
                  @Override
                  public void play() throws Exception{
                      super.play();
                      getHero().getPower().setDamage(getHero().getPower().getDamage() + 2);
                  }
                };
                res.setClass("Mage");
                break;
                //Rogue
            case "Blade of C'Thun":
                res = new MinionCard(hero, cardName, "Battlecry: Destroy a minion. Add its Attack and Health to your C'Thun's (wherever it is).", null, Epic, 4, 4, 9){
                    @Override
                    public void play() throws Exception{
                        super.play();
                        Object target = getPlayer().getUserInterface().read();
                        if (target instanceof MinionCard){
                            MinionCard card = (MinionCard)target;
                            this.setArmor( this.getArmor() + card.getArmor());
                            this.setAttack(this.getAttack() + card.getAttack());
                            card.receiveDamage(card.getArmor());
                        }
                        else
                            throw new Exception("choose a minion!");
                    }
                };
                res.setClass("Rogue");
                break;
            case "Lab recruiter":
                res = new MinionCard(hero, cardName, "Battlecry: Shuffle 3 copies of a friendly minion into your deck.", null, Common, 3, 2, 2){
                    @Override
                    public void play() throws Exception {
                        super.play();
                        Object target = getPlayer().getUserInterface().read();
                        if (target instanceof MinionCard) {
                            MinionCard card = (MinionCard)target;
                            if (card.getHero() != hero)
                                throw new Exception("Choose a friendly");
                            for (int i = 0; i < 3; ++i)
                                getHero().shuffleInDeck(CardFactory.build(hero, ((MinionCard) target).getName()));
                            return;
                        }
                        throw new Exception("not that one!");
                    }
                };
                res.setClass("Rogue");
                break;
                //Warlock
            case "Voidwalker":
                res = new MinionCard(hero, cardName, "Taunt", Demon, Free, 1, 3, 1);
                ((MinionCard)res).setTaunt(true);
                res.setClass("Warlock");
                break;
            case "Aranasi Broodmother":
                res = new MinionCard(hero, cardName, "Taunt\nWhen you draw this, restore 4 Health to your hero.", Demon, Common, 4, 6, 6){
                    @Override
                    public void play() throws Exception {
                        super.play();
                        getHero().setHp(getHero().getHp() + 4);
                    }
                };
                res.setClass("Warlock");
                break;
            //Spell cards
            case "Chaos Nova":
                res = new SpellCard(hero, cardName, Free, 5, "Deal 4 damage to all minions.") {
                    @Override
                    public void play() throws Exception{
                        super.play();
                        for (Player player: getBoard().getPlayers())
                            for (Card card : player.getMyHero().getHand())
                                if (card instanceof MinionCard)
                                    ((MinionCard)card).setArmor(((MinionCard)card).getArmor() - 4);
                    }
                };
                break;
            case "Starfire" :
                res = new SpellCard(hero, cardName, Free, 6, "Deal 5 damage, draw a card.") {
                    @Override
                    public void play() throws Exception{
                        super.play();
                        Object target = getPlayer().getUserInterface().read();
                        if (target instanceof Attackable)
                            ((Attackable)target).receiveDamage(5);
                        else
                            throw new Exception("not that one!");

                        getPlayer().drawACard();
                    }
                };
                break;
            case "Gift of the Wild":
                res = new SpellCard(hero, cardName, Common, 8, "Give your minions +2/+2 and Taunt") {
                    @Override
                    public void play() throws Exception {
                        super.play();
                        for (Card card : getHero().getHand())
                            if (card instanceof MinionCard) {
                                MinionCard now = ((MinionCard)card);
                                now.setArmor(now.getArmor() + 2);
                                now.setAttack(now.getAttack() + 2);
                                now.setTaunt(true);
                            }

                    }
                };
                break;
            case "Hunter's mark":
                res = new SpellCard(hero, cardName, Free, 2, "Change a minion's Health to 1.") {
                    @Override
                    public void play() throws Exception {
                        super.play();
                        Object target = getPlayer().getUserInterface().read();
                        if (target instanceof MinionCard) {
                            ((MinionCard)target).setArmor(1);
                        }
                        else
                            throw new Exception("choose a minion!");
                    }
                };
                break;
            case "Arcane Explosion":
                res = new SpellCard(hero, cardName, Free, 2, "Deal 1 damage to all enemy minions.") {
                    @Override
                    public void play() throws Exception {
                        super.play();
                        for (Player player : getBoard().getPlayers())
                            if (getPlayer().isMyEnemy(player)) {
                                for (Card card : player.getMyHero().getHand())
                                    if (card instanceof MinionCard) {
                                        ((MinionCard)card).setArmor(((MinionCard)card).getArmor() - 1);
                                    }
                            }
                    }
                };
                break;
            case "Mark of the wild":
                res = new SpellCard(hero, cardName, Free, 2, "Give a minion Taunt and +2/+2. (+2 Attack/+2 Health)") {
                    @Override
                    public void play() throws Exception {
                        super.play();
                        Object target = getPlayer().getUserInterface().read();
                        if (target instanceof MinionCard)
                            if (((MinionCard) target).getHero() == hero) {
                                ((MinionCard) target).setArmor(((MinionCard) target).getArmor() + 2);
                                ((MinionCard) target).setAttack(((MinionCard) target).getAttack() + 2);
                                ((MinionCard) target).setTaunt(true);
                                return;
                            }
                        throw new Exception("not that one!");
                    }
                };
            case "Healing Touch":
                res = new SpellCard(hero, cardName, Free, 3, "Restore 8 Health.") {
                    @Override
                    public void play() throws Exception {
                        super.play();
                        getHero().setHp(getHero().getHp() + 8);
                    }
                };
            //Weapons
                //Rogue
            case "Assassin's Blade":
                res = new WeaponCard(hero, cardName, Free, 5, 3, 4, "");
                res.setClass("Rogue");
                break;


        }
        if (res == null)
            throw new Exception("invalid card name :|");
        return res;
    }
}
