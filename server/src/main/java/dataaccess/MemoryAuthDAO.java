package dataaccess;

import dataaccess.interfaces.AuthDAO;
import io.javalin.http.UnauthorizedResponse;
import model.UserData;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO {
    Map<String, String> auth_database = new HashMap<>();
    //Map<Token, username>

    public String createAuth(String username){
        //Create token
        var new_auth = UUID.randomUUID().toString();
        //Add to database
        auth_database.put(new_auth, username);
        return new_auth.toString();
    }
    public String getAuth(String authToken){
        String token = auth_database.get(authToken);
        if (token != null) {
            return token;
        }
        else {
            throw new UnauthorizedResponse("User not found");
        }
    }

    public void deleteAuth(String authtoken){
        String current_auth = getAuth(authtoken);
        auth_database.remove(authtoken, current_auth);
    }
}
