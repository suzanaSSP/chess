package server.handlers.requests_and_results;

import chess.ChessGame;

public record JoinGameRequest(String playerColor, int gameID) {
}
