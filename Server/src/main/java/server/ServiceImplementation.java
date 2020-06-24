package server;

import model.Joc;
import model.User;
import org.springframework.remoting.RemoteAccessException;
import repository.JocHibernateRepository;
import repository.UserJdbcRepository;
import services.AppException;
import services.IObserver;
import services.IService;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceImplementation implements IService {
    private UserJdbcRepository userJdbcRepository;
    private JocHibernateRepository jocHibernateRepository;
    private Map<String, IObserver> logged;
    private Map<String, Integer> asteptare;
    private List<Joc> jocuriActive;

    public ServiceImplementation(UserJdbcRepository userJdbcRepository, JocHibernateRepository jocHibernateRepository) {
        this.userJdbcRepository = userJdbcRepository;
        this.jocHibernateRepository = jocHibernateRepository;
        this.logged = new ConcurrentHashMap<>();
        this.asteptare = new HashMap<>();
        this.jocuriActive = new ArrayList<>();
    }

    @Override
    public void login(User user, IObserver observer) {
        User u = userJdbcRepository.findUserPass(user.getUser(), user.getPassword());
        if (u!=null){
            if(logged.get(u.getId())!=null)
                throw new AppException("User already logged in.");
            logged.put(u.getId(), observer);
        }else
            throw new AppException("Authentication failed.");
    }

    @Override
    public void logout(User user, IObserver observer) {
        User u = userJdbcRepository.findUserPass(user.getUser(), user.getPassword());
        IObserver localClient=logged.remove(u.getId());
        if (localClient==null)
            throw new AppException("User "+u.getId()+" is not logged in.");
        verificaJocActiv(user);
    }

    void verificaJocActiv(User user){
        try {
            Joc jocSters = null;
            for (Joc j : jocuriActive) {
                String jucatorRamas = null;
                if (j.getJucator1().equals(user.getId())) { //daca e primul jucator si a iesit
                    jucatorRamas = j.getJucator2();
                }else if(j.getJucator2().equals(user.getId())){
                    jucatorRamas =j.getJucator1();
                }
                if(jucatorRamas!=null){
                    j.setCastigator(jucatorRamas);
                    logged.get(jucatorRamas).finalJoc("castigator", true);
                    jocSters = j;
                    break;
                }
            }
            if(jocSters!=null){
                jocuriActive.remove(jocSters);
                jocHibernateRepository.save(jocSters);
            }
        }catch (RemoteException ex){
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public void startJoc(User user, Integer pozitie) {
        try{
            if(asteptare.size() == 0){
                //daca nu exista in asteptare
                asteptare.put(user.getId(), pozitie);
                logged.get(user.getId()).asteapta();
            }else{
                //daca exista jucatori doritori ii alegem o pereche pentru joc
                //jucatorul care asteapta de cel mai mult timp (primul din lista)
                Map.Entry<String,Integer> entry = asteptare.entrySet().iterator().next();
                String idJucator2 = entry.getKey();
                Integer poz = entry.getValue();
                //stergem jucatorul extras din lista de asteptare
                asteptare.remove(idJucator2);
                //cream nou joc
                Joc joc = new Joc(user.getId(), idJucator2, pozitie, poz);
                System.out.println(joc);
                jocuriActive.add(joc);
                //anuntam jucatorii
                logged.get(user.getId()).startJoc(joc, userJdbcRepository.findOne(idJucator2));
                logged.get(idJucator2).startJoc(joc, user);
                //anuntam jucatorul care incepe
                logged.get(idJucator2).alege(null);
            }
        }catch(RemoteException ex){
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public void ataca(User user, Joc joc, Integer pozitieAtac) {
        try{
            if(joc.getJucator1().equals(user.getId())){ //a ales jucatorul 1
                if(pozitieAtac.equals(joc.getPozitie2())) { //a castigat
                    logged.get(user.getId()).finalJoc("castigator", false);
                    logged.get(joc.getJucator2()).finalJoc("pierzator", false);
                    jocuriActive.remove(joc);
                    joc.setCastigator(joc.getJucator1());
                    jocHibernateRepository.save(joc);
                }else{ //jocul continua cu mutarea celuilalt jucator
                    logged.get(joc.getJucator2()).alege(pozitieAtac);
                }
            }else{ //a ales jucatorul 2
                if(pozitieAtac.equals(joc.getPozitie1())) { //a castigat
                    logged.get(user.getId()).finalJoc("castigator", false);
                    logged.get(joc.getJucator1()).finalJoc("pierzator", false);
                    jocuriActive.remove(joc);
                    joc.setCastigator(joc.getJucator2());
                    jocHibernateRepository.save(joc);
                }else{ //jocul continua cu mutarea celuilalt jucator
                    logged.get(joc.getJucator1()).alege(pozitieAtac);
                }
            }
        }catch (RemoteException ex){
            System.out.println(ex.getMessage());
        }
    }
}
