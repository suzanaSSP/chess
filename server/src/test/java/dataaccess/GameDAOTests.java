package dataaccess;

import dataaccess.interfaces.AuthDAO;
import dataaccess.interfaces.GameDAO;
import dataaccess.interfaces.UserDAO;
import io.javalin.http.BadRequestResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.handlers.requestsandresults.AlternativeGameData;
import services.GameServices;
import services.UserService;

import java.nio.channels.AlreadyBoundException;

import static org.junit.jupiter.api.Assertions.*;

public class GameDAOTests {
    private UserDAO userDB = new SQLUserDAO();
    private AuthDAO authDB = new SQLAuthDAO();
    private GameDAO gameDB = new SQLGameDAO();

    private UserService testUserService = new UserService(userDB, authDB, gameDB);
    private GameServices testGameService = new GameServices(userDB, authDB, gameDB, testUserService);

    public GameDAOTests() throws DataAccessException {
    }

    @BeforeEach
    public void setUp() throws DataAccessException {
        testUserService.clearService();
    }

    @Test
    public void clearTest() throws DataAccessException {
       testGameService.game.createGame("new game");
       testGameService.game.clear();

       assertTrue(testGameService.game.listGames().isEmpty());
    }

    @Test
    public void createGameSuccessfully() throws DataAccessException {
        testGameService.game.createGame("new game");
        assertNotNull(testGameService.game.listGames());
    }

    @Test
    public void createGameException() throws DataAccessException {
        assertThrows(DataAccessException.class, () -> testGameService.game.createGame(null));

    }

    @Test
    public void listGamesSuccessfully() throws DataAccessException {
        testGameService.game.createGame("new game");
        assertNotNull(testGameService.game.listGames());
    }

    @Test
    public void listGamesException() throws DataAccessException {
        assertTrue(testGameService.game.listGames().isEmpty());
    }

    @Test
    public void updateGameSuccessfully() throws DataAccessException {
        int newGameId = testGameService.game.createGame("new game");
        testGameService.game.updateGame("new username", "WHITE", newGameId);
        assertThrows(AlreadyBoundException.class, () -> testGameService.game.updateGame("other username", "WHITE", newGameId));
    }

    @Test public void updateGameException() throws DataAccessException {
        assertThrows(BadRequestResponse.class, () -> testGameService.game.updateGame("new username", "BLACK", 1234));
    }
}
