package chess.piececalculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;

public class RookMovesCalculator extends PieceMovesCalculator {
    public RookMovesCalculator(ChessPosition currentPosition, ChessBoard board) {
        super(currentPosition, board);
    }

    public Collection<ChessMove> rookMoves() {
        movesUp();
        movesDown();
        movesLeft();
        movesRight();
        return totalMoves;
    }
}
