package DB;

import DB.components.heroes.Hero;
import DB.components.User;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.sql.Timestamp;
import java.util.ArrayList;

public class UserDB {
    private static ArrayList<User> users;
    private static SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
    static {
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
        user.initLog(false);
        user.getLog().write("USER: " + user.getUserName());
        user.getLog().write("CREATED_AT: " + new Timestamp(System.currentTimeMillis()));
        user.getLog().write("PASSWORD: " + user.getPassWord());
        user.getLog().write("");
    }
    public static User getUser(String username, String password) throws Exception{
        for(User i : users)
            if (i.getUserName().equals(username) && i.getPassWord() == password.hashCode()) {
                i.initLog(true);
                return i;
            }
        throw new Exception("invalid username or password!");
    }
    public static void deleteUser(User user){
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.delete(user);
        for (Hero hero : user.getHeroes())
            session.delete(hero);
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
        session.saveOrUpdate(user);
        session.getTransaction().commit();
        session.close();
       }

       @Deprecated
       public static void clearUsers() {
           Session session = HibernateUtil.getSessionFactory().openSession();
           session.beginTransaction();
           for (User user : users) {
               if (user.getUserName().equals("##Defaut"))
                   continue;
               session.delete(user);
           }
           session.getTransaction().commit();
           session.close();

           session = sessionFactory.openSession();
           session.close();


       }
}
