package services;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import model.UserData;
import server.handlers.requests_and_results.RegisterRequest;
import server.handlers.requests_and_results.RegisterResult;

public class UserService {
    public RegisterResult register(RegisterRequest registerRequest) {

        // Create User
        MemoryUserDAO dataaccess = new MemoryUserDAO();
        UserData new_user = dataaccess.createUser(registerRequest.username(), registerRequest.password(),
                registerRequest.email());

        // Create Token
        MemoryAuthDAO auth = new MemoryAuthDAO();
        String new_token = auth.createAuth();

        RegisterResult result = new RegisterResult(new_user.username(), new_token);
        return result;
    }
}
