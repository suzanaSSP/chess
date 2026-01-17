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
    public void addStartPieces() {
        // pawns
        for (int i=1; i<=8;i++) {
            ChessPosition white_new_pos = new ChessPosition(2, i);
            ChessPiece new_white_pawn = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
            squares[white_new_pos.getRow()][white_new_pos.getColumn()] = new_white_pawn;
        }
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
    }

    @Override
    public boolean equals(Object o) {
        if (this.squares == o) {
            return true;
        }
        if (o == null || this.squares.getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(squares, that.squares);
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
