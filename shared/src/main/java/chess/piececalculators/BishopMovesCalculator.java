package chess.piececalculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;

public class BishopMovesCalculator extends PieceMovesCalculator{
    public BishopMovesCalculator(ChessPosition currentPosition, ChessBoard board) {
        super(currentPosition, board);
    }

    public Collection<ChessMove> bishopMoves(){
        rightUp();
        rightDown();
        leftUp();
        leftDown();
        return totalMoves;
    }
}
