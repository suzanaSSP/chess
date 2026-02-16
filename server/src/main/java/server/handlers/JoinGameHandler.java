package server.handlers;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.UnauthorizedResponse;
import server.handlers.requests_and_results.JoinGameRequest;
import services.GameServices;
import services.UserService;

import java.nio.channels.AlreadyBoundException;

public class JoinGameHandler implements Handler {
    UserService user_service;
    GameServices game_service;

    public JoinGameHandler(UserService u, GameServices g){
        user_service = u;
        game_service = g;
    }
    public void handle(Context ctx){
        String token = ctx.header("authorization");
        JoinGameRequest request = ctx.bodyAsClass(JoinGameRequest.class);

        // Authenticate token
        try {
            user_service.authenticateToken(token);

            // Authentication passed
            String username = user_service.getUser_withAuth(token);
            game_service.joinGameService(username, request.playerColor(), request.gameID());
            ctx.json("{}");
        } catch (UnauthorizedResponse e) {
            throw e;
        } catch (AlreadyBoundException e) {
            throw e;
        }





    }

}
