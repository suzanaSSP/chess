package server;

import chess.*;
import io.javalin.Javalin;
import server.handlers.RegisterHandler;

public class ServerMain {
    public static void main(String[] args) {
        Server server = new Server();
        server.run(8080);

        System.out.println("♕ 240 Chess Server");
    }
}
