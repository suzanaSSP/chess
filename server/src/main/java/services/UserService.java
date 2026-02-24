package services;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import io.javalin.http.UnauthorizedResponse;
import model.AuthData;
import model.UserData;

public class UserService {
    MemoryAuthDAO auth;
    MemoryUserDAO dataaccess;
    MemoryGameDAO game;

    public UserService(MemoryUserDAO userDataBase, MemoryAuthDAO authDataBase, MemoryGameDAO g){
        auth = authDataBase;
        dataaccess = userDataBase;
        game = g;
    }

    public RegisterResult register(RegisterRequest registerRequest) {
        // Create User
        UserData newUser = dataaccess.createUser(registerRequest.username(), registerRequest.password(),
                registerRequest.email());

        // Create Token
        String newToken = auth.createAuth(newUser.username());

        RegisterResult result = new RegisterResult(newUser.username(), newToken);
        return result;
    }

    public void clearService(){
        auth.clear();
        game.clear();
        dataaccess.clear();
    }

    public LoginResult loginService(LoginRequest request) {
        UserData user = dataaccess.getUser(request.username());
        if (user.username().equals(request.username()) && user.password().equals(request.password())) {
            String newToken = auth.createAuth(user.username());

            LoginResult result = new LoginResult(user.username(), newToken);
            return result;
        }
        else {
            throw new UnauthorizedResponse();
        }
    }

    public void logoutService(String token) {
        auth.deleteAuth(token);
    }

    public void authenticateToken(String token) {
        try {
            AuthData test = auth.getAuth(token);
        } catch (Exception e) {
            throw new UnauthorizedResponse("User not found");
        }
    }

    public String getUserWithAuth(String token) {
        return auth.getAuth(token).username();
    }
}
