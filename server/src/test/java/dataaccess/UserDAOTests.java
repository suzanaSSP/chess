package dataaccess;

import dataaccess.interfaces.AuthDAO;
import dataaccess.interfaces.GameDAO;
import dataaccess.interfaces.UserDAO;
import model.UserData;
import org.eclipse.jetty.server.Authentication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.handlers.requestsandresults.RegisterRequest;
import server.handlers.requestsandresults.RegisterResult;
import services.UserService;

import java.nio.channels.AlreadyBoundException;

import static org.junit.jupiter.api.Assertions.*;

public class UserDAOTests {
    private UserDAO userDB = new SQLUserDAO();
    private AuthDAO authDB = new SQLAuthDAO();
    private GameDAO gameDB = new SQLGameDAO();

    private UserService testUserService = new UserService(userDB, authDB, gameDB);

    public UserDAOTests() throws DataAccessException {
    }

    @BeforeEach
    public void setUp() throws DataAccessException {
        testUserService.clearService();
    }

    @Test
    public void registerSuccessfullyToDatabase() throws DataAccessException {
        RegisterRequest testUser = new RegisterRequest("newUsername", "newPassword", "newemail");
        testUserService.register(testUser);

        // Get user from database to see if it's there
        UserData expectedUser = testUserService.user.getUser(testUser.username());
        assertNotNull(expectedUser);
    }

    @Test
    public void registerExceptionToDatabase() throws DataAccessException {
        UserData existingUser = new UserData("existingUser", "existingPassword", "em@gmail.com");
        testUserService.user.addToDatabase(existingUser);
        RegisterRequest testUser = new RegisterRequest("existingUser", "existingPassword", "em@gmail.com");

        assertThrows(AlreadyBoundException.class, () -> testUserService.register(testUser));
    }

    @Test
    public void clearTest() throws DataAccessException {
        testUserService.user.addToDatabase(new UserData("test", "test", "test"));

        testUserService.user.clear();
        assertNull(testUserService.user.getUser("test"));
    }

    @Test
    public void getUserSuccessfully() throws DataAccessException {
        UserData expectedUser = new UserData("username", "password", "email");
        testUserService.user.addToDatabase(expectedUser);

        UserData testUser = testUserService.user.getUser("username");
        Assertions.assertEquals(expectedUser.email(), testUser.email());
    }

    @Test
    public void getUserException() throws DataAccessException {
        assertNull(testUserService.user.getUser("nonExistingUsername"));
    }

    @Test
    public void addToDatabaseSuccessfully() throws DataAccessException {
        UserData expectedUser = new UserData("username", "password", "email");
        testUserService.user.addToDatabase(expectedUser);
        UserData testUser = testUserService.user.getUser("username");
        assertEquals(expectedUser.email(), testUser.email());
    }

    @Test
    public void addToDatabaseException() throws DataAccessException {
        UserData expectedUser = new UserData(null, null,null);
        assertThrows(DataAccessException.class, () ->  testUserService.user.addToDatabase(expectedUser));
    }
}
