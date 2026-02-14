package services;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import model.UserData;
import server.handlers.requests_and_results.LoginRequest;
import server.handlers.requests_and_results.LoginResult;
import server.handlers.requests_and_results.RegisterRequest;
import server.handlers.requests_and_results.RegisterResult;

public class UserService {
    MemoryAuthDAO auth = new MemoryAuthDAO();
    MemoryUserDAO dataaccess = new MemoryUserDAO();

    public RegisterResult register(RegisterRequest registerRequest) {

        // Create User
        UserData new_user = dataaccess.createUser(registerRequest.username(), registerRequest.password(),
                registerRequest.email());

        // Create Token
        String new_token = auth.createAuth();

        RegisterResult result = new RegisterResult(new_user.username(), new_token);
        return result;
    }

    public void clear_service(){
        dataaccess.clear();
    }

    public LoginResult login_service(LoginRequest request) {
        UserData user = dataaccess.getUser(request.username());
        String new_token = auth.createAuth();

        LoginResult result = new LoginResult(user.username(), new_token);
        return result;
    }
}
