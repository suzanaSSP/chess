package dataaccess.interfaces;

import chess.ChessGame;

import java.util.Collection;

public interface GameDAO {
    ChessGame createGame();
    ChessGame getGame();
    Collection<ChessGame> listGames();
    void updateGame();
}
