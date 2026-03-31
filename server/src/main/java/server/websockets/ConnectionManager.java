package server.websockets;

import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import javax.management.Notification;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConnectionManager {
    Map<Integer, List<Session>> connections = new HashMap<>();

    public void add(int gameId, Session session) {
        if (connections.get(gameId).isEmpty()){
            List<Session> sessions = new ArrayList<>();
            sessions.add(session);
            connections.put(gameId, sessions);
        } else {
            connections.get(gameId).add(session);
        }
    }

    public void remove(int gameId, Session session) {
        connections.get(gameId).remove(session);
    }

    public void sendNotificationsToALL(int gameId, Session excludeSession, ServerMessage notification) throws IOException {
        String msg = notification.toString();
        List<Session> chosenSessions = connections.get(gameId);
        for (Session c: chosenSessions) {
            if (c.isOpen()) {
                if (!c.equals(excludeSession)) {
                    c.getRemote().sendString(msg);
                }
            }
        }
    }

    public void sendNotification(int gameId, Session session, ServerMessage notification) throws IOException {
        String msg = notification.toString();
        if (session.isOpen()) {
            session.getRemote().sendString(msg);
        }
    }
}
