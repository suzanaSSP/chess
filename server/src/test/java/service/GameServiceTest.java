package service;

import chess.ChessGame;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import io.javalin.http.UnauthorizedResponse;
import model.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.handlers.requestsandresults.RegisterRequest;
import server.handlers.requestsandresults.RegisterResult;
import services.GameServices;
import services.UserService;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GameServiceTest {
    MemoryUserDAO userDataBase = new MemoryUserDAO();
    MemoryGameDAO actualGameDB = new MemoryGameDAO();
    MemoryAuthDAO authDatabase = new MemoryAuthDAO();
    UserService testUserService = new UserService(userDataBase, authDatabase, actualGameDB);
    GameServices testGameService = new GameServices(userDataBase, authDatabase, actualGameDB, testUserService);


    // To run tests
    MemoryGameDAO expectedGameDB = new MemoryGameDAO();

    @BeforeEach
    public void setUp(){
        userDataBase.clear();
        actualGameDB.clear();
    }

    @Test
    public void createGameSuccessfully(){
        // Add new user
        RegisterRequest testRequest = new RegisterRequest("username", "password", "email");
        RegisterResult result = testUserService.register(testRequest);

        testGameService.createGameServices("gameName", result.authToken());
        assertNotNull(testGameService.game);
    }

    @Test
    public void createGameException(){
        // User doesn't exist
        assertThrows(UnauthorizedResponse.class, () -> testGameService.createGameServices("gameError", "safdagfgs"));
    }

    @Test
    public void listGamesSuccessfully(){

    }

}
