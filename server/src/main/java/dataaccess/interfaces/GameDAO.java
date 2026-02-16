package dataaccess.interfaces;

import chess.ChessGame;

import java.util.Collection;

public interface GameDAO {
    void clear();
    int createGame(String gameName);
    ChessGame getGame();
    Collection<ChessGame> listGames();
    void updateGame();
}
