package server;

import com.google.gson.Gson;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import io.javalin.*;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.UnauthorizedResponse;
import server.handlers.*;
import services.GameServices;
import services.UserService;

import java.nio.channels.AlreadyBoundException;
import java.util.Map;


public class Server {

    private final Javalin javalin;
    public MemoryUserDAO userData = new MemoryUserDAO();
    public MemoryAuthDAO authData = new MemoryAuthDAO();
    public MemoryGameDAO gameData = new MemoryGameDAO();

    public UserService serviceUser = new UserService(userData, authData, gameData);
    public GameServices serviceGame = new GameServices(userData, authData, gameData, serviceUser);
    Gson gson = new Gson();

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"))
                .post("/user", (ctx) -> new RegisterHandler(serviceUser).handle(ctx))
                .delete("/db", (ctx)-> new ClearHandler(serviceUser).handle(ctx))
                .post("/session", (ctx) -> new LoginHandler(serviceUser).handle(ctx))
                .delete("/session", (ctx)-> new LogoutHandler(serviceUser).handle(ctx))
                .post("/game", (ctx) -> new CreateGameHandler(serviceUser, serviceGame).handle(ctx))
                .get("/game", (ctx)-> new ListGamesHandler(serviceUser, serviceGame).handle(ctx))
                .put("/game", (ctx) -> new JoinGameHandler(serviceUser, serviceGame).handle(ctx));

        //Exception handling for username already in use when registering
        javalin.exception(AlreadyBoundException.class, (e, ctx) -> {
            ctx.status(403);
            ctx.json(gson.toJson(Map.of("message", "Error: Already Taken")));
        });

        // Exception handling for user not found
        javalin.exception(UnauthorizedResponse.class, (e, ctx)-> {
            ctx.status(401);
            ctx.json(gson.toJson(Map.of("message","Error: unauthorized")));
        });

        // 400 bad request
        javalin.exception(BadRequestResponse.class, (e, ctx) -> {
            ctx.status(400);
            ctx.json(gson.toJson(Map.of("message", "Error: bad request")));
        });
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
