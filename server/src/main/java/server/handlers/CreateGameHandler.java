package server.handlers;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.UnauthorizedResponse;
import server.handlers.requestsandresults.CreateGameRequest;
import server.handlers.requestsandresults.CreateGameResult;
import server.handlers.requestsandresults.JoinGameRequest;
import services.GameServices;
import services.UserService;

public class CreateGameHandler implements Handler {
    UserService userService;
    GameServices gameService;

    public CreateGameHandler(UserService u, GameServices g){
        userService = u;
        gameService = g;
    }

    public void handle(Context ctx) throws DataAccessException {
        String authToken = ctx.header("authorization");
        Gson gson = new Gson();
        CreateGameRequest request = gson.fromJson(ctx.body(), CreateGameRequest.class);

        // Bad request
        if (request.gameName() == null) {
            throw new BadRequestResponse();
        }


        // Everything passes, create game
        int gameId = gameService.createGameServices(request.gameName(), authToken);
        CreateGameResult result = new CreateGameResult(gameId);
        // Turn to JSON file
        ctx.result(gson.toJson(result));
    }
}
