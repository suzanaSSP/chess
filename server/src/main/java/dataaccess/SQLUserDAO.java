package dataaccess;
import dataaccess.interfaces.UserDAO;
import model.UserData;

import java.nio.channels.AlreadyBoundException;
import java.sql.*;

public class SQLUserDAO implements UserDAO {

    public SQLUserDAO() throws DataAccessException {
        configureDatabase();
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  users (
              `username` varchar(256) NOT NULL,
              `password` varchar(256) NOT NULL,
              `email` varchar(256) NOT NULL,
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

                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()){
                    return rs.getInt(1);
                }
                return 0;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error");
        }
    }

    public void clear() throws DataAccessException {
        var statement = "TRUNCATE TABLE users";
        updateDatabase(statement);

    }
    public UserData getUser(String userName) throws DataAccessException{
        try (Connection conn = DatabaseManager.getConnection()){
            var statement = "SELECT username, password, email FROM users WHERE username = ?";
            try (PreparedStatement ps = conn.prepareStatement(statement)){
                ps.setString(1, userName);
                try (ResultSet rs = ps.executeQuery()){
                    if (rs.next()){
                        return readUser(rs);
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

    public UserData readUser(ResultSet rs) throws SQLException {
        String username = rs.getString("username");
        String password = rs.getString("password");
        String email = rs.getString("email");
        return new UserData(username, password, email);

    }

    public UserData createUser(String username, String password, String email)  throws DataAccessException {
        // Check is user already exists
        UserData test_user = getUser(username);
        if (test_user != null){
            throw new AlreadyBoundException();
        }
        var statement = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
        updateDatabase(statement, username, password, email);
        return new UserData(username, password, email);
    }

    public void addToDatabase(UserData userData) throws DataAccessException {
        var statement = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
        updateDatabase(statement, userData.username(), userData.password(), userData.email());
    }

}
