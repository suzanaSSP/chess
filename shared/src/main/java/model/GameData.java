package model;

import chess.ChessGame;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({ "game" })
public record GameData(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {
}
