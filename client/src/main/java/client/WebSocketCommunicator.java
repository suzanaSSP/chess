package client;

import com.google.gson.Gson;
import jakarta.websocket.*;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketCommunicator extends Endpoint {
    Session session;
    Gson gson = new Gson();

    public void connectSession(String url, String authToken, int gameID)
            throws URISyntaxException, DeploymentException, IOException {
        // CONNECT ->
        // LOAD GAME/ NOTIFIACTION <-

        // connect session
        url = url.replace("http", "ws");
        URI socketURI = new URI(url + "/ws");

        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        this.session = container.connectToServer(this, socketURI);

        // send CONNECT
        UserGameCommand command = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameID);
        this.session.getBasicRemote().sendText(gson.toJson(command));
        
    }

    @OnMessage
    public void onMessage(String message){
        ServerMessage.NotificationMessage notificationMessage = gson.fromJson(message,
                ServerMessage.NotificationMessage.class);
    }

    //Endpoint requires this method, but you don't have to do anything
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

}
