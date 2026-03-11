package dataaccess;

import dataaccess.interfaces.AuthDAO;
import dataaccess.interfaces.GameDAO;
import dataaccess.interfaces.UserDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.handlers.requestsandresults.AlternativeGameData;
import services.GameServices;
import services.UserService;

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
    public void listGamesSuccessfully() throws DataAccessException {
        testGameService.game.createGame("new game");
        assertNotNull(testGameService.game.listGames());
    }

    @Test
    public void updateGameSuccessfully() throws DataAccessException {
        


    }

    @Test public void updateGameException(){

    }
}
