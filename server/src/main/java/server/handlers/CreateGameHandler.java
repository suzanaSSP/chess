package server.handlers;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.UnauthorizedResponse;
import server.handlers.requests_and_results.CreateGameRequest;
import server.handlers.requests_and_results.CreateGameResult;
import server.handlers.requests_and_results.RegisterResult;
import services.GameServices;
import services.UserService;

public class CreateGameHandler implements Handler {
    UserService user_service;
    GameServices game_service;

    public CreateGameHandler(UserService u, GameServices g){
        user_service = u;
        game_service = g;
    }

    public void handle(Context ctx) {
        String authToken = ctx.header("authorization");
        CreateGameRequest request = ctx.bodyAsClass(CreateGameRequest.class);

        // Authenticate token
        try {
            user_service.authenticateToken(authToken);
        } catch (UnauthorizedResponse e) {
            throw e;
        }

        // Everything passes, create game
        int game_id = game_service.createGameServices(request.gameName());
        CreateGameResult result = new CreateGameResult(game_id);
        ctx.json(result);
    }
}
