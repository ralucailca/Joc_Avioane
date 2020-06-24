package services;

import model.Joc;
import model.User;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IObserver extends Remote {
    void alege(Integer alegereOponent) throws RemoteException;
    void startJoc(Joc joc, User oponent) throws RemoteException;
    void finalJoc(String status, Boolean oponentIesit) throws RemoteException;
    void asteapta() throws RemoteException;
}
