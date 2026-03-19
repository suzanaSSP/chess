package client;

import requestsandresults.RegisterRequest;
import requestsandresults.RegisterResult;

public class ServerFacade {
    ClientCommunicator cc = new ClientCommunicator();

    public RegisterResult registerServerFacade(String username, String password, String email){
        RegisterRequest request = new RegisterRequest(username, password, email);
        RegisterResult result = cc.registerCommunicator(request);
        return result;
    }
}
