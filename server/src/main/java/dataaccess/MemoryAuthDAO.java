package dataaccess;

import dataaccess.interfaces.AuthDAO;
import io.javalin.http.UnauthorizedResponse;
import model.AuthData;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO {
    Map<String, AuthData> auth_database = new HashMap<>();
    //Map<Token, username>

    public void clear() {auth_database = new HashMap<>();}

    public String createAuth(String username){
        //Create token
        var new_auth = UUID.randomUUID().toString();
        //Add to database
        AuthData authorization = new AuthData(new_auth, username);
        auth_database.put(new_auth, authorization);
        return new_auth.toString();
    }
    public AuthData getAuth(String authToken){
        AuthData authorization = auth_database.get(authToken);
        if (authorization != null) {
            return authorization;
        }
        else {
            throw new UnauthorizedResponse("User not found");
        }
    }

    public void deleteAuth(String authtoken){
        AuthData current_auth = getAuth(authtoken);
        auth_database.remove(authtoken, current_auth);
    }
}
