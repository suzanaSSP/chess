package dataaccess;

import dataaccess.interfaces.AuthDAO;
import dataaccess.interfaces.GameDAO;
import dataaccess.interfaces.UserDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.UserService;

import static org.junit.jupiter.api.Assertions.*;

public class AuthDAOTests {
    private UserDAO userDB = new SQLUserDAO();
    private AuthDAO authDB = new SQLAuthDAO();
    private GameDAO gameDB = new SQLGameDAO();

    private UserService testUserService = new UserService(userDB, authDB, gameDB);

    public AuthDAOTests() throws DataAccessException {
    }

    @BeforeEach
    public void setUp() throws DataAccessException {
        testUserService.clearService();
    }

    @Test
    public void clearTest() throws DataAccessException {
        testUserService.auth.createAuth("testUsername");

        testUserService.auth.clear();
        assertNull(testUserService.auth.getAuth("testUsername"));
    }

    @Test
    public void createAuthSuccessfully() throws DataAccessException {
        String newAuth = testUserService.auth.createAuth("testUsername");
        assertNotNull(testUserService.auth.getAuth(newAuth));
    }

    @Test
    public void createAuthException() {
        assertThrows(DataAccessException.class, () -> testUserService.auth.createAuth(null));
    }

    @Test
    public void getAuthSuccessfully() throws DataAccessException {
        String newAuth = testUserService.auth.createAuth("testUsername");
        assertEquals(newAuth, testUserService.auth.getAuth(newAuth).authToken());
    }

    @Test
    public void deleteAuthSuccessfully() throws DataAccessException {
        String newAuth = testUserService.auth.createAuth("testUsername");
        testUserService.auth.deleteAuth(newAuth);

        assertNull(testUserService.auth.getAuth(newAuth));
    }
}
