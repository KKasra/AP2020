package CLI;

import Game.Cards.Card;
import Game.Cards.CardFactory;
import Game.Heroes.HeroData;
import User.User;

import java.util.ArrayList;
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

            if (in[2].equals("-heroes"))
                return runHeroListCommand(in);

            if (in[2].equals("-cards"))
                return runCardsListCommand(in);
        }

        if (in.length < 2)
            return "invalid Command!";

        if (in[0].equals("add")) {
            try {
                if (user.getAvailableCards().contains(command.substring("add ".length())))
                    user.getHeroes().get(user.getIndexOfHero()).getDeck().addCard(command.substring("add ".length()));
                else
                    return "you don't own this card";
                return "";
            } catch (Exception e) {
                return e.toString();
            }
        }
        if (in[0].equals("remove")) {
            user.getHeroes().get(user.getIndexOfHero()).getDeck().removeCard(command.substring("remove ".length()));
            return "";
        }
        return "invalid Command!";
    }

    @Override
    protected void showCommandList() {
        System.out.println("\nCollection Menu :" +
                           "\nls -a -heroes       | show open heroes" +
                           "\nls -m -heroes       | show your chosen hero" +
                           "\nselect hero         | select a hero" +
                           "\nls -a -cards        | show all available cards" +
                           "\nls -m -cards        | show cards in your hero's deck" +
                           "\nls -n cards         | show available cards out of your deck" +
                           "\nadd <card name>     | add a card to your deck -if possible-" +
                           "\nremove <card name>  | remove a card from your deck");
    }


    private String runHeroListCommand(String[] in) {
        if (in[1].equals("-a")) {
            for (HeroData i : user.getHeroes())
                System.out.println(i);

            user.getLog().writeEvent("List", "Heroes");
            return "";
        }
        if (in[1].equals("-m")) {
            System.out.println(user.getHeroes().get(user.getIndexOfHero()));

            user.getLog().writeEvent("List", "chosen hero");
            return "";
        }
        return "invalid Command!";
    }
    private String runCardsListCommand(String[] in) {
        if (in[1].equals("-m")) {
            for (String i : user.getHeroes().get(user.getIndexOfHero()).getDeck().getCards())
                System.out.print(i + ",\n");
            System.out.println();

            user.getLog().writeEvent("List", "cards in deck");
            return "";
        }
        if (in[1].equals("-a")) {
            for (String i : user.getAvailableCards())
                System.out.print(i + ",\n");
            System.out.println();

            user.getLog().writeEvent("List", "cards in collection");
            return "";
        }
        if (in[1].equals("-n")) {
            ArrayList<String> res = new ArrayList<>(user.getAvailableCards());
            while (true) {
                boolean erased = false;
                for (String i : res)
                    if (user.getHeroes().get(user.getIndexOfHero()).getDeck().countCard(i) != 0) {
                        res.remove(i);
                        erased = true;
                        break;
                    }
                if (!erased)
                    break;
            }
            for (String i : res)
                System.out.print(i + ",\n");

            user.getLog().writeEvent("List", "cards out of deck");
            return "";

        }
        return "invalid Command!";
    }
}
