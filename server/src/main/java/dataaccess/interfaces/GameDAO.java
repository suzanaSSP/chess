package dataaccess.interfaces;

import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import dataaccess.DataAccessException;
import requestsandresults.AlternativeGameData;

import java.util.Collection;

public interface GameDAO {
    void clear() throws DataAccessException;
    int createGame(String gameName) throws DataAccessException;
    Collection<AlternativeGameData> listGames() throws DataAccessException;
    void updateGame(String username, String playerColor, int gameID) throws DataAccessException;
    ChessGame getChessGame(int gameID) throws DataAccessException;
    void updateChessGame(ChessGame newGame, int gameID) throws DataAccessException, InvalidMoveException;
    void removeUser(String playerColor, int gameID) throws DataAccessException;
}

