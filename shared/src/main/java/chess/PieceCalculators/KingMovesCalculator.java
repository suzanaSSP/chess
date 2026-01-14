package chess.PieceCalculators;
import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;
import java.util.List;


public class KingMovesCalculator extends PieceMoveCalculator {
    public ChessPosition position;
    public KingMovesCalculator(ChessPosition current_postion) {
        super(current_postion);
        this.position = current_postion;

    }

    public Collection<ChessMove> king_moves (ChessBoard board) {
        List<ChessMove> kingmoves = List.of();
        //right up
        ChessPosition new_position = new ChessPosition(this.position.getRow()+1, this.position.getColumn()+1);
        ChessMove move = new ChessMove(this.position, new_position, null);
        if (valid_position(board, move)){
            kingmoves.add(move);
        }
        //right down
        ChessPosition new_position2 = new ChessPosition(this.position.getRow()+1, this.position.getColumn()-1);
        ChessMove move2 = new ChessMove(this.position, new_position2, null);
        if (valid_position(board, move2)){
            kingmoves.add(move2);
        }
        //left up
        ChessPosition new_position3 = new ChessPosition(this.position.getRow()-1, this.position.getColumn()+1);
        ChessMove move3 = new ChessMove(this.position, new_position3, null);
        if (valid_position(board, move3)){
            kingmoves.add(move3);
        }
        //left down
        ChessPosition new_position4 = new ChessPosition(this.position.getRow()-1, this.position.getColumn()-1);
        ChessMove move4 = new ChessMove(this.position, new_position4, null);
        if (valid_position(board, move4)){
            kingmoves.add(move4);
        }
        return kingmoves;
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
