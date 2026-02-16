package dataaccess;

import chess.ChessGame;
import dataaccess.interfaces.GameDAO;
import model.GameData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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
        System.out.println(game_database);
        return new_ID;
    }

    public ChessGame getGame(){
        ChessGame test = new ChessGame();
        return test;
    }
    public Collection<ChessGame> listGames(){
        Collection<ChessGame> test = new ArrayList<>();
        return test;
    }
    public void updateGame(){
        System.out.println("Hi");
    }
}
