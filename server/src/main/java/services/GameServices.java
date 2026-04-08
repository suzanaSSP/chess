package services;

import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import dataaccess.DataAccessException;
import dataaccess.interfaces.AuthDAO;
import dataaccess.interfaces.GameDAO;
import dataaccess.interfaces.UserDAO;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.UnauthorizedResponse;
import requestsandresults.AlternativeGameData;

import java.util.Collection;

public class GameServices {
    AuthDAO auth;
    UserDAO dataaccess;
    public GameDAO game;
    UserService userService;

    public GameServices(UserDAO userDataBase, AuthDAO authDataBase, GameDAO g, UserService u){
        auth = authDataBase;
        dataaccess = userDataBase;
        game = g;
        userService = u;
    }

    public Integer createGameServices(String gameName, String authToken) throws DataAccessException {
        // Authenticate token
        try {
            userService.authenticateToken(authToken);
        } catch (UnauthorizedResponse e) {
            throw e;
        }
        return game.createGame(gameName);
    }

    public Collection<AlternativeGameData> listGamesService(String token) throws DataAccessException{
        // Authenticate token
        try {
            userService.authenticateToken(token);
        } catch (UnauthorizedResponse e) {
            throw e;
        }
        return game.listGames();
    }

    public void joinGameService(String username, String playerColor, int gameId) throws DataAccessException {
        if (playerColor == null || ((!playerColor.equals("WHITE")) && (!playerColor.equals("BLACK")))) {
            throw new BadRequestResponse();
        }
        game.updateGame(username, playerColor, gameId);
    }

    public ChessGame getGameWithId(int gameId) throws DataAccessException {
        return game.getChessGame(gameId);
    }

    public void updateChessGame(ChessGame newGame, int gameId) throws DataAccessException, InvalidMoveException {
        game.updateChessGame(newGame, gameId);
    }

    public void removePlayer(int gameId, String username) throws DataAccessException {
        game.removeUser(username, gameId);
    }

}
