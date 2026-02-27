package chess;

import chess.piececalculators.*;

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
            return rook.rookMoves();
        }
        else if(board.getPiece(myPosition).type == PieceType.BISHOP) {
            BishopMovesCalculator bishop = new BishopMovesCalculator(myPosition, board);
            return bishop.bishopMoves();
        }
        else if(board.getPiece(myPosition).type == PieceType.QUEEN) {
            chess.piececalculators.QueenMovesCalculator queen = new chess.piececalculators.QueenMovesCalculator(myPosition, board);
            return queen.queenMoves();
        }
        else if(board.getPiece(myPosition).type == PieceType.KING) {
            KingMovesCalculator king = new chess.piececalculators.KingMovesCalculator(myPosition, board);
            return king.kingMoves();
        }
        else if (board.getPiece(myPosition).type == PieceType.KNIGHT){
            chess.piececalculators.KnightMovesCalculator knight = new chess.piececalculators.KnightMovesCalculator(myPosition, board);
            return knight.knightMoves();
        }
        else if (board.getPiece(myPosition).type == PieceType.PAWN) {
            chess.piececalculators.PawnMovesCalculator pawn = new chess.piececalculators.PawnMovesCalculator(myPosition, board);
            return pawn.pawnMoves();
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
