package service;

import dataaccess.DataAccessException;
import dataaccess.interfaces.UserDAO;
import dataaccess.memorydao.MemoryAuthDAO;
import dataaccess.memorydao.MemoryGameDAO;
import dataaccess.memorydao.MemoryUserDAO;
import io.javalin.http.UnauthorizedResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.handlers.requestsandresults.RegisterRequest;
import server.handlers.requestsandresults.RegisterResult;
import services.GameServices;
import services.UserService;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GameServiceTest {
    UserDAO userDataBase = new MemoryUserDAO();
    MemoryGameDAO actualGameDB = new MemoryGameDAO();
    MemoryAuthDAO authDatabase = new MemoryAuthDAO();
    UserService testUserService = new UserService(userDataBase, authDatabase, actualGameDB);
    GameServices testGameService = new GameServices(userDataBase, authDatabase, actualGameDB, testUserService);


    // To run tests
    MemoryGameDAO expectedGameDB = new MemoryGameDAO();

    @BeforeEach
    public void setUp() throws DataAccessException {
        userDataBase.clear();
        actualGameDB.clear();
    }

    @Test
    public void createGameSuccessfully() throws DataAccessException{
        // Add new user
        RegisterRequest testRequest = new RegisterRequest("username", "password", "email");
        RegisterResult result = testUserService.register(testRequest);

        testGameService.createGameServices("gameName", result.authToken());
        assertNotNull(testGameService.game);
    }

    @Test
    public void createGameException(){
        // User doesn't exist
        assertThrows(UnauthorizedResponse.class, () -> testGameService.createGameServices("gameError",
                "safdagfgs"));
    }

}
