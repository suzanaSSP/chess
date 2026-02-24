package services;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import io.javalin.http.BadRequestResponse;
import model.GameData;
import server.handlers.requestsandresults.AlternativeGameData;

import java.util.Collection;

public class GameServices {
    MemoryAuthDAO auth;
    MemoryUserDAO dataaccess;
    MemoryGameDAO game;

    public GameServices(MemoryUserDAO userDataBase, MemoryAuthDAO authDataBase, MemoryGameDAO g){
        auth = authDataBase;
        dataaccess = userDataBase;
        game = g;
    }

    public Integer createGameServices(String gameName){
        return game.createGame(gameName);
    }

    public Collection<AlternativeGameData> listGamesService(){
        return game.listGames();
    }

    public void joinGameService(String username, String playerColor, int gameId) {
        if (playerColor == null || ((!playerColor.equals("WHITE")) && (!playerColor.equals("BLACK")))) {
            throw new BadRequestResponse();
        }
        game.updateGame(username, playerColor, gameId);


    }

}
