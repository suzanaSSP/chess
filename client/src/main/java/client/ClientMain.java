package client;

import chess.*;
import ui.Client;

import java.io.IOException;
import java.net.URISyntaxException;

public class ClientMain {
    public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);

        Client client = new Client();
        client.runMenu();
    }
}
