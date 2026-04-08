package ui;

import chess.*;

import java.io.PrintStream;
import java.util.*;

import static ui.EscapeSequences.*;

public class DrawChessBoard {
    private final static List<String> whiteLetters = Arrays.asList("a", "b", "c", "d", "e", "f", "g", "h");
    // Board dimensions.
    private static final int SQUARE_SIZE_IN_PADDED_CHARS = 3;
    private final static String headerColor = SET_BG_COLOR_WHITE;
    private static String currentSquareColor = SET_BG_COLOR_MAGENTA;
    private final static String textHeaderColor = SET_TEXT_COLOR_BLACK;

    private final static Map<ChessPiece.PieceType, String> piecesMap =
            Map.of(ChessPiece.PieceType.KING, "K",
                    ChessPiece.PieceType.QUEEN, "Q",
                    ChessPiece.PieceType.BISHOP, "B",
                    ChessPiece.PieceType.KNIGHT, "N",
                    ChessPiece.PieceType.ROOK, "R",
                    ChessPiece.PieceType.PAWN, "P");
    private static final String EMPTY = " ";
    private final ChessBoard currBoard;
    String playerColor;

    public DrawChessBoard(ChessBoard currBoard, String playerColor) {
        this.currBoard = currBoard;
        this.playerColor = playerColor;
    }

    public void drawBoard(PrintStream out) {
        System.out.println("\n");
        if (playerColor.equals("WHITE")) {
            drawWhiteHeaders(out);
            drawRows(out);
            drawWhiteHeaders(out);
        } else if (playerColor.equals("BLACK")){
            drawBlackHeaders(out);
            drawRows(out);
            drawBlackHeaders(out);
        }
    }

    private static void drawWhiteHeaders(PrintStream out){
        drawSquare(out, EMPTY, headerColor, textHeaderColor);
        for (int i=0; i<8; i++) {
            drawSquare(out, whiteLetters.get(i), headerColor, textHeaderColor);
        }
        drawSquare(out, EMPTY, headerColor, textHeaderColor);
        out.println("\n");
    }

    private static void drawBlackHeaders(PrintStream out) {
        drawSquare(out, EMPTY, headerColor, textHeaderColor);
        for (int i=7; i>=0; i--) {
            drawSquare(out, whiteLetters.get(i), headerColor, textHeaderColor);
        }
        drawSquare(out, EMPTY, headerColor, textHeaderColor);

    }

    private void drawRows(PrintStream out) {
        int start;
        int end;
        int step;
        if (playerColor.equals("WHITE")) {
            start = 8;
            end = 1;
            step = -1;
        } else {
            start = 1;
            end = 8;
            step = 1;
        }

        for (int i=start; i!=end+step; i+=step) {
            // Horizontal number
            drawSquare(out, String.valueOf(i), headerColor, textHeaderColor);
            drawWithChessBoard(out, i);
            drawSquare(out, String.valueOf(i), headerColor, textHeaderColor);
            out.print("\n");
        }
    }

    private void drawWithChessBoard(PrintStream out, int row) {
        for (int j=1; j<=8; j++){
            ChessPiece currPiece = currBoard.getPiece(new ChessPosition(row, j));
            if (currBoard.getPiece(new ChessPosition(row, j)) != null) {
                if (currPiece.pieceColor == ChessGame.TeamColor.BLACK) {
                    drawSquare(out, piecesMap.get(currPiece.type), currentSquareColor, SET_TEXT_COLOR_BLACK);
                }
                else if (currPiece.pieceColor == ChessGame.TeamColor.WHITE) {
                    drawSquare(out, piecesMap.get(currPiece.type), currentSquareColor, SET_TEXT_COLOR_WHITE);
                }
            } else {
                drawSquare(out, EMPTY, currentSquareColor, currentSquareColor);
            }
            changeColors();
        }
        changeColors();
    }

    private static void drawSquare(PrintStream out, String headerText, String color, String textColor) {
        int prefixLength = SQUARE_SIZE_IN_PADDED_CHARS / 2;
        int suffixLength = SQUARE_SIZE_IN_PADDED_CHARS - prefixLength - 1;

        out.print(EMPTY.repeat(prefixLength));
        printHeaderText(out, headerText, color, textColor);
        out.print(EMPTY.repeat(suffixLength));
    }

    private static void printHeaderText(PrintStream out, String player, String bgColor, String textColor) {
        out.print(bgColor);
        out.print(textColor);
        out.print(player);
    }

    private static void changeColors() {
        if (currentSquareColor.equals(SET_BG_COLOR_LIGHT_GREY)) {
            currentSquareColor = SET_BG_COLOR_MAGENTA;
        }
        else {
            currentSquareColor = SET_BG_COLOR_LIGHT_GREY;
        }
    }
}
