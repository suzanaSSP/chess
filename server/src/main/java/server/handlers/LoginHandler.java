package server.handlers;

import com.google.gson.Gson;
import io.javalin.http.*;
import io.javalin.http.Handler;
import server.handlers.requests_and_results.LoginRequest;
import server.handlers.requests_and_results.LoginResult;
import services.UserService;


public class LoginHandler implements Handler {
    UserService userService;

    public LoginHandler(UserService service){
        userService = service;
    }

    public void handle(Context ctx) {
        // Get information from request
//        LoginRequest request = ctx.bodyAsClass(LoginRequest.class);
        Gson gson = new Gson();
        LoginRequest request = gson.fromJson(ctx.body(), LoginRequest.class);

        // Bad Requet
        if (request.username() == null || request.password() == null){
            throw new BadRequestResponse();
        }

        //Get result
        LoginResult result = userService.login_service(request);
        // Turn to JSON file

        ctx.result(gson.toJson(result));
    }
}
