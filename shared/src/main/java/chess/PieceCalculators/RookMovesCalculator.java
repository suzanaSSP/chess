package chess.PieceCalculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;

public class RookMovesCalculator extends PieceMovesCalculator{
    public RookMovesCalculator(ChessPosition current_position, ChessBoard board) {
        super(current_position, board);
    }

    public Collection<ChessMove> rook_moves() {
        moves_up();
        moves_down();
        moves_left();
        moves_right();
        return total_moves;
    }
}
