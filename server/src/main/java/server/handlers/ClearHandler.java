package server.handlers;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import services.UserService;


public class ClearHandler implements Handler {
    public void handle(Context context) {
        UserService service = new UserService();
        service.clear_service();
        String json = "{}";
        context.result(json);
    }

}
