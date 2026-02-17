package dataaccess;

import chess.ChessGame;
import dataaccess.interfaces.GameDAO;
import io.javalin.http.BadRequestResponse;
import model.GameData;
import server.handlers.requests_and_results.AlternativeGameData;

import java.nio.channels.AlreadyBoundException;
import java.util.*;

public class MemoryGameDAO implements GameDAO {
    Map<Integer, GameData> game_database = new HashMap<>();
    // <gameID, GameData object>

    public void clear(){game_database = new HashMap<>();}
    public int createGame(String gameName){
        // Create ID
        Random rand = new Random();
        int new_ID = rand.nextInt(9000);
        // Create empty game
        ChessGame game = new ChessGame();

        //Add to database
        GameData new_game = new GameData(new_ID, null, null, gameName, game);
        game_database.put(new_ID, new_game);
        return new_ID;
    }

    public Collection<AlternativeGameData> listGames(){
        // I am only doing this long complicated way because the autograded said so. My other way was more simple and it was working.
        Collection<AlternativeGameData> games = new ArrayList<>();
        for (GameData game : game_database.values()){
            AlternativeGameData new_game = new AlternativeGameData(game.gameID(), game.whiteUsername(), game.blackUsername(), game.gameName());
            games.add(new_game);
        }
        return games;
    }

    public GameData createNewGameData(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game){
        return new GameData(gameID, whiteUsername, blackUsername, gameName, game);
    }

    public GameData getGame(int gameID)  {
        GameData game = game_database.get(gameID);
        if (game == null) {
            throw new BadRequestResponse();
        }
        return game;
    }

    public void updateGame(String username_, String player_color, int gameID) {
        GameData game = getGame(gameID);

        switch (player_color) {
            case "WHITE":
                if (game.whiteUsername() != null) {
                throw new AlreadyBoundException();
                }
                GameData white_game = createNewGameData(gameID, username_, game.blackUsername(), game.gameName(), game.game());
                game_database.replace(gameID, white_game);
                return;

            case "BLACK":
                if (game.blackUsername() != null) {
                    throw new AlreadyBoundException();
                }
                GameData black_game = createNewGameData(gameID, game.whiteUsername(), username_, game.gameName(), game.game());
                game_database.replace(gameID, black_game);
        }

    }

}
