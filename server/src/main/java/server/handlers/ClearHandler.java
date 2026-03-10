package server.handlers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dataaccess.DataAccessException;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import services.UserService;


public class ClearHandler implements Handler {
    UserService userService;

    public ClearHandler(UserService service){
        userService = service;
    }
    public void handle(Context context) throws DataAccessException {
        userService.clearService();
        // Turn to JSON file
        Gson gson = new Gson();
        context.result(gson.toJson(new JsonObject()));

    }

}
