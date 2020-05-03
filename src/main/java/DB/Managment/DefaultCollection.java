package DB.Managment;

import DB.HibernateUtil;
import DB.UserDB;
import DB.components.User;
import DB.components.cards.Card;
import DB.components.cards.Deck;
import DB.components.heroes.Hero;
import org.hibernate.Criteria;
import org.hibernate.Session;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class DefaultCollection {
    private static User user;

    private static CardManager cardManager = CardManager.getInstance();
    private static HeroManager heroManager = HeroManager.getInstance();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while(true) {
            String[] input = scanner.nextLine().toLowerCase().split(" ");
            try{
                exec(scanner, input);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    private static void exec(Scanner scanner, String[] input) throws Exception{
        if (input[1].equals("coins"))
            coinsCommand(scanner, input);
        if (input[1].equals("card"))
            cardCommand(scanner, input);
        if (input[1].equals("hero"))
            heroCommand(scanner, input);
        if (input[1].equals("deck"))
            deckCommand(scanner, input);
        UserDB.saveChanges(user);
    }

    private static void coinsCommand(Scanner scanner, String[] input) throws Exception{
        if (input[0].equals("set"))
            user.setCoins(Integer.valueOf(scanner.nextLine()));
        else
            System.out.println(user.getCoins());

    }

    private static void heroCommand(Scanner scanner, String[] input) throws Exception{
        System.out.println("name: ");

        Hero hero = heroManager.getHero(scanner.nextLine());

        if (input[0].equals("add")) {
            System.out.println(hero);
            System.out.println("add Hero? Y/N");
            if (scanner.nextLine().toLowerCase().equals("y"))
                user.getHeroes().add(hero);
        }
        if (input[0].equals("remove")) {
            System.out.println(hero);
            System.out.println("remove card? Y/N");
            if (scanner.nextLine().toLowerCase().equals("y"))
                user.getHeroes().remove(hero);
        }

    }
    private static void cardCommand(Scanner scanner, String[] input) throws Exception{
        if (input[0].equals("ls")) {
            System.out.println(user.getAvailableCards());
            return;
        }
        System.out.println("name: ");

        Card card = cardManager.getCard(scanner.nextLine());

        if (input[0].equals("add")) {
            System.out.println(card);
            System.out.println("add card? Y/N");
            if (scanner.nextLine().toLowerCase().equals("y")) {
                user.getCurrentDeck().addCard(card);
                user.getAvailableCards().add(card);
            }
        }
        if (input[0].equals("remove")) {
            System.out.println(card);
            System.out.println("remove card? Y/N");
            if (scanner.nextLine().toLowerCase().equals("y")) {
                user.getCurrentDeck().removeCard(card);
                user.getAvailableCards().remove(card);
            }
        }

    }
    private static void deckCommand(Scanner scanner, String[] input) throws Exception{


        Deck deck = null;

        if (input[0].equals("add")) {

            System.out.println("name: ");
            String name = scanner.nextLine();

            System.out.println("Hero: ");
            Hero hero = heroManager.getHero(scanner.nextLine());

            deck = new Deck(name, hero);

            System.out.println(deck);
            System.out.println("add Deck? Y/N");
            if (scanner.nextLine().toLowerCase().equals("y"))
                user.getDecks().add(deck);
        }
        if (input[0].equals("remove")) {
            String name = scanner.nextLine();
            for (Deck userDeck : user.getDecks()) {
                if (userDeck.getName().equals(name))
                    deck = userDeck;
            }
            if (deck == null)
                return;
            System.out.println(deck);
            System.out.println("remove deck? Y/N");
            if (scanner.nextLine().toLowerCase().equals("y")) {
                user.getDecks().remove(deck);
            }

        }
        if (input[0].equals("choose")) {
            System.out.println(user.getDecks());
            System.out.println("index :");
            int index = Integer.valueOf(scanner.nextLine());
            user.setCurrentDeck(index);
        }
    }



    static {
        /*/
        Session session = HibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(User.class);
        session.beginTransaction();
        for (Object o : criteria.list()) {
            session.delete(o);
        }
        session.getTransaction().commit();
        session.close();
        //*/
        try {
            user = UserDB.getUser("##Default", "");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("!");
            user = new User();
            user.setUserName("##Default");
            user.setPassWord("");
            user.setDecks(new ArrayList<Deck>());
            user.setCurrentDeck(0);
            user.setAvailableCards(new ArrayList<Card>());
            user.setHeroes(new ArrayList<Hero>());
            UserDB.saveChanges(user);
        }

    }

    public static User getUser() {
        return user;
    }
}
