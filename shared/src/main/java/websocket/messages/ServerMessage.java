package websocket.messages;

import chess.ChessGame;

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
        NOTIFICATION
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
    }

    public static class LoadGameMessage extends ServerMessage {
        ChessGame game;
        String jsonGame;
        public LoadGameMessage(String game) {
            super(ServerMessageType.LOAD_GAME);
            this.jsonGame = game;
        }
    }

    public static class ErrorMessage extends ServerMessage {
        String message;

        public ErrorMessage(String message) {
            super(ServerMessageType.ERROR);
            this.message = message;
        }
    }
}
