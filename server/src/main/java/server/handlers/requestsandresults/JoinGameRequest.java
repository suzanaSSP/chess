package server.handlers.requestsandresults;

import chess.ChessGame;

public record JoinGameRequest(String playerColor, int gameID) {
}
