package chess.PieceCalculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.Collection;
import java.util.List;

public class BishopMovesCalculator extends  PieceMoveCalculator{
    public ChessPosition position;
    public ChessBoard board;
    public BishopMovesCalculator(ChessPosition current_postion, ChessBoard board) {
        super(current_postion, board);
        this.position = current_postion;
        this.board = board;
    }

    public Collection<ChessMove> bishop_moves_calculator() {
        List<ChessMove> bishop_moves = new java.util.ArrayList<>();
        bishop_moves.addAll(bishop_move_right_up());
        bishop_moves.addAll(bishop_move_right_down());
        bishop_moves.addAll(bishop_move_left_up());
        bishop_moves.addAll(bishop_move_left_down());
        return bishop_moves;
    }


}
