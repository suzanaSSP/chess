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
    String currPosColor = SET_BG_COLOR_YELLOW;
    String moveWhiteColor = SET_BG_COLOR_GREEN;
    String moveBlackColor = SET_BG_COLOR_DARK_GREEN;

    public DrawChessBoard(ChessBoard currBoard, String playerColor) {
        this.currBoard = currBoard;
        this.playerColor = playerColor;
    }

    public void drawBoard(PrintStream out, Collection<ChessMove> moves, ChessPosition currPost) {
        System.out.println("\n");
        if (playerColor.equals("WHITE")) {
            drawWhiteHeaders(out);
            drawRows(out, moves, currPost);
            drawWhiteHeaders(out);
        } else if (playerColor.equals("BLACK")){
            drawBlackHeaders(out);
            drawRows(out, moves,currPost);
            drawBlackHeaders(out);
        }
    }

    private static void drawWhiteHeaders(PrintStream out){
        drawSquareWithText(out, EMPTY, headerColor, textHeaderColor);
        for (int i=0; i<8; i++) {
            drawSquareWithText(out, whiteLetters.get(i), headerColor, textHeaderColor);
        }
        drawSquareWithText(out, EMPTY, headerColor, textHeaderColor);
        out.println("\n");
    }

    private static void drawBlackHeaders(PrintStream out) {
        drawSquareWithText(out, EMPTY, headerColor, textHeaderColor);
        for (int i=7; i>=0; i--) {
            drawSquareWithText(out, whiteLetters.get(i), headerColor, textHeaderColor);
        }
        drawSquareWithText(out, EMPTY, headerColor, textHeaderColor);
        out.println("\n");

    }

    private void drawRows(PrintStream out, Collection<ChessMove> moves, ChessPosition currPos) {
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
            drawSquareWithText(out, String.valueOf(i), headerColor, textHeaderColor);
            drawWithChessBoard(out, i, moves, currPos);
            drawSquareWithText(out, String.valueOf(i), headerColor, textHeaderColor);
            out.print("\n");
        }
    }

    private void drawWithChessBoard(PrintStream out, int row, Collection<ChessMove> move, ChessPosition currPost) {
        Set<ChessPosition> positions = new HashSet<>();
        if (move != null) {
            positions = highlightMoves(move);
        }

        for (int j=1; j<=8; j++){
            String squareColor = currentSquareColor;
            ChessPosition posToCheck = new ChessPosition(row, j);
            ChessPiece currPiece = currBoard.getPiece(posToCheck);

            //Check for highlights
            if (currPost != null && move != null) {
                if (posToCheck.equals(currPost)){
                    squareColor = currPosColor;
                } else if (positions.contains(posToCheck)) {
                    if (currentSquareColor.equals(SET_BG_COLOR_MAGENTA)){
                        squareColor = moveWhiteColor;
                    } else {squareColor = moveBlackColor;}
                }
            }


            if (currBoard.getPiece(posToCheck) != null) {
                if (currPiece.pieceColor == ChessGame.TeamColor.BLACK) {
                    drawSquareWithText(out, piecesMap.get(currPiece.type), squareColor, SET_TEXT_COLOR_BLACK);
                }
                else if (currPiece.pieceColor == ChessGame.TeamColor.WHITE) {
                    drawSquareWithText(out, piecesMap.get(currPiece.type), squareColor, SET_TEXT_COLOR_WHITE);
                }
            } else {
                drawSquareWithText(out, EMPTY, squareColor, squareColor);
            }
            changeColors();
        }
        changeColors();
    }

    private static void drawSquareWithText(PrintStream out, String headerText, String color, String textColor) {
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

    private static Set<ChessPosition> highlightMoves(Collection<ChessMove> moves) {
        Set<ChessPosition> highlightPos = new HashSet<>();
        for (ChessMove move : moves) {
            highlightPos.add(move.getEndPosition());
        }

        return highlightPos;
    }



}
