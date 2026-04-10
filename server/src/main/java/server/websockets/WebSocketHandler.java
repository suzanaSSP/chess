package server.websockets;

import chess.*;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.UnauthorizedResponse;
import io.javalin.websocket.*;
import org.eclipse.jetty.websocket.api.Session;
import org.jetbrains.annotations.NotNull;
import requestsandresults.AlternativeGameData;
import services.GameServices;
import services.UserService;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.nio.channels.AlreadyBoundException;

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

            String message = String.format("%s joined the game", username);
            if (isObserver(username, command.getGameID())){
                message = String.format("%s joined the game as observer", username);
            }
            // notify people
            ServerMessage notification = new ServerMessage.NotificationMessage(message);
            // broadcast to everyone in gameID EXCEPT this session
            connectionManager.sendNotificationsToALL(command.getGameID(), session, notification);
    }

    public void makeMove(Session session, String username, UserGameCommand.MakeMoveCommand command)
            throws DataAccessException, IOException {
        // get game
        try {
            if (isObserver(username, command.getGameID())){
                throw new ObserverException("Observer not allowed");
            }

            ChessGame currGame = gameServices.getGameWithId(command.getGameID());
            ChessGame.TeamColor userColor = getUserColor(username, command.getGameID());
            if (!currGame.teamPlaying.equals(userColor)){
                throw new OponnentException("It's not your turn");
            }

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
            // Team playing switched after making a move
            checkExceptions(currGame, username, command);

        } catch (InvalidMoveException e) {
            String message = "Invalid move";
            ServerMessage notification = new ServerMessage.ErrorMessage(message);
            connectionManager.sendNotification(session, notification);
        } catch (ResignedException e) {
            ServerMessage error = new ServerMessage.ErrorMessage("Error" + e.getMessage());
            connectionManager.sendNotification(session, error);
        } catch (ObserverException e) {
            ServerMessage error = new ServerMessage.ErrorMessage("Error: You're not supposed to do that");
            connectionManager.sendNotification(session, error);
        } catch (OponnentException e) {
            ServerMessage error = new ServerMessage.ErrorMessage("Error: Not your turn");
            connectionManager.sendNotification(session, error);
        }
    }

    public void checkExceptions(ChessGame currGame, String username, UserGameCommand command)
            throws DataAccessException, IOException {
        String message = "";
        if (currGame.isInCheckmate(currGame.teamPlaying)){
            message = String.format("%s is in checkmate", getOpponentUsername(username, command.getGameID()));
            ServerMessage notification = new ServerMessage.NotificationMessage(message);
            connectionManager.sendNotificationsToALL(command.getGameID(), null, notification);
        } else if (currGame.isInCheck(currGame.teamPlaying)) {
            message = String.format("%s is in check", getOpponentUsername(username, command.getGameID()));
            ServerMessage notification = new ServerMessage.NotificationMessage(message);
            connectionManager.sendNotificationsToALL(command.getGameID(), null, notification);
        } else if (currGame.isInStalemate(currGame.teamPlaying)) {
            message = String.format("%s is in stalemate", getOpponentUsername(username, command.getGameID()));
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
        try {
            if (isObserver(username, command.getGameID())){
                throw new ObserverException("Observer not allowed");
            }
            // get game
            ChessGame currGame = gameServices.getGameWithId(command.getGameID());
            // game resign
            currGame.setResign();
            gameServices.updateChessGame(currGame, command.getGameID());
            // send notification
            String message = username + "resigned";
            ServerMessage notification = new ServerMessage.NotificationMessage(message);
            connectionManager.sendNotificationsToALL(command.getGameID(), null, notification);
            // user stays in the game
        } catch (AlreadyBoundException e) {
            ServerMessage error = new ServerMessage.ErrorMessage("Error: Already resigned");
            connectionManager.sendNotification(session, error);
        } catch (ObserverException e) {
            ServerMessage error = new ServerMessage.ErrorMessage("Error: You're not supposed to do that");
            connectionManager.sendNotification(session, error);
        }
    }

    public boolean isObserver(String username, int gameId) throws DataAccessException {
        AlternativeGameData currGameCheck = gameServices.getGameData(gameId);
        if ((currGameCheck == null)|| (currGameCheck.blackUsername() == null) || (currGameCheck.whiteUsername() == null)) {
            return false;
        }
        if ((currGameCheck.blackUsername().equals(username)) || (currGameCheck.whiteUsername().equals(username))) {
            return false;
        } return true;
    }

    public ChessGame.TeamColor getUserColor(String username, int gameId) throws DataAccessException {
        AlternativeGameData currGameCheck = gameServices.getGameData(gameId);
        if (currGameCheck.whiteUsername().equals(username)){
            return ChessGame.TeamColor.WHITE;
        } else {
            return ChessGame.TeamColor.BLACK;
        }
    }

    public String getOpponentUsername(String username, int gameId) throws DataAccessException {
        AlternativeGameData currGameCheck = gameServices.getGameData(gameId);
        if (currGameCheck.blackUsername().equals(username)){
            return currGameCheck.whiteUsername();
        } else {
            return currGameCheck.blackUsername();
        }
    }

}
