package chess.PieceCalculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class KnightMovesCalculator extends PieceMoveCalculator {
    public ChessPosition position;
    public ChessBoard board;
    public KnightMovesCalculator(ChessPosition current_position, ChessBoard board) {
        super(current_position, board);
        this.position = current_position;
        this.board = board;
    }

    public Set<ChessMove> knight_moves () {
        Set<ChessMove> all_moves = new HashSet<>();

        //three right one up
        ChessPosition new_position1 = new ChessPosition(this.position.getRow()+1, this.position.getColumn()+2);
        if (valid_position(new_position1) && can_kill_or_move(new_position1)) {
            ChessMove new_move = new ChessMove(this.position, new_position1, null);
            all_moves.add(new_move);
        }

        // three right one down
        ChessPosition new_position2 = new ChessPosition(this.position.getRow()-1, this.position.getColumn()+2);
        if (valid_position(new_position2) && can_kill_or_move(new_position2)) {
            ChessMove new_move = new ChessMove(this.position, new_position2, null);
            all_moves.add(new_move);
        }

        // three down one right
        ChessPosition new_position3 = new ChessPosition(this.position.getRow()-2, this.position.getColumn()+1);
        if (valid_position(new_position3) && can_kill_or_move(new_position3)) {
            ChessMove new_move = new ChessMove(this.position, new_position3, null);
            all_moves.add(new_move);
        }

        // three down one left
        ChessPosition new_position4 = new ChessPosition(this.position.getRow()-2, this.position.getColumn()-1);
        if (valid_position(new_position4) && can_kill_or_move(new_position4)) {
            ChessMove new_move = new ChessMove(this.position, new_position4, null);
            all_moves.add(new_move);
        }

        //three left one down
        ChessPosition new_position5 = new ChessPosition(this.position.getRow()-1, this.position.getColumn()-2);
        if (valid_position(new_position5) && can_kill_or_move(new_position5)) {
            ChessMove new_move = new ChessMove(this.position, new_position5, null);
            all_moves.add(new_move);
        }

        //three left one up
        ChessPosition new_position6 = new ChessPosition(this.position.getRow()+1, this.position.getColumn()-2);
        if (valid_position(new_position6) && can_kill_or_move(new_position6)) {
            ChessMove new_move = new ChessMove(this.position, new_position6, null);
            all_moves.add(new_move);
        }

        //three up one left
        ChessPosition new_position7 = new ChessPosition(this.position.getRow()+2, this.position.getColumn()-1);
        if (valid_position(new_position7) && can_kill_or_move(new_position7)) {
            ChessMove new_move = new ChessMove(this.position, new_position7, null);
            all_moves.add(new_move);
        }

        //three up one right
        ChessPosition new_position8 = new ChessPosition(this.position.getRow()+2, this.position.getColumn()+1);
        if (valid_position(new_position8) && can_kill_or_move(new_position8)) {
            ChessMove new_move = new ChessMove(this.position, new_position8, null);
            all_moves.add(new_move);
        }

        return all_moves;
    }

    public Boolean can_kill_or_move(ChessPosition new_position) {
        if (this.board.getPiece(new_position) != null) {
            ChessPiece own_piece = this.board.getPiece(this.position);
            if (this.board.getPiece(new_position).getTeamColor() == own_piece.getTeamColor()) {
                return false;
            } else {
                return true;
            }
        }
        else{
            return true;
        }
    }
}
