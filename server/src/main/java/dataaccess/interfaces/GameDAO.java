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
    ChessGame updateChessGame(int gameID, ChessMove move) throws DataAccessException, InvalidMoveException;
}

