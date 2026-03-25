package client;

import dataaccess.DataAccessException;
import dataaccess.SQLAuthDAO;
import dataaccess.SQLGameDAO;
import dataaccess.SQLUserDAO;
import org.junit.jupiter.api.*;
import requestsandresults.ListGamesResult;
import requestsandresults.LoginResult;
import requestsandresults.RegisterResult;
import server.Server;
import services.UserService;
import java.io.IOException;
import java.net.URISyntaxException;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade serverFacadeTest = new ServerFacade();
    static UserService userServiceTest;

    static {
        try {
            userServiceTest = new UserService(new SQLUserDAO(), new SQLAuthDAO(), new SQLGameDAO());
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public ServerFacadeTests() {
    }

    @BeforeAll
    public static void init() throws URISyntaxException, IOException, InterruptedException {
        server = new Server();
        var port = server.run(8080);

        System.out.println("Started test HTTP server on " + port);
    }

    @BeforeEach
    public void setUp() throws URISyntaxException, IOException, InterruptedException {
        serverFacadeTest.clearServerFacade();
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
    public void registerException() {
        Assertions.assertThrows(ClientExceptions.class, () -> serverFacadeTest.registerServerFacade(null, null, null));
    }

    @Test
    public void loginSuccessfully() throws URISyntaxException, IOException, InterruptedException {
        serverFacadeTest.registerServerFacade("username", "password", "email");
        LoginResult result = serverFacadeTest.loginServerFacade("username", "password");
        Assertions.assertNotNull(result);
        Assertions.assertEquals("username", result.username());
    }

    @Test
    public void loginException() {
        Assertions.assertThrows(ClientExceptions.class, () -> serverFacadeTest.loginServerFacade(null, null));
    }

    @Test
    public void createGameSuccessfully() throws URISyntaxException, IOException, InterruptedException {
        RegisterResult signUpResult = serverFacadeTest.registerServerFacade("username", "password", "email");
        Assertions.assertNotNull(serverFacadeTest.createGameServerFacade(signUpResult.authToken(), "myGame"));
    }

    @Test
    public void createGameException() {
        Assertions.assertThrows(ClientExceptions.class, ()-> serverFacadeTest.createGameServerFacade("1234", null));
    }

    @Test
    public void listGamesSuccessfully() throws URISyntaxException, IOException, InterruptedException {
        RegisterResult signUpResult = serverFacadeTest.registerServerFacade("username", "password", "email");
        serverFacadeTest.createGameServerFacade(signUpResult.authToken(), "myGame");
        ListGamesResult games = serverFacadeTest.listGamesServerFacade(signUpResult.authToken());
        Assertions.assertNotNull(games);
    }

    @Test
    public void listGamesException() {
        Assertions.assertThrows(ClientExceptions.class, () -> serverFacadeTest.listGamesServerFacade("123"));
    }

    @Test
    public void logoutSuccessfully() throws URISyntaxException, IOException, InterruptedException {
        RegisterResult result = serverFacadeTest.registerServerFacade("username", "password", "email");
        serverFacadeTest.logoutServerFacade(result.authToken());
        Assertions.assertThrows(ClientExceptions.class, ()-> serverFacadeTest.listGamesServerFacade(result.authToken()));
    }

    @Test
    public void logoutException() {
        Assertions.assertThrows(ClientExceptions.class, () -> serverFacadeTest.logoutServerFacade("1234"));
    }

    @Test
    public void clearSuccessfully() throws URISyntaxException, IOException, InterruptedException {
        serverFacadeTest.registerServerFacade("user", "password", "email");
        serverFacadeTest.clearServerFacade();
        Assertions.assertThrows(ClientExceptions.class, ()-> serverFacadeTest.loginServerFacade("user", "password"));
    }

    @Test
    public void joinGameSuccessfully() throws URISyntaxException, IOException, InterruptedException {
        RegisterResult signUpResult = serverFacadeTest.registerServerFacade("username", "password", "email");
        int gameID = serverFacadeTest.createGameServerFacade(signUpResult.authToken(), "myGame");
        serverFacadeTest.joinGameServerFacade("WHITE", gameID, signUpResult.authToken());

        Assertions.assertThrows(ClientExceptions.class, ()-> serverFacadeTest.joinGameServerFacade("WHITE", gameID, signUpResult.authToken()));
    }

    @Test
    public void joinGameException() throws URISyntaxException, IOException, InterruptedException {
        RegisterResult signUpResult = serverFacadeTest.registerServerFacade("username", "password", "email");
        Assertions.assertThrows(ClientExceptions.class, ()-> serverFacadeTest.joinGameServerFacade("WHITE", 1234, signUpResult.authToken()));
    }

}
