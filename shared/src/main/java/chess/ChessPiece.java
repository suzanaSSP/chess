package chess;

import java.util.Collection;
import java.util.List;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;       

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPiece piece = board.getPiece(myPosition);
        if (piece.getPieceType() == PieceType.BISHOP) {
            return bishopMoves(board, myPosition);
        } else {    
            return List.of();
        }
    }

    
    public Collection<ChessMove> bishopMoves(ChessBoard board, ChessPosition myPosition) {
        List<ChessMove> bishop_moves = new java.util.ArrayList<>();
        for (int i = 1; i <= 8; i++) {  
            ChessMove move = new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()), 
            new ChessPosition(myPosition.getRow()+i, myPosition.getColumn()+i), null);
            if (valid_position(board, move)) {
                bishop_moves.add(move);
            }

            ChessMove move2 = new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()), 
            new ChessPosition(myPosition.getRow()-i, myPosition.getColumn()+i), null);
            if (valid_position(board, move2)) {
                bishop_moves.add(move2);
            }

            ChessMove move3 = new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()), 
                new ChessPosition(myPosition.getRow()+i, myPosition.getColumn()-i), null);
            if (valid_position(board, move3)) {
                bishop_moves.add(move3);
            }
            ChessMove move4 = new ChessMove(new ChessPosition(myPosition.getRow(), myPosition.getColumn()), 
                new ChessPosition(myPosition.getRow()-i, myPosition.getColumn()-i), null);
                
            if (valid_position(board, move4)) {
                bishop_moves.add(move4);
            }
        }
        return bishop_moves;
    }

    public Boolean valid_position(ChessBoard board, ChessMove move) {
        ChessPosition position = move.getEndPosition();
        if (position.getRow() >= 1 && position.getRow() <= 8 &&
            position.getColumn() >= 1 && position.getColumn() <= 8 && 
            board.getPiece(position) == null) {
                return true;
            } else {
                return false;
            }
    }

}
