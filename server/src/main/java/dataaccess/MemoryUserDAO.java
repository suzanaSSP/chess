package dataaccess;
import dataaccess.interfaces.UserDAO;
import model.UserData;

public class MemoryUserDAO implements UserDAO {
    public void clear(){
        System.out.println("Clearing");
    }

    public UserData getUser(String username){
        UserData user = new UserData(username, null, null);
        return user;
    }

    public UserData createUser(String username, String password, String email){
        UserData user = new UserData(username, password, email);
        return user;
    }
}
