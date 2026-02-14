package dataaccess;
import dataaccess.interfaces.UserDAO;
import model.UserData;
import org.eclipse.jetty.server.Authentication;

import java.util.HashMap;
import java.util.Map;

public class MemoryUserDAO implements UserDAO {
    Map<String, UserData> user_database = new HashMap<>();

    public void clear(){
        System.out.println("Clearing");
    }

    public UserData getUser(String username){
        UserData user = user_database.get(username);
        return user;
    }

    public UserData createUser(String username, String password, String email){
        UserData user = new UserData(username, password, email);
        user_database.put(user.username(), user);
        return user;
    }
}
