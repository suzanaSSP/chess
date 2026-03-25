package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import static ui.EscapeSequences.*;

public class DrawChessBoard {
    private static List<String> letters = Arrays.asList("a", "b", "c", "d", "e", "f", "g", "h");
    // Board dimensions.
    private static final int SQUARE_SIZE_IN_PADDED_CHARS = 3;
    private static String headerColor = SET_BG_COLOR_WHITE;
    private static String currentSquareColor = SET_BG_COLOR_LIGHT_GREY;
    private static String textHeaderColor = SET_TEXT_COLOR_BLACK;

    private static List<String> pieces = Arrays.asList("R", "N", "B", "K", "Q", "B", "N", "R");
    private static final String EMPTY = " ";
    private static String playerColorTest = "WHITE";

    public void drawBoard(String playerColor, PrintStream out) {
        drawHeaders(out);
        System.out.println("\n");
        if (playerColor.equals("WHITE")) {
            drawWhiteRows(out);
        } else {
            drawBlackRows(out);
        }
        drawHeaders(out);
    }


    public static void main(String[] args) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        drawHeaders(out);
        System.out.println("\n");
        if (playerColorTest == "WHITE") {
            drawWhiteRows(out);
        } else {
            drawBlackRows(out);
        }
        drawHeaders(out);
    }

    private static void drawHeaders(PrintStream out){
        drawSquare(out, EMPTY, headerColor, textHeaderColor);
        for (int i=0; i<8; i++) {
            drawSquare(out, letters.get(i), headerColor, textHeaderColor);
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
            drawRow(out, i);
            drawSquare(out, String.valueOf(i), headerColor, textHeaderColor);
            out.print("\n");
        }
    }

    private static void drawBlackRows(PrintStream out) {
        for (int i=1; i<=8; i++) {
            // Horizantal number
            drawSquare(out, String.valueOf(i), headerColor, textHeaderColor);
            drawRow(out, i);
            drawSquare(out, String.valueOf(i), headerColor, textHeaderColor);
            out.print("\n");
        }
    }

    private static void drawRow(PrintStream out, int i) {
        for (int j=0; j<8; j++) {
            if (i == 8 ) {
                drawSquare(out, pieces.get(j), currentSquareColor, SET_TEXT_COLOR_BLACK);
            } else if (i == 7) {
                drawSquare(out, "P", currentSquareColor, SET_TEXT_COLOR_BLACK);
            }  else if (i == 2) {
                drawSquare(out, "P", currentSquareColor, SET_TEXT_COLOR_WHITE);
            } else if (i == 1) {
                drawSquare(out, pieces.get(j), currentSquareColor, SET_TEXT_COLOR_WHITE);
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
