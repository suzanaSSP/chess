package server.handlers;

import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import server.handlers.requests_and_results.RegisterRequest;
import services.UserService;

public class RegisterHandler implements Handler {
    public void handle(Context context ){
        Javalin.create().post("/user", ctx->ctx.body());
        RegisterRequest request = context.bodyAsClass(RegisterRequest.class);
        UserService user_service = new UserService();
        user_service.register(request);
    }
}
