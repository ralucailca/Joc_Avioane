package repository;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateUtils {
    private static SessionFactory sessionFactory=null;

    private HibernateUtils(){};

    public static SessionFactory getSessionFactory(){
        if(sessionFactory == null){
            initialize();
        }
        return sessionFactory;
    }

    private static void initialize() {
        // A SessionFactory is set up once for an application!
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .build();
        try {
            sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
        }
        catch (Exception e) {
            System.out.println(e);
            StandardServiceRegistryBuilder.destroy( registry );
        }
    }

    public static void close(){
        if ( sessionFactory != null ) {
            sessionFactory.close();
        }
    }
}
