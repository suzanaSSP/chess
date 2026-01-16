package chess.PieceCalculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

public class QueenMovesCalculator extends PieceMoveCalculator{

    public ChessPosition position;
    public ChessBoard board;
    public QueenMovesCalculator(ChessPosition current_postion, ChessBoard board) {
        super(current_postion, board);
        this.position = current_postion;
        this.board = board;
    }

    public Collection<ChessMove> queen_moves () {
        Collection<ChessMove> all_moves = new ArrayList<>();
        all_moves.addAll(moves_up());
        all_moves.addAll(moves_down());
        all_moves.addAll(moves_left());
        all_moves.addAll(moves_right());
        all_moves.addAll(bishop_move_right_up());
        all_moves.addAll(bishop_move_right_down());
        all_moves.addAll(bishop_move_left_up());
        all_moves.addAll(bishop_move_left_down());
        return all_moves;
    }
}
