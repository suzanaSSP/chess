package client;

import requestsandresults.RegisterRequest;
import requestsandresults.RegisterResult;

public class ClientComunicator {

    public RegisterResult registerComunicator(RegisterRequest request){
        return new RegisterResult("", "");
    }
}
