package server.websockets;

import com.google.gson.Gson;
import io.javalin.websocket.WsMessageContext;
import org.eclipse.jetty.websocket.api.Session;
import org.jetbrains.annotations.NotNull;
import websocket.commands.UserGameCommand;

import static javax.management.remote.JMXConnectorFactory.connect;

public class WebSocketHandler {
    Gson gson = new Gson();
    ConnectionManager connect = new ConnectionManager();

    public void handleMessage(@NotNull WsMessageContext wsMessageContext) throws Exception {
        int gameId = -1;
        Session session = wsMessageContext.session;


            UserGameCommand command = gson.fromJson(
                    wsMessageContext.message(), UserGameCommand.class);
            gameId = command.getGameID();
//            String username = getUsername(command.getAuthString()); is this where it validates the user? What class does it do that?
            connect.add(gameId, session);



    }
}
