package dataaccess.interfaces;

import chess.ChessGame;
import dataaccess.DataAccessException;
import model.GameData;

import java.util.Collection;

public interface GameDAO {
    void clear();
    int createGame(String gameName);
    Collection<GameData> listGames();
    void updateGame(String username, String player_color, int gameID) throws DataAccessException;
}
