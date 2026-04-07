package client;

import chess.ChessGame;
import com.google.gson.Gson;
import jakarta.websocket.*;
import ui.DrawChessBoard;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketCommunicator extends Endpoint {
    Session session;
    Gson gson = new Gson();

    public void connectSession(String url, String authToken, int gameID, String playercolor)
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
    public void onMessage(String message, String playercolor){
        ServerMessage msg = gson.fromJson(message, ServerMessage.class);
        if (msg.getServerMessageType() == ServerMessage.ServerMessageType.LOAD_GAME) {
            ServerMessage.LoadGameMessage loadGameMessage = gson.fromJson(message, ServerMessage.LoadGameMessage.class);
            new DrawChessBoard(loadGameMessage.getChessGame().currentBoard, playercolor).drawBoard(System.out);
        }
        else if (msg.getServerMessageType() == ServerMessage.ServerMessageType.NOTIFICATION) {
            ServerMessage.NotificationMessage nMessagae = gson.fromJson(message,
                    ServerMessage.NotificationMessage.class);
            System.out.println(nMessagae.getMessage());
        }
    }

    //Endpoint requires this method, but you don't have to do anything
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

}
