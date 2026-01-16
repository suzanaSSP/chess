package chess;

import chess.PieceCalculators.*;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;

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
        ChessPosition position;
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
        return this.pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return this.type;
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
            BishopMovesCalculator bishop = new BishopMovesCalculator(myPosition, board);
            Collection<ChessMove> bishop_moves = bishop.bishop_moves_calculator();
            return bishop_moves;
        }
        else if (piece.getPieceType() == PieceType.KING) {
            KingMovesCalculator king = new KingMovesCalculator(myPosition, board);
            Collection<ChessMove> king_moves;
            king_moves = king.king_moves();
            return king_moves;
        }
        else if (piece.getPieceType() == PieceType.ROOK) {
            RookMoveCalculator rook = new RookMoveCalculator(myPosition, board);
            Collection<ChessMove> rook_moves = rook.rook_moves();
            return rook_moves;
        }
        else if (piece.getPieceType() == PieceType.QUEEN) {
            QueenMovesCalculator queen = new QueenMovesCalculator(myPosition, board);
            Collection<ChessMove> queen_moves = queen.queen_moves();
            return queen_moves;
        }
        else if (piece.getPieceType() == PieceType.KNIGHT) {
            KnightMovesCalculator knight = new KnightMovesCalculator(myPosition, board);
            Set<ChessMove> knight_moves = knight.knight_moves();
            return knight_moves;
        }
        else{
            return List.of();
        }
    }
}
