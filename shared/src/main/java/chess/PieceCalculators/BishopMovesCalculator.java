package chess.PieceCalculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;

public class BishopMovesCalculator extends PieceMovesCalculator{
    public BishopMovesCalculator(ChessPosition current_position, ChessBoard board) {
        super(current_position, board);
    }

    public Collection<ChessMove> bishop_moves(){
        right_up();
        right_down();
        left_up();
        left_down();
        return total_moves;
    }
}
