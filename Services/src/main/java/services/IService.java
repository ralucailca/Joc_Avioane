package services;

import model.Joc;
import model.User;

public interface IService {
    void login(User user, IObserver observer);
    void logout(User user, IObserver observer);
    void startJoc(User user, Integer pozitie);
    void ataca(User user, Joc joc, Integer pozitieAtac);
}
