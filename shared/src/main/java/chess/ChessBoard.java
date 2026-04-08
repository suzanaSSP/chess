package chess;

import java.util.Arrays;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard  {

    ChessPiece[][] board = new ChessPiece[8][8];
    boolean isBlack = false;

    public ChessBoard() {
        
    }

     public ChessBoard(ChessBoard other){
         for (int i=1; i<=8;i++) {
             for (int j=1; j<=8;j++) {
                 if (other.getPiece(new ChessPosition(i,j)) != null){
                     this.board[i-1][j-1] = other.getPiece(new ChessPosition(i, j));
                 }
             }
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
        addWhiteStartPieces();
    }

    public void setToBlack() {
        isBlack = true;
    }

    public void addWhiteStartPieces() {
        addStartPawns();
        addStartBishops();
        addStartKnight();
        addStartRooks();

        board[0][3] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
        board[0][4] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
        board[7][3] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
        board[7][4] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);

    }

    public void addBlackStartPieces() {
        addStartPawns();
        addStartBishops();
        addStartKnight();
        addStartRooks();

        board[0][4] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
        board[0][3] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
        board[7][4] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
        board[7][3] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);

    }

    public void addStartPawns(){
        ChessPiece whitePawn = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        ChessPiece blackPawn = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        for (int i=0;i<8;i++){
            //add white pawn
            board[1][i] = whitePawn;
            board[6][i] = blackPawn;
        }
    }

    public void addStartBishops(){
        ChessPiece whiteBishop = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        ChessPiece blackBishop = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);

        board[0][2] = whiteBishop;
        board[0][5] = whiteBishop;
        board[7][2] = blackBishop;
        board[7][5] = blackBishop;

    }
    public void addStartRooks(){
        ChessPiece whiteRook = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        ChessPiece blackRook = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);

        board[0][0] = whiteRook;
        board[0][7] = whiteRook;
        board[7][0] = blackRook;
        board[7][7] = blackRook;
    }
    public void addStartKnight(){
        ChessPiece whiteKnight = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        ChessPiece blackKnight = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);

        board[0][1] = whiteKnight;
        board[0][6] = whiteKnight;
        board[7][1] = blackKnight;
        board[7][6] = blackKnight;
    }

    @Override
    public String toString() {
        StringBuilder printBoard = new StringBuilder();
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
            printBoard.append(row);
        }
        return printBoard.toString();
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
