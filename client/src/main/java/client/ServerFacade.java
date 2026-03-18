package client;

import requestsandresults.RegisterRequest;
import requestsandresults.RegisterResult;

public class ServerFacade {
    ClientComunicator cc = new ClientComunicator();

    public RegisterResult registerServerFacade(String username, String password, String email){
        RegisterRequest request = new RegisterRequest(username, password, email);
        RegisterResult result = cc.registerComunicator(request);
        return result;
    }
}
