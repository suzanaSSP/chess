package chess.PieceCalculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;

public class QueenMovesCalculator extends PieceMovesCalculator{
    public QueenMovesCalculator(ChessPosition current_position, ChessBoard board) {
        super(current_position, board);
    }

    public Collection<ChessMove> queen_moves() {
        moves_right();
        moves_left();
        moves_up();
        moves_down();
        right_up();
        right_down();
        left_up();
        left_down();

        return total_moves;
    }
}
