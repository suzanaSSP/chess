package server.handlers;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import requestsandresults.ListGamesResult;
import services.GameServices;
import services.UserService;

public class ListGamesHandler implements Handler {
    UserService userService;
    GameServices gameService;

    public ListGamesHandler(UserService u, GameServices g){
        userService = u;
        gameService = g;
    }

    public void handle(Context ctx) throws DataAccessException {
        String token = ctx.header("authorization");

        // Passed authentication, list games
        ListGamesResult result = new ListGamesResult(gameService.listGamesService(token));
        // Turn to JSON file
        Gson gson = new Gson();
        ctx.result(gson.toJson(result));
    }
}
