package server.handlers;

import com.google.gson.Gson;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import server.handlers.requests_and_results.LoginRequest;
import server.handlers.requests_and_results.RegisterRequest;
import server.handlers.requests_and_results.RegisterResult;
import services.UserService;

public class RegisterHandler implements Handler {
    UserService userService;

    public RegisterHandler(UserService service){
        userService = service;
    }
    public void handle(Context context ){
        // Put context in request object
        Gson gson = new Gson();
        RegisterRequest request = gson.fromJson(context.body(), RegisterRequest.class);

        // Bad request
        if (request.username() == null || request.password() == null || request.email() == null){
            throw new BadRequestResponse();
        }

        // Call service and store result in result class
        RegisterResult result = userService.register(request);

        // Turn to JSON file
        context.result(gson.toJson(result));
    }
}
