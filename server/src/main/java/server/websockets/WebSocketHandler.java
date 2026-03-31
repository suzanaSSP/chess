package server.websockets;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import io.javalin.http.UnauthorizedResponse;
import io.javalin.websocket.WsMessageContext;
import org.eclipse.jetty.websocket.api.Session;
import org.jetbrains.annotations.NotNull;
import services.GameServices;
import services.UserService;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.io.IOException;

import static javax.management.remote.JMXConnectorFactory.connect;

public class WebSocketHandler {
    Gson gson = new Gson();
    ConnectionManager connectionManager = new ConnectionManager();
    UserService userService;
    GameServices gameServices;

    public void handleMessage(@NotNull WsMessageContext wsMessageContext, UserService service, GameServices gameServices) throws Exception {
        int gameId = -1;
        Session session = wsMessageContext.session;
        this.userService = service;
        this.gameServices = gameServices;

        try {
            UserGameCommand command = gson.fromJson(
                    wsMessageContext.message(), UserGameCommand.class);
            gameId = command.getGameID();
            String username = getUsername(command.getAuthToken());
            connectionManager.add(gameId, session);

            switch (command.getCommandType()) {
                case CONNECT -> connect(session, username, command);
                case MAKE_MOVE -> makeMove(session, username, (UserGameCommand.MakeMoveCommand) command);
                case LEAVE -> leaveGame(session, username, gameId, command);
                case RESIGN -> resign(session, username, command);
            }
        } catch (UnauthorizedResponse e) {
            sendMessage(session, gameId, new ServerMessage.ErrorMessage("Error: unauthorized"));
            throw e;
        } catch (Exception ex) {
            ex.printStackTrace();
            sendMessage(session, gameId, new ServerMessage.ErrorMessage("Error: " + ex.getMessage()));
            throw ex;
        }
    }

    public void sendMessage(Session session, int gameId, ServerMessage message) throws IOException {
        connectionManager.sendNotification(gameId, session, message);
    }

    public String getUsername(String authToken) {
        return userService.getUserWithAuth(authToken);
    }

    public void connect(Session session, String username, UserGameCommand command) throws DataAccessException {
        // get game
        gameServices.getGameWithId(command.getGameID());
        // load game
        // notify people

    }

    public void makeMove(Session session, String username, UserGameCommand.MakeMoveCommand command) {
        // get game
        // make move
        // load game
        // notify people

    }

    public void leaveGame(Session session, String username, int gameId, UserGameCommand command) {
        connectionManager.remove(gameId, session);
        String message = username + "left the game";
        ServerMessage newMsg = new ServerMessage.NotificationMessage(message);
        // send back to post-login UI

    }

    public void resign(Session session, String username, UserGameCommand command) {
        // get game
        // game lost
        // send notification
        // user stays in the game

    }

}
