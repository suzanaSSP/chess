package dataaccess.interfaces;

import chess.ChessGame;
import dataaccess.DataAccessException;
import model.GameData;
import server.handlers.requestsandresults.AlternativeGameData;

import java.util.Collection;

public interface GameDAO {
    void clear() throws DataAccessException;
    int createGame(String gameName) throws DataAccessException;
    Collection<AlternativeGameData> listGames() throws DataAccessException;
    void updateGame(String username, String playerColor, int gameID) throws DataAccessException;
}

