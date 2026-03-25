package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static ui.EscapeSequences.*;

public class DrawChessBoard {
    private static List<String> whiteLetters = Arrays.asList("a", "b", "c", "d", "e", "f", "g", "h");
    private static List<String> blackLetters = Arrays.asList("h", "g", "f", "e", "d", "c", "b", "a");
    // Board dimensions.
    private static final int SQUARE_SIZE_IN_PADDED_CHARS = 3;
    private static String headerColor = SET_BG_COLOR_WHITE;
    private static String currentSquareColor = SET_BG_COLOR_MAGENTA;
    private static String textHeaderColor = SET_TEXT_COLOR_BLACK;

    private static Map<String, String> piecesMap = Map.of("a", "R",
                                                        "b", "N",
                                                        "c", "B",
                                                        "d", "Q",
                                                    "e", "K",
                                                    "f", "B",
                                                    "g", "N",
                                                    "h", "R");
    private static final String EMPTY = " ";
    private static final List<String> PIECES = Arrays.asList("R", "N", "B", "Q", "K", "B", "N", "R");
    private static String playerColorTest = "BLACK";

    public void drawBoard(String playerColor, PrintStream out) {
        System.out.println("\n");
        if (playerColor.equals("WHITE")) {
            drawWhiteHeaders(out);
            drawWhiteRows(out);
            drawWhiteHeaders(out);
        } else if (playerColor.equals("BLACK")){
            drawBlackHeaders(out);
            drawBlackRows(out);
            drawBlackHeaders(out);
        }

    }


    public static void main(String[] args) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        if (playerColorTest == "WHITE") {
            drawWhiteHeaders(out);
            drawWhiteRows(out);
            drawWhiteHeaders(out);
        } else if (playerColorTest == "BLACK"){
            drawBlackHeaders(out);
            drawBlackRows(out);
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

    private static void drawWhiteRows(PrintStream out) {
        for (int i=8; i>=1; i--) {
            // Horizantal number
            drawSquare(out, String.valueOf(i), headerColor, textHeaderColor);
            drawRow(out, i, "WHITE");
            drawSquare(out, String.valueOf(i), headerColor, textHeaderColor);
            out.print("\n");
        }
    }

    private static void drawBlackRows(PrintStream out) {
        out.println("\n");
        for (int i=1; i<=8; i++) {
            // Horizantal number
            drawSquare(out, String.valueOf(i), headerColor, textHeaderColor);
            drawRow(out, i, "BLACK");
            drawSquare(out, String.valueOf(i), headerColor, textHeaderColor);
            out.print("\n");
        }
    }

    private static void drawRow(PrintStream out, int i, String currPlayerColor) {
        String currColumn;
        for (int j=0; j<8; j++) {
            //CHeck for which column
            if (currPlayerColor == "BLACK") {
                currColumn = blackLetters.get(j);
            } else{
                currColumn = whiteLetters.get(j);
            }

            // Check for which row
            if (i == 8 ) {
                String peiceTest = piecesMap.get(currColumn);
                drawSquare(out, piecesMap.get(currColumn), currentSquareColor, SET_TEXT_COLOR_BLACK);
            } else if (i == 7) {
                drawSquare(out, "P", currentSquareColor, SET_TEXT_COLOR_BLACK);
            }  else if (i == 2) {
                drawSquare(out, "P", currentSquareColor, SET_TEXT_COLOR_WHITE);
            } else if (i == 1) {
                drawSquare(out, piecesMap.get(currColumn), currentSquareColor, SET_TEXT_COLOR_WHITE);
            } else {
                drawSquare(out, EMPTY, currentSquareColor, currentSquareColor);
            }
            changeColors();
        }
        changeColors();

    }

    private static void changeColors() {
        if (currentSquareColor == SET_BG_COLOR_LIGHT_GREY) {
            currentSquareColor = SET_BG_COLOR_MAGENTA;
        }
        else {
            currentSquareColor = SET_BG_COLOR_LIGHT_GREY;
        }
    }
}
