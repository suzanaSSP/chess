package client;

import io.javalin.http.BadRequestResponse;
import requestsandresults.RegisterRequest;
import requestsandresults.RegisterResult;

import java.io.IOException;
import java.net.URISyntaxException;

public class ServerFacade {
    ClientCommunicator cc = new ClientCommunicator();

    public RegisterResult registerServerFacade(String username, String password, String email) throws URISyntaxException, IOException, InterruptedException {
        RegisterRequest request = new RegisterRequest(username, password, email);
        RegisterResult result = cc.registerCommunicator(request);
        return result;
    }
}
