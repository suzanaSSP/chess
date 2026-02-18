package server.handlers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
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
            // Turn to JSON file
            Gson gson = new Gson();
            ctx.result(gson.toJson(new JsonObject()));

        } catch (UnauthorizedResponse e) {
            throw new UnauthorizedResponse("User not found");
        }
    }
}
