package dataaccess.memorydao;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.interfaces.GameDAO;
import io.javalin.http.BadRequestResponse;
import model.GameData;
import requestsandresults.AlternativeGameData;

import java.nio.channels.AlreadyBoundException;
import java.util.*;

public class MemoryGameDAO implements GameDAO {
    Map<Integer, GameData> gameDatabase = new HashMap<>();
    // <gameID, GameData object>

    public void clear(){
        gameDatabase = new HashMap<>();}

    public int createGame(String gameName){
        // Create ID
        Random rand = new Random();
        int newID = rand.nextInt(9000);
        // Create empty game
        ChessGame game = new ChessGame();

        //Add to database
        GameData newGame = new GameData(newID, null, null, gameName, game);
        gameDatabase.put(newID, newGame);
        return newID;
    }

    public Collection<AlternativeGameData> listGames(){
        // I am only doing this long complicated way because the autograded said so.
        // My other way was more simple and it was working.
        Collection<AlternativeGameData> games = new ArrayList<>();
        for (GameData game : gameDatabase.values()){
            AlternativeGameData newGame = new AlternativeGameData(game.gameID(), game.whiteUsername(), game.blackUsername(), game.gameName());
            games.add(newGame);
        }
        return games;
    }

    public GameData createNewGameData(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game){
        return new GameData(gameID, whiteUsername, blackUsername, gameName, game);
    }

    public GameData getGame(int gameID)  {
        GameData game = gameDatabase.get(gameID);
        if (game == null) {
            throw new BadRequestResponse();
        }
        return game;
    }

    public void updateGame(String username, String playerColor, int gameID) {
        GameData game = getGame(gameID);

        switch (playerColor) {
            case "WHITE":
                if (game.whiteUsername() != null) {
                throw new AlreadyBoundException();
                }
                GameData whiteGame = createNewGameData(gameID, username, game.blackUsername(), game.gameName(), game.game());
                gameDatabase.replace(gameID, whiteGame);
                return;

            case "BLACK":
                if (game.blackUsername() != null) {
                    throw new AlreadyBoundException();
                }
                GameData blackGame = createNewGameData(gameID, game.whiteUsername(), username, game.gameName(), game.game());
                gameDatabase.replace(gameID, blackGame);
        }
    }

    public ChessGame getChessGame(int gameID) throws DataAccessException{
        return new ChessGame();
    }


}
