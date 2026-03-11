package services;

import dataaccess.DataAccessException;
import dataaccess.interfaces.AuthDAO;
import dataaccess.interfaces.GameDAO;
import dataaccess.interfaces.UserDAO;
import io.javalin.http.UnauthorizedResponse;
import model.AuthData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;
import server.handlers.requestsandresults.LoginRequest;
import server.handlers.requestsandresults.LoginResult;
import server.handlers.requestsandresults.RegisterRequest;
import server.handlers.requestsandresults.RegisterResult;

public class UserService {
    public AuthDAO auth;
    public UserDAO user;
    public GameDAO game;

    public UserService(UserDAO userDataBase, AuthDAO authDataBase, GameDAO g){
        auth = authDataBase;
        user = userDataBase;
        game = g;
    }

    public RegisterResult register(RegisterRequest registerRequest) throws DataAccessException {
        // Create User
        UserData newUser = user.createUser(registerRequest.username(), registerRequest.password(),
                registerRequest.email());
        // Create Token
        String newToken = auth.createAuth(newUser.username());

        RegisterResult result = new RegisterResult(newUser.username(), newToken);
        return result;

    }

    public void clearService() throws DataAccessException {
        user.clear();
        auth.clear();
        game.clear();
    }

    public LoginResult loginService(LoginRequest request) throws DataAccessException {
        UserData userTest = this.user.getUser(request.username());

        if (userTest != null) {
            // Check hashed password
            Boolean correctPassword = BCrypt.checkpw(request.password(), userTest.password());
            if (userTest.username().equals(request.username()) && correctPassword) {
                String newToken = auth.createAuth(userTest.username());

                LoginResult result = new LoginResult(userTest.username(), newToken);
                return result;
            }
        }
        throw new UnauthorizedResponse();
    }

    public void logoutService(String token) throws DataAccessException {
        AuthData authTokenTest = auth.getAuth(token);
        if (authTokenTest == null){
            throw new UnauthorizedResponse();
        }
        else {
            auth.deleteAuth(token);
        }

    }

    public void authenticateToken(String token) throws DataAccessException {
        AuthData test = auth.getAuth(token);
        if (test == null){
            throw new UnauthorizedResponse();
        }
    }

    public String getUserWithAuth(String token) {
        try {
            return auth.getAuth(token).username();
        } catch (Exception e) {
            throw new UnauthorizedResponse("User not found");
        }
    }

}
