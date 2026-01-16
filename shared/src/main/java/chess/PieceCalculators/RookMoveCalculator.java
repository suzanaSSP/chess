package chess.PieceCalculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import java.util.ArrayList;
import java.util.Collection;

public class RookMoveCalculator extends PieceMoveCalculator {
    public ChessPosition position;
    public ChessBoard board;
    public RookMoveCalculator(ChessPosition current_postion, ChessBoard board) {
        super(current_postion, board);
        this.position = current_postion;
        this.board = board;
    }

    public Boolean valid_position(ChessMove move) {
        ChessPosition position = move.getEndPosition();
        if (position.getRow() >= 1 && position.getRow() <= 8 && position.getColumn() >= 1 && position.getColumn() <= 8) {
            ChessPiece own_piece = this.board.getPiece(this.position);
            if (this.board.getPiece(position) != null){
                if (this.board.getPiece(position).getTeamColor() == own_piece.getTeamColor()) {
                    return false;
                }
                else {return true;}
            }
            else {return true;}

        } else {
            return false;
        }
    }

    public Collection<ChessMove> rook_moves () {
        Collection<ChessMove> all_moves = new ArrayList<>();
        all_moves.addAll(moves_up());
        all_moves.addAll(moves_down());
        all_moves.addAll(moves_left());
        all_moves.addAll(moves_right());
        return all_moves;
    }
}
