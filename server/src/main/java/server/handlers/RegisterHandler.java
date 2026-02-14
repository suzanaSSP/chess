package server.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import com.fasterxml.jackson.databind.ObjectMapper;
import server.handlers.requests_and_results.RegisterRequest;
import server.handlers.requests_and_results.RegisterResult;
import services.UserService;


public class RegisterHandler implements Handler {
    public void handle(Context context ){
        // Put context in request object
        RegisterRequest request = context.bodyAsClass(RegisterRequest.class);

        // Call service and store result in result class
        UserService user_service = new UserService();
        RegisterResult result = user_service.register(request);

        //Turn to JSON file
        try {
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(result);
            // Send back to client
            context.result(json);
        } catch (JsonProcessingException e) {
            // if it fails
            e.printStackTrace();
            System.out.println("Failed to convert object to JSON");
        }

    }
}
