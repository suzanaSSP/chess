package chess;

import java.util.Arrays;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard implements Cloneable {

    ChessPiece[][] board = new ChessPiece[8][8];

    public ChessBoard() {
        
    }

     @Override
     public ChessBoard clone(){
         try{
             ChessBoard clone_board = (ChessBoard) super.clone();

//             clone_board.board = new ChessPiece[8][8];
//
//             for (int i=0; i<8;i++) {
//                 for (int j=0; j<8;j++) {
//                     if (this.getPiece(new ChessPosition(i,j)) != null){
//                         clone_board.board[i][j] = this.getPiece(new ChessPosition(i, j));
//                     }
//                 }
//             }

             return clone_board;
         }
         catch (CloneNotSupportedException e) {
             throw new RuntimeException(e);
         }
     }
    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        board[position.getRow()-1][position.getColumn()-1] = piece;
    }

    public void removePiece(ChessPosition position) {
        board[position.getRow()-1][position.getColumn()-1] = null;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return board[position.getRow()-1][position.getColumn()-1];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        board = new ChessPiece[8][8];
        add_start_pieces();
    }

    public void add_start_pieces() {
        add_start_pawns();
        add_start_bishops();
        add_start_knight();
        add_start_rooks();

        board[0][3] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
        board[0][4] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
        board[7][3] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
        board[7][4] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);

    }

    public void add_start_pawns(){
        ChessPiece white_pawn = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        ChessPiece black_pawn = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        for (int i=0;i<8;i++){
            //add white pawn
            board[1][i] = white_pawn;
            board[6][i] = black_pawn;
        }
    }

    public void add_start_bishops(){
        ChessPiece white_bishop = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        ChessPiece black_bishop = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);

        board[0][2] = white_bishop;
        board[0][5] = white_bishop;
        board[7][2] = black_bishop;
        board[7][5] = black_bishop;

    }
    public void add_start_rooks(){
        ChessPiece white_rook = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        ChessPiece black_rook = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);

        board[0][0] = white_rook;
        board[0][7] = white_rook;
        board[7][0] = black_rook;
        board[7][7] = black_rook;
    }
    public void add_start_knight(){
        ChessPiece white_knight = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        ChessPiece black_knight = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);

        board[0][1] = white_knight;
        board[0][6] = white_knight;
        board[7][1] = black_knight;
        board[7][6] = black_knight;
    }

    @Override
    public String toString() {
        StringBuilder print_board = new StringBuilder();
        for (int i=0; i<8;i++) {
            StringBuilder row = new StringBuilder();
            for (int j=0;j<8;j++) {
                if (board[i][j] == null) {
                    row.append("[.]");
                }
                else{
                    String square = String.format("%s", board[i][j].toString());
                    row.append(square);
                }
            }
            row.append("\n");
            print_board.append(row);
        }
        return print_board.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(board, that.board);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }


}
