package server.handlers;

import io.javalin.http.Handler;
import io.javalin.http.Context;
import io.javalin.http.UnauthorizedResponse;
import services.UserService;

public class LogoutHandler implements Handler {
    UserService userService;

    public LogoutHandler(UserService service){
        userService = service;
    }

    public void handle(Context ctx) {
        String authToken = ctx.header("authorization");
        try {
            userService.logout_service(authToken);
            ctx.json("{}");
        } catch (UnauthorizedResponse e) {
            throw new UnauthorizedResponse("User not found");
        }
    }
}
