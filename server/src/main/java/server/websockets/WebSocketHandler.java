package server.websockets;

import chess.*;
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
import java.util.Collection;

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
        Session session = wsMessageContext.session;
        try {
            UserGameCommand command = gson.fromJson(wsMessageContext.message(), UserGameCommand.class);
            int gameId = command.getGameID();
            String username = getUsername(command.getAuthToken());
            connectionManager.add(gameId, session);

            switch (command.getCommandType()) {
                case CONNECT:
                    connect(session, username, command);
                    break;
                case MAKE_MOVE:
                    UserGameCommand.MakeMoveCommand gameCommand = gson.fromJson(wsMessageContext.message(),
                            UserGameCommand.MakeMoveCommand.class);
                    makeMove(session, username, gameCommand);
                    break;
                case LEAVE:
                    leaveGame(session, username, command);
                    break;
                case RESIGN:
                    resign(session, username, command);
                    break;
                case REDRAW:
                    redraw(session, command);
                    break;
                case HIGHLIGHT:
                    UserGameCommand.HighLightCommand highLightCommand = gson.fromJson(wsMessageContext.message(),
                            UserGameCommand.HighLightCommand.class);
                    highlight(session, username, highLightCommand);
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
        try {
            ChessGame currGame = gameServices.getGameWithId(command.getGameID());
            currGame.makeMove(command.getMove());
            gameServices.updateChessGame(currGame, command.getGameID());
            // load game
            String jsonGame = gson.toJson(currGame);
            ServerMessage loadGame = new ServerMessage.LoadGameMessage(jsonGame);
            connectionManager.sendNotificationsToALL(command.getGameID(), null, loadGame);
            // notify people
            String message = String.format("%s moved to %s", username, command.getMove().toString());
            ServerMessage notification = new ServerMessage.NotificationMessage(message);
            connectionManager.sendNotificationsToALL(command.getGameID(), session, notification);
        } catch (GameOverException e) {
            String winner = e.getWinner();
            String message = "GAME OVER, Winner: " + winner;
            ServerMessage notification = new ServerMessage.NotificationMessage(message);
            connectionManager.sendNotificationsToALL(command.getGameID(), null, notification);
        }
    }

    public void leaveGame(Session session, String username, UserGameCommand command)
            throws IOException, DataAccessException {
        gameServices.removePlayer(command.getGameID(), username);
        connectionManager.remove(command.getGameID(), session);
        String message = username + "left the game";
        ServerMessage newMsg = new ServerMessage.NotificationMessage(message);
        connectionManager.sendNotificationsToALL(command.getGameID(), session, newMsg);
        // send back to post-login UI

    }

    public void resign(Session session, String username, UserGameCommand command)
            throws DataAccessException, IOException, InvalidMoveException {
        // get game
        ChessGame currGame = gameServices.getGameWithId(command.getGameID());
        // game lost
        currGame.setGameOver();
        currGame.currentBoard.resetBoard();
        gameServices.updateChessGame(currGame, command.getGameID());
        // send notification
        String message = "GAME OVER";
        ServerMessage notification = new ServerMessage.NotificationMessage(message);
        connectionManager.sendNotificationsToALL(command.getGameID(), null, notification);
        // user stays in the game
        String jsonGame = gson.toJson(currGame);
        ServerMessage.LoadGameMessage lgMessage = new ServerMessage.LoadGameMessage(jsonGame);
        connectionManager.sendNotificationsToALL(command.getGameID(), null, lgMessage);
    }

    public void redraw(Session session, UserGameCommand command) throws DataAccessException, IOException {
        ChessGame currGame = gameServices.getGameWithId(command.getGameID());
        String jsonGame = gson.toJson(currGame);
        ServerMessage.LoadGameMessage loadGameMsg = new ServerMessage.LoadGameMessage(jsonGame);
        connectionManager.sendNotification(session, loadGameMsg);
    }

    public void highlight(Session session, String username, UserGameCommand.HighLightCommand command) throws DataAccessException {
        ChessGame currGame = gameServices.getGameWithId(command.getGameID());
        Collection<ChessMove> pieceMoves = currGame.validMoves(command.getPosition());

    }

}
