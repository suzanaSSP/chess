package dataaccess;
import dataaccess.interfaces.UserDAO;
import io.javalin.http.UnauthorizedResponse;
import model.UserData;
import java.nio.channels.AlreadyBoundException;
import java.util.*;

public class MemoryUserDAO implements UserDAO {
    Map<String, UserData> userDatabase = new HashMap<>();
    // <username, UserData>

    public void clear(){
        userDatabase = new HashMap<>();
    }

    public UserData getUser(String username)  {
        UserData user = userDatabase.get(username);
        if (user != null) {
            return user;
        } else {
            throw new UnauthorizedResponse("User not found");
        }
    }

    public UserData createUser(String username, String password, String email){
        UserData userTest = userDatabase.get(username);
        if (userTest == null) {
            UserData user = new UserData(username, password, email);
            userDatabase.put(user.username(), user);
            return user;
        }
        else {
            throw new AlreadyBoundException();
        }
    }
}
