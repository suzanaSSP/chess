package server.handlers.requests_and_results;

import model.GameData;

import java.util.Collection;

public record ListGamesResult(Collection<GameData> games) {
}
