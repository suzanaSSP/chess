package chess.PieceCalculators;
import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;


public class PieceMoveCalculator{
    public ChessPosition position;

    public PieceMoveCalculator(ChessPosition current_position) {
        this.position = current_position;
    }

    public Boolean valid_position(ChessBoard board, ChessMove move) {
        ChessPosition position = move.getEndPosition();
        if (position.getRow() >= 1 && position.getRow() <= 8 &&
                position.getColumn() >= 1 && position.getColumn() <= 8) {
            return true;
        } else {
            return false;
        }
    }
}

