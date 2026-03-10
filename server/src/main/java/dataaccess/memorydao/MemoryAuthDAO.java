package dataaccess.memorydao;

import dataaccess.interfaces.AuthDAO;
import io.javalin.http.UnauthorizedResponse;
import model.AuthData;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO {
    public Map<String, AuthData> authDatabase = new HashMap<>();
    //Map<Token, username>

    public void clear() {
        authDatabase = new HashMap<>();}

    public String createAuth(String username){
        //Create token
        var newAuth = UUID.randomUUID().toString();
        //Add to database
        AuthData authorization = new AuthData(newAuth, username);
        authDatabase.put(newAuth, authorization);
        return newAuth.toString();
    }
    public AuthData getAuth(String authToken){
        AuthData authorization = authDatabase.get(authToken);
        if (authorization != null) {
            return authorization;
        }
        else {
            throw new UnauthorizedResponse("User not found");
        }
    }

    public void deleteAuth(String authtoken){
        AuthData currentAuth = getAuth(authtoken);
        authDatabase.remove(authtoken, currentAuth);
    }
}
