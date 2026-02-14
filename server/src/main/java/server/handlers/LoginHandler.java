package server.handlers;

import io.javalin.http.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;

import io.javalin.http.Handler;
import com.fasterxml.jackson.databind.ObjectMapper;
import server.handlers.requests_and_results.LoginRequest;
import server.handlers.requests_and_results.LoginResult;
import server.handlers.requests_and_results.RegisterRequest;
import server.handlers.requests_and_results.RegisterResult;
import services.UserService;
import io.javalin.http.Handler;

public class LoginHandler implements Handler {
    public void handle(Context ctx) {
        // Get information from request
        LoginRequest request = ctx.bodyAsClass(LoginRequest.class);
        //Get result
        LoginResult result = new UserService().login_service(request);
        // Turn to JSON file
        ctx.json(result);
    }
}
