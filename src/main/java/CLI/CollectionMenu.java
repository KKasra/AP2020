package CLI;

import Game.Cards.Card;
import Game.Cards.CardFactory;
import Game.Heros.Hero;
import User.User;

import java.util.Set;

public class CollectionMenu extends Menu{
    CollectionMenu(User user) {
        super(user);
    }
    @Override
    protected String run(String command) {
        String[] in = command.split(" ");

        if (in[0].equals("ls")) {
            if (in.length !=  3)
                return "invalid Command!";

            if (in[2].equals("heroes"))
                return runHeroListCommand(in);

            if (in[2].equals("-cards"))
                return runCardsListCommand(in);
        }

        if (in.length < 2)
            return "invalid Command!";

        if (in[0].equals("add")) {
            try {
                user.getHeroes().get(user.getIndexOfHero()).getDeck().addCard(CardFactory.build(in[1]));
            } catch (Exception e) {
                return e.toString();
            }
        }
        if (in[0].equals("remove"))
            user.getHeroes().get(user.getIndexOfHero()).getDeck().removeCard(in[1]);

        return "invalid Command!";
    }


    private String runHeroListCommand(String[] in) {
        if (in[1].equals("-a")) {
            for (Hero i : user.getHeroes())
                System.out.println(i);
            return "";
        }
        if (in[1].equals("-m")) {
            System.out.println(user.getHeroes().get(user.getIndexOfHero()));
            return "";
        }
        return "invalid Command!";
    }
    private String runCardsListCommand(String[] in) {
        if (in[1].equals("-m")) {
            for (Card i : user.getHeroes().get(user.getIndexOfHero()).getDeck().getCards())
                System.out.print(i + " ");
            System.out.println();
            return "";
        }
        if (in[1].equals("-a")) {
            for (Card i : user.getAvailableCards())
                System.out.print(i + " ");
            System.out.println();
            System.out.print("Hero Cards :");
            for (Card i : user.getHeroes().get(user.getIndexOfHero()).getSpecialCards())
                System.out.print(i + " ");
            return "";
        }
        if (in[1].equals("-n")) {
            Set<Card> res = (Set<Card>)user.getAvailableCards().clone();
            res.addAll(user.getHeroes().get(user.getIndexOfHero()).getSpecialCards());
            while (true) {
                boolean erased = false;
                for (Card i : res)
                    if (user.getHeroes().get(user.getIndexOfHero()).getDeck().countCard(i) != 0) {
                        res.remove(i);
                        erased = true;
                        break;
                    }
                if (!erased)
                    break;
            }
            for (Card i : res)
                System.out.print(i + " ");
            return "";

        }
        return "invalid Command!";
    }
}
