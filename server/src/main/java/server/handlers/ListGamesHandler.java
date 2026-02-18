package server.handlers;

import com.google.gson.Gson;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.UnauthorizedResponse;
import server.handlers.requests_and_results.ListGamesResult;
import services.GameServices;
import services.UserService;

public class ListGamesHandler implements Handler {
    UserService user_service;
    GameServices game_service;

    public ListGamesHandler(UserService u, GameServices g){
        user_service = u;
        game_service = g;
    }

    public void handle(Context ctx) {
        String token = ctx.header("authorization");

        // Authenticate token
        try {
            user_service.authenticateToken(token);
        } catch (UnauthorizedResponse e) {
            throw e;
        }

        // Passed authentication, list games
        ListGamesResult result = new ListGamesResult(game_service.listGamesService());
        // Turn to JSON file
        Gson gson = new Gson();
        ctx.result(gson.toJson(result));
    }
}
