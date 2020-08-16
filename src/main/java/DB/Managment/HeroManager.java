package DB.Managment;

import DB.HibernateUtil;
import DB.components.heroes.Hero;
import DB.components.heroes.HeroPower;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.awt.*;
import java.util.List;

public class HeroManager {
    private List<Hero> heroes;
    private SessionFactory sessionFactory;

    public static void main(String[] args) throws Exception{
        HeroManager manager = getInstance();
//        Hero mage = new Hero();
//        mage.setHeroName("Mage");
//        mage.setHp(30);
//        mage.setPower(new HeroPower("FirstBlast"));
//
//        Hero Rogue = new Hero();
//        Rogue.setHeroName("Rogue");
//        Rogue.setHp(30);
//        Rogue.setPower(new HeroPower("Poisoned Daggers"));
//
//        Hero Warlock = new Hero();
//        Warlock.setHeroName("Warlock");
//        Warlock.setHp(35);
//        Warlock.setPower(new HeroPower("Life Tap"));
//
//
//        Hero Hunter = new Hero();
//        Hunter.setHeroName("Hunter");
//        Hunter.setHp(30);
//        Hunter.setPower(new HeroPower("Caltrops"));
//
//        Hero Paladin = new Hero();
//        Paladin.setHeroName("Paladin");
//        Paladin.setHp(30);
//        Paladin.setPower(new HeroPower("The silver Hand"));
//
//        manager.addHero(mage);
//        manager.addHero(Rogue);
//        manager.addHero(Warlock);
//        manager.addHero(Hunter);
//        manager.addHero(Paladin);

        System.out.println(manager.heroes);

    }


    public Hero getHero(String name)throws Exception{
        if (name.toLowerCase().equals("neutral") || name.toLowerCase().equals("null"))
            return null;
        for (Hero hero : heroes) {
            if (hero.getHeroName().equals(name))
                return hero;
        }
        throw new Exception("no hero with such name");
    }
    public void addHero(Hero hero) throws Exception{
        for (Hero hero1 : heroes) {
            if (hero1.getHeroName().equals(hero))
                throw new Exception("hero already Exists");
        }
        heroes.add(hero);
        update();
    }
    public void removeHero(Hero hero) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.delete(hero);
        heroes.remove(hero);
        session.getTransaction().commit();
        session.close();
        update();
    }

    public void update() {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        for (Hero hero : heroes) {
            session.saveOrUpdate(hero);
        }
        session.getTransaction().commit();
        session.close();
    }

    public List<Hero> getHeroes() {
        return heroes;
    }

    private static HeroManager instance;
    public static HeroManager getInstance(){
        if (instance == null)
            instance = new HeroManager();
        return instance;
    }
    private HeroManager() {
        sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        Criteria criteria = session.createCriteria(Hero.class);
        heroes = criteria.list();
        session.close();
    }
}
