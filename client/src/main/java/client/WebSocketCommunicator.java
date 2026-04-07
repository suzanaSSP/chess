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
import java.util.Locale;

public class WebSocketCommunicator extends Endpoint {
    Session session;
    Gson gson = new Gson();

    public void connectSession(String host, int port, String authToken, int gameID)
            throws URISyntaxException, DeploymentException, IOException {
        // CONNECT ->
        // LOAD GAME/ NOTIFIACTION <-

        // connect session
        String urlString = String.format(Locale.getDefault(), "ws://%s:%d/ws", host, port);
        URI socketURI = new URI(urlString);
        System.out.println(urlString);

        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        this.session = container.connectToServer(this, socketURI);

        this.session.addMessageHandler(new MessageHandler.Whole<String>() {
            @OnMessage
            public void onMessage(String message){
                String playercolor = "WHITE";
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

                else if(msg.getServerMessageType() == ServerMessage.ServerMessageType.REDRAW) {
                    ServerMessage.RedrawBoardMessage board = gson.fromJson(message, ServerMessage.RedrawBoardMessage.class);
                    new DrawChessBoard(board.getChessGame().currentBoard, playercolor).drawBoard(System.out);
                }

                else if (msg.getServerMessageType() == ServerMessage.ServerMessageType.HIGHLIGHT) {
                    ServerMessage.HighLightMovesMessage highlightMessage = gson.fromJson(message, ServerMessage.HighLightMovesMessage.class);
                }
            }
        });

        // send CONNECT
        UserGameCommand command = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameID);
        this.session.getBasicRemote().sendText(gson.toJson(command));


    }



    //Endpoint requires this method, but you don't have to do anything
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

}
