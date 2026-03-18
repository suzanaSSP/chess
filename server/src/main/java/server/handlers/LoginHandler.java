package server.handlers;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import io.javalin.http.*;
import io.javalin.http.Handler;
import requestsandresults.LoginRequest;
import requestsandresults.LoginResult;
import services.UserService;


public class LoginHandler implements Handler {
    UserService userService;

    public LoginHandler(UserService service){

        userService = service;
    }

    public void handle(Context ctx) throws DataAccessException {
        // Get information from request
//        LoginRequest request = ctx.bodyAsClass(LoginRequest.class);
        Gson gson = new Gson();
        LoginRequest request = gson.fromJson(ctx.body(), LoginRequest.class);

        // Bad Requet
        if (request.username() == null || request.password() == null){
            throw new BadRequestResponse();
        }

        //Get result
        LoginResult result = userService.loginService(request);
        // Turn to JSON file

        ctx.result(gson.toJson(result));
    }
}
