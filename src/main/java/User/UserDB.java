package User;

import User.Heroes.HeroData;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.sql.Timestamp;
import java.util.ArrayList;

public class UserDB {
    private static ArrayList<User> users;
    private static SessionFactory sessionFactory;
    static {
        StandardServiceRegistry sr = new StandardServiceRegistryBuilder().configure().build();
        sessionFactory = new MetadataSources(sr).buildMetadata().buildSessionFactory();

        Session session = sessionFactory.openSession();
        Criteria criteria = session.createCriteria(User.class);
        users = (ArrayList<User>) criteria.list();
        session.close();

        System.err.println(users);
    }

    public static void addUser(User user) throws Exception{
        for (User i : users)
            if (i.getUserName().equals(user.getUserName()))
                throw new Exception("username already taken");

        saveChanges(user);
        System.err.println("##");
        user.initLog(false);
        user.getLog().write("USER: " + user.getUserName());
        user.getLog().write("CREATED_AT: " + new Timestamp(System.currentTimeMillis()));
        user.getLog().write("PASSWORD: " + user.getPassWord());
        user.getLog().write("");
    }
    public static User getUser(String username, String password) throws Exception{
        for(User i : users)
            if (i.getUserName().equals(username) && i.getPassWord().equals(password)) {
                i.initLog(true);
                return i;
            }
        throw new Exception("invalid username or password!");
    }
    public static void deleteUser(User user){
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.delete(user);
        for (HeroData heroData : user.getHeroes())
            session.delete(heroData);
        session.getTransaction().commit();
        session.close();
        user.getLog().addDeleteHeader();
        users.remove(user);
    }

    public static void saveChanges(User user) {
        if (user == null) {
            //TODO
            return;
        }
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        for (HeroData i : user.getHeroes())
            session.saveOrUpdate(i);
        session.getTransaction().commit();
        session.close();

        session = sessionFactory.openSession();
        session.beginTransaction();
        session.saveOrUpdate(user);
        session.getTransaction().commit();
        session.close();
       }

}
