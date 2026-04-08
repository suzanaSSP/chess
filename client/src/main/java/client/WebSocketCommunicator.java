package client;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import jakarta.websocket.*;
import ui.Client;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;

public class WebSocketCommunicator extends Endpoint {
    Session session;
    Gson gson = new Gson();

    public void connectSession(String host, int port, Client currClient)
            throws URISyntaxException, DeploymentException, IOException {
        // CONNECT ->
        // LOAD GAME/ NOTIFICATION <-

        // connect session
        String urlString = String.format(Locale.getDefault(), "ws://%s:%d/ws", host, port);
        URI socketURI = new URI(urlString);

        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        this.session = container.connectToServer(this, socketURI);

        this.session.addMessageHandler(new MessageHandler.Whole<String>() {
            @OnMessage
            public void onMessage(String message){
                ServerMessage msg = gson.fromJson(message, ServerMessage.class);
                if (msg.getServerMessageType() == ServerMessage.ServerMessageType.LOAD_GAME) {
                    ServerMessage.LoadGameMessage loadGameMessage = gson.fromJson(message, ServerMessage.LoadGameMessage.class);
                    ChessGame currGame = loadGameMessage.getChessGame();
                    if (!currGame.wasMoved) {
                        if (currClient.currPlayerColor.equals("WHITE")) {
                            currGame.currentBoard.addWhiteStartPieces();
                        } else {
                            currGame.currentBoard.addBlackStartPieces();
                        }
                    }
                    currClient.drawChessboard(currGame);
                }
                else if (msg.getServerMessageType() == ServerMessage.ServerMessageType.NOTIFICATION) {
                    ServerMessage.NotificationMessage nMessage = gson.fromJson(message,
                            ServerMessage.NotificationMessage.class);
                    System.out.println(nMessage.getMessage());
                }
                else if (msg.getServerMessageType() == ServerMessage.ServerMessageType.ERROR) {
                    ServerMessage.ErrorMessage eMessage = gson.fromJson(message, ServerMessage.ErrorMessage.class);
                    System.out.println(eMessage.getErrorMessage());
                }
            }
        });
    }

    public void connectCommand(String authToken, int gameID) throws IOException {
        // send CONNECT
        UserGameCommand command = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameID);
        this.session.getBasicRemote().sendText(gson.toJson(command));
    }

    public void leaveCommand(String authToken, int gameID) throws IOException {
        UserGameCommand command = new UserGameCommand(UserGameCommand.CommandType.LEAVE, authToken, gameID);
        this.session.getBasicRemote().sendText(gson.toJson(command));
    }

    public void resignCommand(String authToken, int gameID) throws IOException {
        UserGameCommand command = new UserGameCommand(UserGameCommand.CommandType.RESIGN, authToken, gameID);
        this.session.getBasicRemote().sendText(gson.toJson(command));
    }

    public void makeMoveCommand(String authToken, int gameID, ChessMove move) throws IOException {
        UserGameCommand.MakeMoveCommand command = new UserGameCommand.MakeMoveCommand
                (UserGameCommand.CommandType.MAKE_MOVE, authToken, gameID, move);
        this.session.getBasicRemote().sendText(gson.toJson(command));
    }

    //Endpoint requires this method, but you don't have to do anything
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

}
