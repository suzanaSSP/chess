package dataaccess.interfaces;

import chess.ChessGame;
import model.GameData;
import server.handlers.requestsandresults.AlternativeGameData;

import java.util.Collection;

public interface GameDAO {
    void clear();

    int createGame(String gameName);

    Collection<AlternativeGameData> listGames();

    void updateGame(String username, String playerColor, int gameID);
}
