package client;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
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

        try {
            return gson.fromJson(httpResponse.body(), RegisterResult.class);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            throw new ClientExceptions("Error");
        }

    }

    public LoginResult loginServerFacade(String username, String password)
            throws URISyntaxException, IOException, InterruptedException {
        LoginRequest request = new LoginRequest(username, password);
        String requestString = gson.toJson(request).toString();
        HttpResponse<String> httpResponse = cc.doPost("localhost",
                8080, "/session", requestString, null);
        try {
            return gson.fromJson(httpResponse.body(), LoginResult.class);
        } catch (JsonSyntaxException e) {
            throw new ClientExceptions("Error");
        }

    }

    public ListGamesResult listGamesServerFacade(String authToken)
            throws URISyntaxException, IOException, InterruptedException {
        HttpResponse<String> httpResponse = cc.doGet("localhost",
                8080, "/game", authToken);

        try {
            return gson.fromJson(httpResponse.body(), ListGamesResult.class);
        } catch (JsonSyntaxException e) {
            throw new ClientExceptions("Error");
        }

    }

    public Integer createGameServerFacade(String authToken, String gameName)
            throws URISyntaxException, IOException, InterruptedException {
        // No need to create game request because it's just a string
        CreateGameRequest request = new CreateGameRequest(gameName);
        String requestString = gson.toJson(request).toString();
        HttpResponse<String> httpResponse = cc.doPost("localhost", 8080,
                "/game", requestString, authToken);

        try {
            CreateGameResult result = gson.fromJson(httpResponse.body(), CreateGameResult.class);
            return result.gameID();
        } catch (JsonSyntaxException e) {
            throw new ClientExceptions("Error");
        }

    }

    public void logoutServerFacade(String authToken)
            throws URISyntaxException, IOException, InterruptedException {
        try {
            cc.doDelete("localhost", 8080, "/session", authToken);
        } catch (Exception e) {
            throw new ClientExceptions("Error?");
        }

    }

    public void clearServerFacade()
            throws URISyntaxException, IOException, InterruptedException {
        try {
            cc.doDelete("localhost", 8080, "/db", null);
        } catch (Exception e) {
            throw new ClientExceptions("Error");
        }
    }

    public void joinGameServerFacade(String playercolor, int gameid, String authToken)
            throws URISyntaxException, IOException, InterruptedException {
        JoinGameRequest request = new JoinGameRequest(playercolor, gameid);
        String requestString = gson.toJson(request).toString();

        try {
            cc.doPut("localhost", 8080, "/game", requestString, authToken);
        } catch (Exception e) {
            throw new ClientExceptions("Error");
        }
    }
}
