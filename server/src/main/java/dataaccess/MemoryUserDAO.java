package dataaccess;
import dataaccess.interfaces.UserDAO;
import io.javalin.http.UnauthorizedResponse;
import model.UserData;
import java.nio.channels.AlreadyBoundException;
import java.util.*;

public class MemoryUserDAO implements UserDAO {
    Map<String, UserData> user_database = new HashMap<>();
    // <username, UserData>

    public void clear(){
        user_database = new HashMap<>();
    }

    public UserData getUser(String username)  {
        UserData user = user_database.get(username);
        if (user != null) {
            return user;
        } else {
            throw new UnauthorizedResponse("User not found");
        }
    }

    public UserData createUser(String username, String password, String email){
        System.out.println(user_database);
        UserData user_test = user_database.get(username);
        if (user_test != null) {
            throw new AlreadyBoundException();
        }
        else {
            UserData user = new UserData(username, password, email);
            user_database.put(user.username(), user);
            System.out.println(user_database);
            return user;
        }
    }
}
