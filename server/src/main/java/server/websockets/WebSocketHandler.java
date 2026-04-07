package server.websockets;

import chess.ChessGame;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.UnauthorizedResponse;
import io.javalin.websocket.*;
import org.eclipse.jetty.websocket.api.Session;
import org.jetbrains.annotations.NotNull;
import services.GameServices;
import services.UserService;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.io.IOException;

public class WebSocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {
    Gson gson = new Gson();
    ConnectionManager connectionManager = new ConnectionManager();
    UserService userService;
    GameServices gameServices;

    public WebSocketHandler(UserService service, GameServices gameServices) {
        this.userService = service;
        this.gameServices = gameServices;
    }

    @Override
    public void handleConnect(WsConnectContext ctx) {
        System.out.println("Websocket connected");
        ctx.enableAutomaticPings();
    }

    @Override
    public void handleMessage(@NotNull WsMessageContext wsMessageContext) throws Exception {
        int gameId = -1;
        Session session = wsMessageContext.session;

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
            sendMessage(session, new ServerMessage.ErrorMessage("Error: unauthorized"));
        } catch (BadRequestResponse e) {
            sendMessage(session, new ServerMessage.ErrorMessage("Error: invalid input"));
        } catch (Exception ex) {
            ex.printStackTrace();
            sendMessage(session, new ServerMessage.ErrorMessage("Error: " + ex.getMessage()));
            throw ex;
        }
    }

    public void sendMessage(Session session, ServerMessage message) throws IOException {
        connectionManager.sendNotification(session, message);
    }

    public String getUsername(String authToken) {
        return userService.getUserWithAuth(authToken);
    }

    @Override
    public void handleClose(WsCloseContext ctx) {
        System.out.println("Websocket closed");
    }

    public void connect(Session session, String username, UserGameCommand command)
            throws DataAccessException, IOException {

            // get game
            ChessGame currGame = gameServices.getGameWithId(command.getGameID());
            if (currGame == null) {
                throw new BadRequestResponse();
            }
            // load game
            String jsonGame = gson.toJson(currGame);
            ServerMessage loadGame = new ServerMessage.LoadGameMessage(jsonGame);
            connectionManager.sendNotification(session, loadGame);
            // notify people
            String message = String.format("%s joined the game", username);
            ServerMessage notification = new ServerMessage.NotificationMessage(message);
            // broadcast to everyone in gameID EXCEPT this session
            connectionManager.sendNotificationsToALL(command.getGameID(), session, notification);


    }

    public void makeMove(Session session, String username, UserGameCommand.MakeMoveCommand command)
            throws DataAccessException, InvalidMoveException, IOException {
        // get game
        ChessGame updatedChessGame = gameServices.updateChessGame(command.getGameID(), command.getMove());
        // load game
        String jsonGame = gson.toJson(updatedChessGame);
        ServerMessage loadGame = new ServerMessage.LoadGameMessage(jsonGame);
        connectionManager.sendNotificationsToALL(command.getGameID(), null, loadGame);
        // notify people
        String message = String.format("%s moved to %s", username, command.getMove().toString());
        ServerMessage notification = new ServerMessage.NotificationMessage(message);
        connectionManager.sendNotificationsToALL(command.getGameID(), session, notification);

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
