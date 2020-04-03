package CLI;

import User.Heroes.HeroData;
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
            user.getLog().writeEvent("store", "show coins in wallet");
            return "";
        }

        if (in.length < 2)
            return "invalid Command!";

        if (in[0].equals("ls")) {
            if (in[1].equals("-b")) {
                System.out.println(Market.getCardsForSale());
                user.getLog().writeEvent("List", "cards in store");
                return "";
            }
            if (in[1].equals("-s")) {
                for (String card : Market.getCardsForSale()) {
                    boolean isContained = false;
                    for (HeroData hero : user.getHeroes())
                        if (hero.getDeck().countCard(card) > 0)
                            isContained = true;
                    if (!isContained)
                        System.out.print(card + ",\n");

                }

                System.out.println();
                user.getLog().writeEvent("List", "cards to sell");
                return "";
            }
        }
        if (in[0].equals("buy")) {
            for (String i : Market.getCardsForSale())
                if (i.toString().equals(in[1])) {

                    if (Market.getCardCost().get(in[1]) <= user.getCoins()) {

                        boolean isClassOpen = false;
                        for (HeroData hero : user.getHeroes())
                            if (hero.getHeroName().equals(Market.getCardClass().get(in[1])))
                                isClassOpen = true;
                        if (isClassOpen == false)
                            return "this card is in a class which currently is not open!";
                        if (user.getAvailableCards().contains(in[1]))
                            return "you already have it!";

                        user.setCoins(user.getCoins() - Market.getCardCost().get(in[1]));
                        user.getAvailableCards().add(i);
                        user.getLog().writeEvent("Trade", "buy");
                        return "";
                    }
                    else
                        return "you dont have enough coins!";
                }
            return "no such card in the market";
        }
        if (in[0].equals("sell")) {
            Market.getCardCost().get(in[1]);
            for (HeroData i : user.getHeroes())
                if (i.getDeck().countCard(in[1]) > 0)
                    return "this card is in your hero's deck";

            for (String i : user.getAvailableCards())
                if (i.toString().equals(in[1])) {
                    user.getAvailableCards().remove(i);
                    user.setCoins(user.getCoins() + Market.getCardCost().get(in[1]));
                    user.getLog().writeEvent("Trade", "sell");
                    return "";
                }
            return "this card is not in your collection";
        }
        return "invalid Command!";
    }

    @Override
    protected void showCommandList() {
        System.out.println("\nStore :" +
                "\nbuy <card name>     | buy a card -if you have enough coins" +
                "\nsell <card name>    | sell a card from your collection and earn coins" +
                "\nls -s               | show all cards that you're alowed to sell" +
                "\nls -b               | show all cards available in Market");
    }
}
