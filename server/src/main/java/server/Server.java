package server;

import io.javalin.*;
import io.javalin.http.Context;
import server.handlers.RegisterHandler;

public class Server {

    private final Javalin javalin;
    private final RegisterHandler handler = new RegisterHandler();

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"))
                .post("/user", (ctx) -> new RegisterHandler().handle(ctx));

    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
