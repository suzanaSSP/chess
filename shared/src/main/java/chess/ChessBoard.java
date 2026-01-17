package chess;

import java.util.Arrays;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    ChessPiece[][] squares = new ChessPiece[8][8];
    public ChessBoard() {
        
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        squares[position.getRow()-1][position.getColumn()-1] = piece;
    }

    // adds all the start pieces in a resetted board
    public void addStartPawns() {
        // white pawns
        for (int i=0; i<8;i++) {
            ChessPosition white_new_pos = new ChessPosition(1, i);
            ChessPiece new_white_pawn = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
            squares[white_new_pos.getRow()][white_new_pos.getColumn()] = new_white_pawn;
        }

        //black pawns
        for (int i=0; i<8;i++) {
            ChessPosition black_new_pos = new ChessPosition(6, i);
            ChessPiece new_black_pawn = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
            squares[black_new_pos.getRow()][black_new_pos.getColumn()] = new_black_pawn;
        }
    }

    public void addStartBishops() {
        ChessPiece white_bishop = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        ChessPiece black_bishop = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);

        //white bishop left
        squares[0][2] = white_bishop;
        //white bishop right
        squares[0][5] = white_bishop;

        //black bishop left
        squares[7][2] = black_bishop;
        squares[7][5] = black_bishop;
    }

    public void addStartRooks() {
        ChessPiece white_rook = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        ChessPiece black_rook = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);

        //white rook left
        squares[0][0] = white_rook;
        //white rook right
        squares[0][7] = white_rook;

        //black rook left
        squares[7][0] = black_rook;
        squares[7][7] = black_rook;
    }

    public void addStartKnights() {
        ChessPiece white_knight = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        ChessPiece black_knight = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);

        //white bishop left
        squares[0][1] = white_knight;
        //white bishop right
        squares[0][6] = white_knight;

        //black bishop left
        squares[7][1] = black_knight;
        squares[7][6] = black_knight;
    }

    public void addStartPieces() {
        addStartPawns();
        addStartBishops();
        addStartKnights();
        addStartRooks();

        ChessPiece white_king = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
        ChessPiece white_queen = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
        ChessPiece black_king = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);
        ChessPiece black_queen = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);

        //white king and queen
        squares[0][3] = white_queen;
        squares[0][4] = white_king;
        squares[7][3] = black_queen;
        squares[7][4] = black_king;
    }
    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {

        return squares[position.getRow()-1][position.getColumn()-1];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        squares = new ChessPiece[8][8];
        addStartPieces();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        System.out.println("This board contents: " + Arrays.deepToString(this.squares));
        System.out.println("That board contents: " + Arrays.deepToString(that.squares));

        boolean deepMatch = Objects.deepEquals(squares, that.squares);
        System.out.println("DeepEquals result: " + deepMatch);
        return deepMatch;
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(squares);
    }

    @Override
    public String toString() {
        StringBuilder board = new StringBuilder();

        for (int i=0; i<8; i++) {
            StringBuilder row = new StringBuilder();
            for (int j=0; j<8; j++) {
                if (squares[i][j] == null) {
                    String square = "[.]";
                    row.append(square);
                }
                else {
                    String square = "[" + squares[i][j].toString() + "]";
                    row.append(square);
                }
            }
            row.append('\n');
            board.append(row);
        }
        return board.toString();
    }
}
