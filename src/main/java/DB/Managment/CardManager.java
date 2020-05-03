package DB.Managment;


import DB.HibernateUtil;
import DB.components.cards.*;
import DB.components.cards.Rarity;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;



import java.util.List;
import java.util.Scanner;

public class CardManager {
    private SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
    private List<Card> cards;

    public List<Card> getCards() {
        return cards;
    }

    public static void main(String[] args) {
        CardManager manager = getInstance();
        Scanner scanner = new Scanner(System.in);

        while(true) {
            String input = scanner.nextLine().toLowerCase();

            if (input.equals("exit")) {
                System.exit(0);
            }
            if (input.equals("add")) {
                Card res = manager.getConfig(scanner);
                System.out.println("add card ? Y/N");
                System.out.println(res);

                if (scanner.nextLine().toLowerCase().equals("y")) {
                    try {
                        manager.addCard(res);
                        System.out.println("card: " + res + "added.");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                else
                    System.out.println("canceled.");
                continue;
            }
            if (input.equals("remove")) {
                try{
                    Card target = manager.getCard(scanner.nextLine());
                    System.out.println(target);
                    System.out.println("removeCard ? Y/N");
                    if (scanner.nextLine().toLowerCase().equals("y")) {
                        manager.removeCard(target);
                        System.out.println("card: " + target + "removed.");
                    }
                    else
                        System.out.println("canceled.");
                }catch (Exception e){
                    e.printStackTrace();
                }
                continue;
            }
            if(input.equals("ls"))
                manager.printList();

        }
    }
    private void printList(){
        int index = 0;
        for (Card card : cards) {
            System.out.print(++index + ". ");
            System.out.println(card);
        }

    }
    private Card getConfig(Scanner scanner){
        Card res;

        System.out.println("Type: ");
        String input = scanner.nextLine().toLowerCase();
        switch (input){
            case "minion":
                res = new MinionCard();
                break;
            case "spell":
                res = new SpellCard();
                break;
            case "weapon":
                res = new WeaponCard();
                break;
            case "quest":
                res = new QuestAndRewardCard();
                break;
            default:
                res = new Card();
        }

        System.out.println("name: ");
        res.setName(scanner.nextLine());

        System.out.println("Coin Cost: ");
        res.setCoinCost(Integer.valueOf(scanner.nextLine()));
        
        System.out.println("Description: ");
        res.setDescription(scanner.nextLine());

        System.out.println("Mana Cost: ");
        res.setManaCost(Integer.valueOf(scanner.nextLine()));

        System.out.println("Rarity: ");
        while (true)
            try {
                res.setRarity(Rarity.valueOf(scanner.nextLine()));
                break;
            }catch (Exception e) {
                e.printStackTrace();
                continue;
            }

        System.out.println("Class: ");
        while (true)
            try {
                res.setHero(HeroManager.getInstance().getHero(scanner.nextLine()));
                break;

            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }



        if (res instanceof MinionCard){
            System.out.println("MinionType: ");
            String type = scanner.nextLine();
            if (type.toLowerCase().equals("null"))
                ((MinionCard) res).setMinionType(null);
            else
                ((MinionCard) res).setMinionType(MinionType.valueOf(type));

            System.out.println("Attack: ");
            ((MinionCard) res).setAttack(Integer.valueOf(scanner.nextLine()));

            System.out.println("Health: ");
            ((MinionCard) res).setHealth(Integer.valueOf(scanner.nextLine()));
        }
        if (res instanceof SpellCard) {
            if (res instanceof QuestAndRewardCard){
            }
        }
        if (res instanceof WeaponCard) {
            System.out.println("Attack: ");
            ((WeaponCard) res).setAttack(Integer.valueOf(scanner.nextLine()));

            System.out.println("Durability");
            ((WeaponCard) res).setHealth(Integer.valueOf(scanner.nextLine()));
        }
        return res;
    }


    public void removeCard(Card card) {
        cards.remove(card);
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.delete(card);
        session.getTransaction().commit();
        session.close();
    }
    public Card getCard(String name) throws Exception{
        for (Card card : cards) {
            if (card.getName().equals(name))
                return card;
        }
        throw new Exception("no cards with given name in DB");
    }
    public  void addCard(Card card) throws Exception{
        System.out.println("!");
        try{
            getCard(card.getName());
            System.out.println(">");
            throw new Exception("given card name already token");
        }catch (Exception E) {
            System.out.println("?");
            cards.add(card);
            update();
        }
    }
    private  void update() {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        for (Card card : cards) {
            session.saveOrUpdate(card);
        }
        session.getTransaction().commit();
        session.close();
    }

    private static CardManager instance;
    public static CardManager getInstance(){
        if (instance == null)
            instance = new CardManager();
        return instance;
    }
    private CardManager() {
        Session session =  sessionFactory.openSession();
        Criteria criteria = session.createCriteria(Card.class);
        cards = criteria.list();
        session.close();
    }
}
