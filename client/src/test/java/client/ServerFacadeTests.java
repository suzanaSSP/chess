package client;

import dataaccess.DataAccessException;
import dataaccess.SQLAuthDAO;
import dataaccess.SQLGameDAO;
import dataaccess.SQLUserDAO;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.UnauthorizedResponse;
import org.junit.jupiter.api.*;
import requestsandresults.ListGamesResult;
import requestsandresults.LoginResult;
import requestsandresults.RegisterResult;
import server.Server;
import server.ServerMain;
import services.UserService;

import java.io.IOException;
import java.net.URISyntaxException;


public class ServerFacadeTests {

    private static Server server;
    ServerFacade serverFacadeTest = new ServerFacade();
    static UserService userServiceTest;

    static {
        try {
            userServiceTest = new UserService(new SQLUserDAO(), new SQLAuthDAO(), new SQLGameDAO());
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public ServerFacadeTests() throws DataAccessException {
    }

    @BeforeAll
    public static void init() throws DataAccessException {
        server = new Server();
        var port = server.run(8080);
        userServiceTest.clearService();
        System.out.println("Started test HTTP server on " + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void sampleTest() {
        Assertions.assertTrue(true);
    }

    @Test
    public void registerSuccessfully() throws URISyntaxException, IOException, InterruptedException {
        RegisterResult result = serverFacadeTest.registerServerFacade("username", "password", "email");
        Assertions.assertNotNull(result);
    }

    @Test
    public void registerException() throws URISyntaxException, IOException, InterruptedException {
        Assertions.assertThrows(BadRequestResponse.class, () -> serverFacadeTest.registerServerFacade(null, null, null));
    }

    @Test
    public void loginSuccessfully() throws URISyntaxException, IOException, InterruptedException {
        serverFacadeTest.registerServerFacade("username", "password", "email");
        LoginResult result = serverFacadeTest.loginServerFacade("username", "password");
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.username().equals("username"));
    }

    @Test
    public void loginException() {
        Assertions.assertThrows(BadRequestResponse.class, () -> serverFacadeTest.loginServerFacade(null, null));
    }

    @Test
    public void createGameSuccessfully() throws URISyntaxException, IOException, InterruptedException {
        RegisterResult signUpResult = serverFacadeTest.registerServerFacade("username", "password", "email");
        String gameID = serverFacadeTest.createGameServerFacade(signUpResult.authToken(), "myGame");
        Assertions.assertNotNull(gameID);
    }

    @Test
    public void createGameException() {
        Assertions.assertThrows(BadRequestResponse.class, ()-> serverFacadeTest.createGameServerFacade("1234", null));
    }

    @Test
    public void listGamesSuccessfully() throws URISyntaxException, IOException, InterruptedException {
        RegisterResult signUpResult = serverFacadeTest.registerServerFacade("username", "password", "email");
        String gameID = serverFacadeTest.createGameServerFacade(signUpResult.authToken(), "myGame");
        ListGamesResult games = serverFacadeTest.listGamesServerFacade(signUpResult.authToken());
        Assertions.assertNotNull(games);
    }

    @Test
    public void listGamesException() {
        Assertions.assertThrows(UnauthorizedResponse.class, () -> serverFacadeTest.listGamesServerFacade("123"));
    }

}
