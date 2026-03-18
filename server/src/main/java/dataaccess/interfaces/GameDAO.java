package dataaccess.interfaces;

import dataaccess.DataAccessException;
import requestsandresults.AlternativeGameData;

import java.util.Collection;

public interface GameDAO {
    void clear() throws DataAccessException;
    int createGame(String gameName) throws DataAccessException;
    Collection<AlternativeGameData> listGames() throws DataAccessException;
    void updateGame(String username, String playerColor, int gameID) throws DataAccessException;
}

