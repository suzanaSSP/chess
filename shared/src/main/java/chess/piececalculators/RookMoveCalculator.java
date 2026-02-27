//package chess.piececalculators;
//
//import chess.ChessBoard;
//import chess.ChessMove;
//import chess.ChessPiece;
//import chess.ChessPosition;
//import java.util.ArrayList;
//import java.util.Collection;
//
//public class RookMoveCalculator extends PieceMoveCalculator {
//    public ChessPosition position;
//    public ChessBoard board;
//
//
//    public RookMoveCalculator(ChessPosition current_postion, ChessBoard board) {
//        super(current_postion, board);
//        this.position = current_postion;
//        this.board = board;
//    }
//
//    public Collection<ChessMove> rook_moves () {
//        Collection<ChessMove> all_moves = new ArrayList<>();
//        all_moves.addAll(moves_up());
//        all_moves.addAll(moves_down());
//        all_moves.addAll(moves_left());
//        all_moves.addAll(moves_right());
//
//        return all_moves;
//    }
//}
