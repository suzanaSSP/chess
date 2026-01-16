package chess.PieceCalculators;

import chess.ChessBoard;
import chess.ChessPosition;

public class PawnMovesCalculator extends PieceMoveCalculator{
    public ChessPosition position;
    public ChessBoard board;
    public PawnMovesCalculator(ChessPosition current_position, ChessBoard board) {
        super(current_position, board);
        this.position = current_position;
        this.board = board;
    }
}
