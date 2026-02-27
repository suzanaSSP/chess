package service;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import io.javalin.http.UnauthorizedResponse;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.handlers.requestsandresults.LoginRequest;
import server.handlers.requestsandresults.LoginResult;
import server.handlers.requestsandresults.RegisterRequest;
import server.handlers.requestsandresults.RegisterResult;
import services.UserService;

import java.nio.channels.AlreadyBoundException;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTests {
    private MemoryUserDAO expectedDataBase = new MemoryUserDAO();
    private MemoryUserDAO actualDataBase = new MemoryUserDAO();
    private MemoryAuthDAO authData = new MemoryAuthDAO();
    private UserService testService = new UserService(actualDataBase, authData, new MemoryGameDAO());

    @BeforeEach
    public void setUp(){
        expectedDataBase.clear();
        actualDataBase.clear();
    }

    @Test
    public void registerSuccessfully(){
        // How expected database should be
        UserData newUser = new UserData("newUsername", "newPassword", "newEmail");
        expectedDataBase.addToDatabase(newUser);

        // Create user to register
        RegisterRequest testRequest = new RegisterRequest("newUsername", "newPassword", "newEmail");
        testService.register(testRequest);
        actualDataBase = testService.dataaccess;

        Assertions.assertEquals(expectedDataBase.userDatabase, actualDataBase.userDatabase);
    }

    @Test
    public void registerException(){
        // Add user to database to throw error
        UserData existingUser = new UserData("existingUsername", "existingPassword", "existingEmail");
        actualDataBase.addToDatabase(existingUser);

        RegisterRequest testRequest = new RegisterRequest("existingUsername", "existingPassword", "existingEmail");
        assertThrows(AlreadyBoundException.class, () -> testService.register(testRequest));
    }

    @Test
    public void clear(){
        UserData testUser = new UserData("username", "password", "email");
        actualDataBase.addToDatabase(testUser);
        testService.clearService();

        Assertions.assertEquals(expectedDataBase.userDatabase, actualDataBase.userDatabase);
    }

    @Test
    public void loginSuccessfully(){
        // Add user to database
        testService.register(new RegisterRequest("username", "password", "email"));

        LoginResult result = testService.loginService(new LoginRequest("username", "password"));
        // store auth token somewhere and assert true that it exists
        assertNotNull(result.authToken());

    }

    @Test
    public void loginException(){
        LoginRequest testRequest = new LoginRequest("unexistingUser", "unexistingPassword");
        assertThrows(UnauthorizedResponse.class, () -> testService.loginService(testRequest));
    }

    @Test
    public void logoutSuccessfully(){
        // Add user to database
        RegisterResult result = testService.register(new RegisterRequest("username", "password", "email"));
        testService.logoutService(result.authToken());
        assertTrue(authData.authDatabase.isEmpty());
    }

    @Test
    public void logoutException(){
        assertThrows(UnauthorizedResponse.class, () -> testService.logoutService("adkfhdjfakh"));
    }

    @Test
    public void authenticateUserSuccessfully(){
        testService.register(new RegisterRequest("username", "password", "email"));
        assertNotNull(authData.authDatabase);
    }

    @Test
    public void authenticateUserException(){
        assertThrows(UnauthorizedResponse.class, () -> testService.authenticateToken("skbjfhs"));
    }

    @Test
    public void getUserSuccessfully(){
        RegisterResult result = testService.register(new RegisterRequest("username", "password", "email"));
        assertNotNull(testService.getUserWithAuth(result.authToken()));
    }

    @Test
    public void getUserException(){
        assertThrows(UnauthorizedResponse.class, ()-> testService.getUserWithAuth("sfjhkjds"));
    }
}
