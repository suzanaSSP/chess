package chess;

import chess.PieceCalculators.*;

import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    public ChessGame.TeamColor pieceColor;
    public ChessPiece.PieceType type;
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
        if (board.getPiece(myPosition).type == PieceType.ROOK){
            RookMovesCalculator rook = new RookMovesCalculator(myPosition, board);
            return rook.rook_moves();
        }
        else if(board.getPiece(myPosition).type == PieceType.BISHOP) {
            BishopMovesCalculator bishop = new BishopMovesCalculator(myPosition, board);
            return bishop.bishop_moves();
        }
        else if(board.getPiece(myPosition).type == PieceType.QUEEN) {
            QueenMovesCalculator queen = new QueenMovesCalculator(myPosition, board);
            return queen.queen_moves();
        }
        else if(board.getPiece(myPosition).type == PieceType.KING) {
            KingMovesCalculator king = new KingMovesCalculator(myPosition, board);
            return king.king_moves();
        }
        else if (board.getPiece(myPosition).type == PieceType.KNIGHT){
            KnightMovesCalculator knight = new KnightMovesCalculator(myPosition, board);
            return knight.knight_moves();
        }
        else if (board.getPiece(myPosition).type == PieceType.PAWN) {
            PawnMovesCalculator pawn = new PawnMovesCalculator(myPosition, board);
            return  pawn.pawn_moves();
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    @Override
    public String toString() {
        return String.format("[%s %s]", pieceColor, type);
    }
}
