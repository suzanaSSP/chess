package services;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import io.javalin.http.UnauthorizedResponse;
import model.AuthData;
import model.UserData;
import server.handlers.requests_and_results.*;

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
        UserData new_user = dataaccess.createUser(registerRequest.username(), registerRequest.password(),
                registerRequest.email());

        // Create Token
        String new_token = auth.createAuth(new_user.username());

        RegisterResult result = new RegisterResult(new_user.username(), new_token);
        return result;
    }

    public void clear_service(){
        auth.clear();
        game.clear();
        dataaccess.clear();
    }

    public LoginResult login_service(LoginRequest request) {
        UserData user = dataaccess.getUser(request.username());
        if (user.username().equals(request.username()) && user.password().equals(request.password())) {
            String new_token = auth.createAuth(user.username());

            LoginResult result = new LoginResult(user.username(), new_token);
            return result;
        }
        else {
            throw new UnauthorizedResponse();
        }
    }

    public void logout_service(String token) {
        auth.deleteAuth(token);
    }

    public void authenticateToken(String token) {
        try {
            AuthData test = auth.getAuth(token);
        } catch (Exception e) {
            throw new UnauthorizedResponse("User not found");
        }
    }

    public String getUser_withAuth(String token) {
        return auth.getAuth(token).username();
    }
}
