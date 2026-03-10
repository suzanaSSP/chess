package server.handlers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dataaccess.DataAccessException;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.UnauthorizedResponse;
import server.handlers.requestsandresults.JoinGameRequest;
import services.GameServices;
import services.UserService;

import java.nio.channels.AlreadyBoundException;

public class JoinGameHandler implements Handler {
    UserService userService;
    GameServices gameService;

    public JoinGameHandler(UserService u, GameServices g){
        userService = u;
        gameService = g;
    }
    public void handle(Context ctx) throws DataAccessException {
        String token = ctx.header("authorization");
//        JoinGameRequest request = ctx.bodyAsClass(JoinGameRequest.class);
        Gson gson = new Gson();
        JoinGameRequest request = gson.fromJson(ctx.body(), JoinGameRequest.class);

        // Authenticate token
        try {
            userService.authenticateToken(token);

            // Authentication passed
            String username = userService.getUserWithAuth(token);
            gameService.joinGameService(username, request.playerColor(), request.gameID());
            // Turn to JSON file
            ctx.result(gson.toJson(new JsonObject()));
        } catch (UnauthorizedResponse e) {
            throw e;
        } catch (AlreadyBoundException e) {
            throw e;
        } catch (BadRequestResponse e) {
            throw e;
        }
    }

}
