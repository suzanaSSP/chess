package services;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import io.javalin.http.BadRequestResponse;
import model.GameData;

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

    public Collection<GameData> listGamesService(){
        return game.listGames();
    }

    public void joinGameService(String username, String player_color, int gameId) {
        if (player_color == null || ((!player_color.equals("WHITE")) && (!player_color.equals("BLACK")))) {
            throw new BadRequestResponse();
        }
        game.updateGame(username, player_color, gameId);


    }

}
