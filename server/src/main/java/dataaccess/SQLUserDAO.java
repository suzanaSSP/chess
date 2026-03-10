package dataaccess;
import dataaccess.interfaces.UserDAO;
import model.UserData;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

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
                return 0;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error");
        }
    }

    public void clear() throws DataAccessException {
        var statement = "DROP TABLE IF EXISTS users";
        int response = updateDatabase(statement);

    }
    public UserData getUser(String username){
        return new UserData(null, null, null);
    }
    public UserData createUser(String username, String password, String email)  throws DataAccessException {
        configureDatabase();
        var statement = "INSERT INTO Users (username, password, email) VALUES (?, ?, ?)";
        int ID = updateDatabase(statement, username, password, email);
        return new UserData(username, password, email);
    }

    public void addToDatabase(UserData userData) {

    }
}
