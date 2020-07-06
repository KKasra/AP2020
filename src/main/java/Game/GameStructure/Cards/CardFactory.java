package Game.GameStructure.Cards;

import Game.CommandAndResponse.GameProcessor;
import Game.GameStructure.CardModels.*;
import Game.GameStructure.Game;
import Game.GameStructure.Player;

import java.lang.reflect.InvocationTargetException;

public class CardFactory {
    public static Card produce(GameProcessor processor, DB.components.cards.Card card, Player player) {
        Card res = null;
        try{
            if (card instanceof DB.components.cards.MinionCard)
                res = new MinionCard((DB.components.cards.MinionCard) card, player);
            if (card instanceof DB.components.cards.WeaponCard)
                res =  new WeaponCard((DB.components.cards.WeaponCard) card, player);
            if (card instanceof DB.components.cards.SpellCard)
                res = new SpellCard((DB.components.cards.SpellCard) card, player);

            res.setCardModel((CardModel) Loader.getInstance()
                    .getModel(res).newInstance(new Object[]{processor, player, res}));
        } catch (ClassNotFoundException e) {
            setDefaultModel(res, processor, player);
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return res;
    }

    private static void setDefaultModel(Card card,GameProcessor processor, Player player){
        if (card instanceof MinionCard)
            card.setCardModel(new MinionModel(processor, player, card) {});
        if (card instanceof WeaponCard)
            card.setCardModel(new WeaponModel(processor, player, card) {
                @Override
                protected int getDurability() {
                    return ((WeaponCard) card).getDurability();
                }
            });
        if (card instanceof SpellCard)
            card.setCardModel(new SpellModel(processor, player, card) {});



    }
}
