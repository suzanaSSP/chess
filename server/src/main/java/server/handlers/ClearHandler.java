package server.handlers;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import services.UserService;


public class ClearHandler implements Handler {
    UserService userService;

    public ClearHandler(UserService service){
        userService = service;
    }
    public void handle(Context context) {
        userService.clear_service();
        String json = "{}";
        context.result(json);
    }

}
