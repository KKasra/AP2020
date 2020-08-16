package DB;

import DB.components.User;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.ArrayList;

public class HibernateUtil {
    private static SessionFactory sessionFactory;
    static {
        StandardServiceRegistry sr = new StandardServiceRegistryBuilder().configure().build();
        sessionFactory = new MetadataSources(sr).buildMetadata().buildSessionFactory();
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
