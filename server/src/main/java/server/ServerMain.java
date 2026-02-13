package server;

import chess.*;
import io.javalin.Javalin;
import server.handlers.RegisterHandler;

public class ServerMain {
    public static void main(String[] args) {
        var app = Javalin.create().get("/", ctx->ctx.result("Hello World")).start(7070);
    }
}
