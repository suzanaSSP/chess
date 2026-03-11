package dataaccess;
import dataaccess.interfaces.UserDAO;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.nio.channels.AlreadyBoundException;
import java.sql.*;

public class SQLUserDAO implements UserDAO {

    public SQLUserDAO() throws DataAccessException {
        DatabaseManager.createDatabaseAndTables(createStatements);
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  users (
              `username` varchar(256) NOT NULL,
              `hashedPassword` varchar(256) NOT NULL,
              `email` varchar(256) NOT NULL,
              PRIMARY KEY (`username`)
            )
            """
    };

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
            var statement = "SELECT * FROM users WHERE username = ?";
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
        String password = rs.getString("hashedPassword");
        String email = rs.getString("email");

        //returns hashed password instead of original one
        return new UserData(username, password, email);

    }

    public UserData createUser(String username, String password, String email)  throws DataAccessException {
        // Check is user already exists
        UserData testUser = getUser(username);
        if (testUser != null){
            throw new AlreadyBoundException();
        }
        var statement = "INSERT INTO users (username, hashedPassword, email) VALUES (?, ?, ?)";

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        updateDatabase(statement, username, hashedPassword, email);

        return new UserData(username, hashedPassword, email);
    }


    public void addToDatabase(UserData userData) throws DataAccessException {
        var statement = "INSERT INTO users (username, hashedPassword, email) VALUES (?, ?, ?)";
        String hashedPassword = BCrypt.hashpw(userData.password(), BCrypt.gensalt());
        updateDatabase(statement, userData.username(), hashedPassword, userData.email());
    }

}
