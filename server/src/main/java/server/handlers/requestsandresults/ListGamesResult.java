package server.handlers.requestsandresults;

import model.GameData;

import java.util.Collection;

public record ListGamesResult(Collection<AlternativeGameData> games) {
}
