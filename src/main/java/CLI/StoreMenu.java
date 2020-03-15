package CLI;

import Game.Cards.Card;
import Game.Cards.CardFactory;
import Game.Heros.Hero;
import Market.Market;
import User.User;

public class StoreMenu extends Menu {
    StoreMenu(User user) {
        super(user);
    }
    @Override
    protected String run(String command) {
        String[] in = command.split(" ");
        if (in[0].equals("wallet")) {
            System.out.println(user.getCoins());
            return "";
        }

        if (in.length < 2)
            return "invalid Command!";

        if (in[0].equals("buy")) {
            for (Card i : Market.getCardsForSale())
                if (i.toString().equals(in[1])) {
                    if (i.getCoinCost() <= user.getCoins()) {
                        user.setCoins(user.getCoins() - i.getCoinCost());
                        try {
                            user.getAvailableCards().add(CardFactory.build(i.toString()));
                        } catch (Exception e) {}
                        return "";
                    }
                    else
                        return "you dont have enough coins!";
                }
            return "no such card in the market";
        }
        if (in[0].equals("sell")) {
            Card card;
            try {
                card = CardFactory.build(in[1]);
            }catch (Exception e) {
                return e.toString();
            }

            for (Hero i : user.getHeroes())
                if (i.getDeck().countCard(card) > 0)
                    return "this card is in your hero's deck";

            for (Card i : user.getAvailableCards())
                if (i.toString().equals(in[1])) {
                    user.getAvailableCards().remove(i);
                    user.setCoins(user.getCoins() + i.getCoinCost());
                    return "";
                }
            return "this card is not in your collection";
        }
        return "invalid Command!";
    }
}
