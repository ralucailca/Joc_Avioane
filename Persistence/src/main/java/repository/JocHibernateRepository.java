package repository;

import model.Joc;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JocHibernateRepository {
    private SessionFactory sessionFactory;

    public JocHibernateRepository(){
        this.sessionFactory=HibernateUtils.getSessionFactory();
    }

    public void save(Joc entity) {
        try(Session session=sessionFactory.openSession()){
            Transaction transaction=null;
            try{
                transaction=session.beginTransaction();
                session.save(entity);
                transaction.commit();
            }catch(RuntimeException ex){
                if(transaction!=null)
                    transaction.rollback();
                ex.printStackTrace();
                throw new RepositoryException(ex.getMessage());
            }
        }
    }

    public Joc findOne(Integer integer) {
        Joc r=null;
        try(Session session=sessionFactory.openSession()){
            Transaction transaction=null;
            try{
                transaction=session.beginTransaction();
                r=session.createQuery("from Joc where id=?",Joc.class).setParameter(0,integer).setMaxResults(1).uniqueResult();
                transaction.commit();
            }catch(RuntimeException ex){
                if(transaction!=null)
                    transaction.rollback();
                System.out.println("Aici prinde eroare");
                ex.printStackTrace();
                throw new RepositoryException(ex.getMessage());
            }
        }
        return r;
    }

    public Iterable<Joc> findUser(String idUser) {
        try(Session session=sessionFactory.openSession()){
            Transaction transaction=null;
            try{
                transaction=session.beginTransaction();
                List<Joc> lista = session.createQuery("from Joc where jucator1=? or jucator2=? ", Joc.class)
                        .setParameter(0, idUser)
                        .setParameter(1, idUser)
                        .list();
                transaction.commit();
                return lista;
            }catch(RuntimeException ex){
                if(transaction!=null)
                    transaction.rollback();
                ex.printStackTrace();
            }
        }
        return null;
    }

    public void delete(Integer integer) {
        try(Session session=sessionFactory.openSession()){
            Transaction transaction=null;
            try{
                transaction=session.beginTransaction();
                Joc joc=session.createQuery("from Joc where id=?", Joc.class).setParameter(0,integer).setMaxResults(1).uniqueResult();
                session.delete(joc);
                transaction.commit();
            }catch(RuntimeException ex){
                if(transaction!=null)
                    transaction.rollback();
                ex.printStackTrace();
                throw new RepositoryException(ex.getMessage());
            }
        }
    }
    
    public void update(Integer integer, Joc entity) {
        try(Session session=sessionFactory.openSession()){
            Transaction transaction=null;
            try{
                transaction=session.beginTransaction();
                Joc joc=session.createQuery("from Joc where id=?", Joc.class).setParameter(0,integer).setMaxResults(1).uniqueResult();
                joc.setJucator1(entity.getJucator1());
                joc.setJucator2(entity.getJucator2());
                joc.setCastigator(entity.getCastigator());
                joc.setPozitie1(entity.getPozitie1());
                joc.setPozitie2(entity.getPozitie2());
                session.update(joc);
                transaction.commit();
            }catch(RuntimeException ex){
                if(transaction!=null)
                    transaction.rollback();
                ex.printStackTrace();
                throw new RepositoryException(ex.getMessage());
            }
        }
    }

}
