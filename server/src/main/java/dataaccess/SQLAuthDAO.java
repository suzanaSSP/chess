package dataaccess;

import dataaccess.interfaces.AuthDAO;
import model.AuthData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
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
        configureDatabase();

        var newAuth = UUID.randomUUID().toString();
        var statement = "INSERT INTO authorizations(authtoken, username) VALUES (?, ?)";
        int id = updateDatabase(statement, newAuth, username);
        return newAuth;
    };

    public AuthData getAuth(String username){
        return new AuthData(null, null);
    };
    public void deleteAuth(String username){

    };

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  authorizations (
              `authtoken` varchar(256) NOT NULL,
              `username` varchar(256) NOT NULL,
              PRIMARY KEY (`username`)
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
