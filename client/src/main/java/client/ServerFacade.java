package client;

import com.google.gson.Gson;
import requestsandresults.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpResponse;

public class ServerFacade {
    ClientCommunicator cc = new ClientCommunicator();
    Gson gson = new Gson();

    public RegisterResult registerServerFacade(String username, String password, String email)
            throws URISyntaxException, IOException, InterruptedException {
        RegisterRequest request = new RegisterRequest(username, password, email);
        String requestString = gson.toJson(request).toString();
        HttpResponse<String> httpResponse = cc.doPost("localhost", 8080,
                "/user", requestString, null);
        return gson.fromJson(httpResponse.body(), RegisterResult.class);
    }

    public LoginResult loginServerFacade(String username, String password)
            throws URISyntaxException, IOException, InterruptedException {
        LoginRequest request = new LoginRequest(username, password);
        String requestString = gson.toJson(request).toString();
        HttpResponse<String> httpResponse = cc.doPost("localhost",
                8080, "/session", requestString, null);
        return gson.fromJson(httpResponse.body(), LoginResult.class);
    }

    public ListGamesResult listGamesServerFacade(String authToken)
            throws URISyntaxException, IOException, InterruptedException {
        HttpResponse<String> httpResponse = cc.doGet("localhost",
                8080, "/game", authToken);
        return gson.fromJson(httpResponse.body(), ListGamesResult.class);
    }

    public Integer createGameServerFacade(String authToken, String gameName)
            throws URISyntaxException, IOException, InterruptedException {
        // No need to create game request because it's just a string
        CreateGameRequest request = new CreateGameRequest(gameName);
        String requestString = gson.toJson(request).toString();
        HttpResponse<String> httpResponse = cc.doPost("localhost", 8080,
                "/game", requestString, authToken);
        CreateGameResult result = gson.fromJson(httpResponse.body(), CreateGameResult.class);
        return result.gameID();
    }

    public void logoutServerFacade(String authToken)
            throws URISyntaxException, IOException, InterruptedException {
        cc.doDelete("localhost", 8080, "/session", authToken);
    }

    public void clearServerFacade()
            throws URISyntaxException, IOException, InterruptedException {
        cc.doDelete("localhost", 8080, "/db", null);
    }

    public void joinGameServerFacade(String playercolor, int gameid, String authToken)
            throws URISyntaxException, IOException, InterruptedException {
        JoinGameRequest request = new JoinGameRequest(playercolor, gameid);
        String requestString = gson.toJson(request).toString();
        cc.doPut("localhost", 8080, "/game", requestString, authToken);
    }
}
