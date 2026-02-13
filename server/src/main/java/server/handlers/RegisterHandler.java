package server.handlers;

import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.Handler;

public class RegisterHandler implements Handler {
    public void handle(Context context ){
        Javalin.create().post("/user", ctx->ctx.body());
        //context.bodyAsClass()
    }
}
