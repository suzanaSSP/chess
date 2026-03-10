package services;

import dataaccess.interfaces.GameDAO;
import dataaccess.interfaces.UserDAO;
import dataaccess.memorydao.MemoryAuthDAO;
import dataaccess.memorydao.MemoryGameDAO;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.UnauthorizedResponse;
import server.handlers.requestsandresults.AlternativeGameData;

import java.util.Collection;

public class GameServices {
    MemoryAuthDAO auth;
    UserDAO dataaccess;
    public GameDAO game;
    UserService userService;

    public GameServices(UserDAO userDataBase, MemoryAuthDAO authDataBase, GameDAO g, UserService u){
        auth = authDataBase;
        dataaccess = userDataBase;
        game = g;
        userService = u;
    }

    public Integer createGameServices(String gameName, String authToken){
        // Authenticate token
        try {
            userService.authenticateToken(authToken);
        } catch (UnauthorizedResponse e) {
            throw e;
        }
        return game.createGame(gameName);
    }

    public Collection<AlternativeGameData> listGamesService(String token){
        // Authenticate token
        try {
            userService.authenticateToken(token);
        } catch (UnauthorizedResponse e) {
            throw e;
        }
        return game.listGames();
    }

    public void joinGameService(String username, String playerColor, int gameId) {
        if (playerColor == null || ((!playerColor.equals("WHITE")) && (!playerColor.equals("BLACK")))) {
            throw new BadRequestResponse();
        }
        game.updateGame(username, playerColor, gameId);


    }

}
