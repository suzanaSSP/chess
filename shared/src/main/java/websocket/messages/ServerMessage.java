package websocket.messages;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a Message the server can send through a WebSocket
 * <p>
 * Note: You can add to this class, but you should not alter the existing
 * methods.
 */
public class ServerMessage {
    ServerMessageType serverMessageType;

    public enum ServerMessageType {
        LOAD_GAME,
        ERROR,
        NOTIFICATION,
        REDRAW,
        HIGHLIGHT
    }

    public ServerMessage(ServerMessageType type) {
        this.serverMessageType = type;
    }

    public ServerMessageType getServerMessageType() {
        return this.serverMessageType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ServerMessage that)) {
            return false;
        }
        return getServerMessageType() == that.getServerMessageType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getServerMessageType());
    }

    public static class NotificationMessage extends ServerMessage {
        String message;
        public NotificationMessage(String message) {
            super(ServerMessageType.NOTIFICATION);
            this.message = message;
        }

        public String getMessage() {return this.message;}
    }

    public static class LoadGameMessage extends ServerMessage {
        ChessGame game;
        String jsonGame;
        public LoadGameMessage(String game) {
            super(ServerMessageType.LOAD_GAME);
            this.jsonGame = game;
            this.game = new Gson().fromJson(jsonGame, ChessGame.class);
        }
        public ChessGame getChessGame(){return this.game;}
    }

    public static class ErrorMessage extends ServerMessage {
        String errorMessage;

        public ErrorMessage(String message) {
            super(ServerMessageType.ERROR);
            this.errorMessage = message;
        }
    }

    public static class RedrawBoardMessage extends ServerMessage {
        ChessGame game;
        String jsonGame;

        public RedrawBoardMessage(String game) {
            super(ServerMessageType.REDRAW);
            this.jsonGame = game;
            this.game = new Gson().fromJson(jsonGame, ChessGame.class);
        }

        public ChessGame getChessGame() {
            return this.game;
        }
    }

    public static class HighLightMovesMessage extends ServerMessage {
        Collection<ChessMove> moves;

        public HighLightMovesMessage(String moves) {
            super(ServerMessageType.HIGHLIGHT);
            Type collectionType = new TypeToken<Collection<ChessMove>>() {}.getType();
            this.moves = new Gson().fromJson(moves, collectionType);
        }
    }
}
