package dataaccess;

import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataaccess.interfaces.GameDAO;
import io.javalin.http.BadRequestResponse;
import requestsandresults.AlternativeGameData;

import java.nio.channels.AlreadyBoundException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

public class SQLGameDAO implements GameDAO {
    Gson gson = new Gson();

    public SQLGameDAO() throws DataAccessException {
        DatabaseManager.createDatabaseAndTables(createStatements);
    }

    public void clear() throws DataAccessException{
        try (Connection conn = DatabaseManager.getConnection()){
            var statement = "TRUNCATE TABLE games";
            try (PreparedStatement ps = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)){
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error");
        }
    }

    public int createGame(String gameName) throws DataAccessException {
        // Create ID
        Random rand = new Random();
        int newID = rand.nextInt(9000);
        // Create empty game
        ChessGame game = new ChessGame();
        String jsonGame = toJsonGame(game);

        try (Connection conn = DatabaseManager.getConnection()){
            var statement = "INSERT INTO games (gameID, gameName, game) VALUES (?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(statement)){
                ps.setInt(1, newID);
                ps.setString(2, gameName);
                ps.setString(3, jsonGame);

                ps.executeUpdate();
            }
        } catch (Exception e) {
            throw new DataAccessException("Database Error");
        }

        return newID;
    }

    private String toJsonGame(ChessGame game) {
        return gson.toJson(game);
    }

    public Collection<AlternativeGameData> listGames() throws DataAccessException {
        Collection<AlternativeGameData> allGames = new ArrayList<>();

        try (Connection conn = DatabaseManager.getConnection()){
            var statement = "SELECT gameID, whiteUsername, blackUsername, gameName from games";
            try (PreparedStatement ps = conn.prepareStatement(statement)){
                try (ResultSet rs = ps.executeQuery()){
                    while (rs.next()){
                        AlternativeGameData newGame = readGame(rs);
                        allGames.add(newGame);
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException("Database Error");
        }

        return allGames;
    }

    private AlternativeGameData readGame(ResultSet rs) throws SQLException {
        int gameID = rs.getInt("gameID");
        String whiteUsername = rs.getString("whiteUsername");
        String blackUsername = rs.getString("blackUsername");
        String gameName = rs.getString("gameName");

        return new AlternativeGameData(gameID, whiteUsername, blackUsername, gameName);

    }

    public void updateGame(String username, String playerColor, int gameID) throws DataAccessException {
        // Check if game exists
        AlternativeGameData game = getGame(gameID);
        if (game == null){
            throw new BadRequestResponse();
        }

        String statement;
        if (playerColor.equals("WHITE")){
            statement = "UPDATE games SET whiteUsername = ? WHERE gameID = ? AND whiteUsername IS NULL";
        } else {
            statement = "UPDATE games SET blackUsername = ? WHERE gameID = ? AND blackUsername IS NULL";
        }

        try (Connection conn = DatabaseManager.getConnection()){
            try (PreparedStatement ps = conn.prepareStatement(statement)){
                ps.setString(1, username);
                ps.setInt(2, gameID);
                int rowsAffected = ps.executeUpdate();

                // player taken in game
                if (rowsAffected == 0) {
                    throw new AlreadyBoundException();
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Database Error");
        }
    }

    public void removeUser(String username, int gameID) throws DataAccessException {
        AlternativeGameData game = getGame(gameID);
        if (game == null){
            throw new BadRequestResponse();
        }

        String statement;
        statement = "UPDATE games SET whiteUsername = CASE WHEN whiteUsername = ? THEN null END, " +
                "blackUsername = CASE WHEN blackUsername = ? THEN null END WHERE gameID = ? AND (whiteUsername = ? OR blackUsername =?)";

        try (Connection conn = DatabaseManager.getConnection()){
            try (PreparedStatement ps = conn.prepareStatement(statement)){
                ps.setString(1, username);
                ps.setString(2, username);
                ps.setInt(3, gameID);
                ps.setString(4, username);
                ps.setString(5, username);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Database Error");
        }
    }

    private AlternativeGameData getGame(int gameID) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()){
            var statement = "SELECT gameID, whiteUsername, blackUsername, gameName FROM games WHERE gameID = ?";
            try (PreparedStatement ps = conn.prepareStatement(statement)){
                ps.setInt(1, gameID);
                try (ResultSet rs = ps.executeQuery()){
                    if (rs.next()){
                        return readGame(rs);
                    }
                    else {
                        return null;
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException("Database error");
        }
    }

    public ChessGame getChessGame(int gameID) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()){
            var statement = "SELECT game FROM games WHERE gameID = ?";
            try (PreparedStatement ps = conn.prepareStatement(statement)){
                ps.setInt(1, gameID);
                try (ResultSet rs = ps.executeQuery()){
                    if (rs.next()){
                        String jsonGame = rs.getString("game");
                        return gson.fromJson(jsonGame, ChessGame.class);
                    }
                    else {
                        return null;
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException("Database error");
        }
    }

    public ChessGame updateChessGame(int gameID, ChessMove move) throws DataAccessException, InvalidMoveException {
        ChessGame gameFetched = getChessGame(gameID);
        gameFetched.makeMove(move);
        String jsonGame = toJsonGame(gameFetched);

        String statement = "UPDATE games SET game = ? WHERE gameID = ?";
        try (Connection conn = DatabaseManager.getConnection()){
            try (PreparedStatement ps = conn.prepareStatement(statement)){
                ps.setString(1, jsonGame);
                ps.setInt(2, gameID);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Database Error");
        }
        return gameFetched;
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS games (
              `gameID` INT NOT NULL,
              `whiteUsername` varchar(256) DEFAULT NULL,
              `blackUsername` varchar(256) DEFAULT NULL,
              `gameName` varchar(256) NOT NULL,
              `game` TEXT NOT NULL,
              PRIMARY KEY (`gameID`)
            )
            """
    };
}
