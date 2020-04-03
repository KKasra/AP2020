package Game.Gamestructure;

import Game.Cards.Card;
import Game.Cards.CardFactory;
import Game.Cards.SpellCard;
import User.Heroes.HeroData;

import java.util.ArrayList;

public class HeroFactory {
    public static Hero build(HeroData data){
        Hero res = new Hero();
        res.setHeroName(data.getHeroName());
        res.setDeck(new ArrayList<Card>());
        for (String i : data.getDeck().getCards())
            try {
                res.getDeck().add(CardFactory.build(res, i));
            } catch (Exception e) {
                //TODO
            }

        res.setHp(data.getHp());
        res.setPower(HeroPowerFactory.build(data.getPower(), res));
        switch (res.getHeroName()) {
            case "Mage":
                for (Card i : res.getDeck())
                    if (i instanceof SpellCard)
                        i.setManaCost(i.getManaCost() - 2);

            case "Rogue" :
                for (Card i : res.getDeck())
                    if (i.getCardClass().equals("natural") && i.getCardClass().equals("Rogue"));
                    else
                        i.setManaCost(i.getManaCost() - 2);


        }
        return res;
    }
}
