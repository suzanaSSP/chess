package dataaccess;

import dataaccess.interfaces.AuthDAO;
import io.javalin.http.UnauthorizedResponse;
import model.AuthData;
import model.UserData;

import java.sql.*;
import java.util.UUID;

public class SQLAuthDAO implements AuthDAO {

    public SQLAuthDAO() throws DataAccessException {
        configureDatabase();
    }

    public void clear() throws DataAccessException {
        var statement = "TRUNCATE TABLE authorizations";
        updateDatabase(statement);
    };
    public String createAuth(String username) throws DataAccessException {
        var newAuth = UUID.randomUUID().toString();
        var statement = "INSERT INTO authorizations(authtoken, username) VALUES (?, ?)";
        updateDatabase(statement, newAuth, username);
        return newAuth;
    };

    public AuthData getAuth(String authToken) throws DataAccessException{
        try (Connection conn = DatabaseManager.getConnection()){
            var statement = "SELECT authtoken, username FROM authorizations WHERE authtoken = ?";
            try (PreparedStatement ps = conn.prepareStatement(statement)){
                ps.setString(1, authToken);
                try (ResultSet rs = ps.executeQuery()){
                    if (rs.next()){
                        return readAuth(rs);
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

    public AuthData readAuth(ResultSet rs) throws SQLException {
        String authToken = rs.getString("authtoken");
        String username = rs.getString("username");
        return new AuthData(authToken, username);
    }

    public void deleteAuth(String authToken) throws DataAccessException {
        var statement = "DELETE FROM authorizations WHERE authtoken = ?";
        updateDatabase(statement, authToken);
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS authorizations (
              `authtoken` varchar(256) NOT NULL,
              `username` varchar(256) NOT NULL,
              PRIMARY KEY (`authtoken`)
            ) 
            """
    };

    // Create database
    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (Connection conn = DatabaseManager.getConnection()) {
            for (String statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("failed to create database", ex);
        }
    }

    private int updateDatabase(String statement, Object... params) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()){
            try (PreparedStatement ps = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)){
                for (int i=0; i < params.length; i++) {
                    Object param = params[i];
                    if (param instanceof String p) {
                        ps.setString(i+1, p);
                    }
                }
                ps.executeUpdate();
                return 0;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error");
        }
    }

}
