package chess.piececalculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;

public class QueenMovesCalculator extends PieceMovesCalculator{
    public QueenMovesCalculator(ChessPosition currentPosition, ChessBoard board) {
        super(currentPosition, board);
    }

    public Collection<ChessMove> queenMoves() {
        movesRight();
        movesLeft();
        movesUp();
        movesDown();
        rightUp();
        rightDown();
        leftUp();
        leftDown();

        return totalMoves;
    }
}
