package client;

import com.google.gson.Gson;
import io.javalin.http.BadRequestResponse;
import requestsandresults.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpResponse;

public class ServerFacade {
    ClientCommunicator cc = new ClientCommunicator();
    Gson gson = new Gson();

    public RegisterResult registerServerFacade(String username, String password, String email) throws URISyntaxException, IOException, InterruptedException {
        RegisterRequest request = new RegisterRequest(username, password, email);
        String requestString = gson.toJson(request).toString();
        HttpResponse<String> httpResponse = cc.doPost("localhost", 8080, "/user", requestString);
        return gson.fromJson(httpResponse.body(), RegisterResult.class);
    }

    public LoginResult loginServerFacade(String username, String password) throws URISyntaxException, IOException, InterruptedException {
        LoginRequest request = new LoginRequest(username, password);
        String requestString = gson.toJson(request).toString();
        HttpResponse<String> httpResponse = cc.doPost("localhost", 8080, "/session", requestString);
        return gson.fromJson(httpResponse.body(), LoginResult.class);
    }

    public ListGamesResult listGamesServerFacade(String authToken) throws URISyntaxException, IOException, InterruptedException {
        HttpResponse<String> httpResponse = cc.doGet("localhost", 8080, "/game", authToken);

        return gson.fromJson(httpResponse.body(), ListGamesResult.class);
    }
}
