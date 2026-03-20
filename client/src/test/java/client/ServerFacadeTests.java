package client;

import dataaccess.DataAccessException;
import dataaccess.SQLAuthDAO;
import dataaccess.SQLGameDAO;
import dataaccess.SQLUserDAO;
import org.junit.jupiter.api.*;
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
        serverFacadeTest.registerServerFacade("existingUsername", "existingPassword", "existingEmail");
    }

}
