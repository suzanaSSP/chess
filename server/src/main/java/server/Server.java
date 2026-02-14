package server;

import io.javalin.*;
import io.javalin.http.UnauthorizedResponse;
import server.handlers.ClearHandler;
import server.handlers.LoginHandler;
import server.handlers.RegisterHandler;

import java.rmi.AlreadyBoundException;
import java.util.Map;

public class Server {

    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"))
                .post("/user", (ctx) -> new RegisterHandler().handle(ctx))
                .delete("/db", (ctx)-> new ClearHandler().handle(ctx))
                .post("/session", (ctx) -> new LoginHandler().handle(ctx));

        // Exception handling for user not found
        javalin.exception(UnauthorizedResponse.class, (e, ctx)-> {
            ctx.status(401);
            ctx.json(Map.of("error", "unauthorized",
                             "message", "User not found"));
        });

        //Exception handling for username already in use when registering
        javalin.exception(AlreadyBoundException.class, (e, ctx) -> {
            ctx.status(403);
            ctx.json(Map.of("message", "Username already used, pick a different one",
                           "error", "Already Taken"));
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
