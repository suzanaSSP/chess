package chess.PieceCalculators;
import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class PieceMoveCalculator {
    public ChessPosition position;
    public ChessBoard board;

    //Stop when pass by piece
    public Boolean just_killed = false;

    public ChessPosition new_position = null;
    public ChessMove new_move = null;

    public PieceMoveCalculator(ChessPosition current_position, ChessBoard board) {
        this.board = board;
        this.position = current_position;
    }

    public Boolean valid_position(ChessPosition position) {
        // position is inside the board and is null
        if (position.getRow() >= 1 && position.getRow() <= 8 && position.getColumn() >= 1 && position.getColumn() <= 8) {
            return true;
        }
        else {return false;}
    }

    public Boolean it_killed(ChessPosition new_position) {
        return this.board.getPiece(new_position).getTeamColor() != this.board.getPiece(this.position).getTeamColor();
    }

    public ChessMove make_move (ChessPosition new_position) {
        ChessMove move = new ChessMove(this.position, new_position, null);
        if (!valid_position(new_position)) {return null;}
        else {
            if (null == this.board.getPiece(new_position)){
                return move;
            }
            else{
                if (this.board.getPiece(new_position).getTeamColor() == this.board.getPiece(this.position).getTeamColor()) {
                    return null;
                }
                else {
                    just_killed = true;
                   return move;
                }
            }
        }
    }

    public Collection<ChessMove> moves_up () {
        Collection<ChessMove> rook_moves = new ArrayList<>();

        for (int i = 1; i <= 8; i++) {
            this.new_position = new ChessPosition(this.position.getRow() + i, this.position.getColumn());
            this.new_move = make_move(this.new_position);
            if (null != new_move){
                rook_moves.add(new_move);
                if (just_killed){
                    just_killed = false;
                    return rook_moves;
                }
            }
            else {
                return rook_moves;
            }
        }
        return rook_moves;
    }

    public Collection<ChessMove> moves_down () {
        Collection<ChessMove> rook_moves = new ArrayList<>();

        for (int i = 1; i <= 8; i++) {
            this.new_position = new ChessPosition(this.position.getRow() - i, this.position.getColumn());
            this.new_move = make_move(this.new_position);
            if (null != new_move){
                rook_moves.add(new_move);
                if (just_killed){
                    just_killed = false;
                    return rook_moves;
                }
            }
            else {
                return rook_moves;
            }
        }
        return rook_moves;
    }



    public Collection<ChessMove> moves_left () {
        Collection<ChessMove> rook_moves = new ArrayList<>();

        for (int i = 1; i <= 8; i++) {
            this.new_position = new ChessPosition(this.position.getRow(), this.position.getColumn()-i);
            this.new_move = make_move(this.new_position);
            if (null != new_move) {
                rook_moves.add(new_move);
                if (just_killed) {
                    just_killed = false;
                    return rook_moves;
                }
            }
            else {
                return rook_moves;
            }
        }
        return rook_moves;
    }

    public Collection<ChessMove> moves_right () {
        Collection<ChessMove> rook_moves = new ArrayList<>();

        for (int i = 1; i <= 8; i++) {
            this.new_position = new ChessPosition(this.position.getRow(), this.position.getColumn()+i);
            this.new_move = make_move(this.new_position);
            if (null != new_move){
                rook_moves.add(new_move);
                if (just_killed){
                    just_killed = false;
                    return rook_moves;
                }
            }
            else {
                return rook_moves;
            }
        }
        return rook_moves;
    }

    public Collection<ChessMove> bishop_move_right_up () {
        List<ChessMove> bishop_moves = new java.util.ArrayList<>();
        for (int i = 1; i <= 8; i++) {
            this.new_position = new ChessPosition(this.position.getRow() + i, this.position.getColumn() + i);
            this.new_move = make_move(this.new_position);
            if (null != this.new_move) {
                bishop_moves.add(new_move);
                if (just_killed) {
                    just_killed = false;
                    return bishop_moves;
                }
            } else {
                return bishop_moves;
            }
        }
        return bishop_moves;
    }


    public Collection<ChessMove> bishop_move_right_down () {
        List<ChessMove> bishop_moves = new java.util.ArrayList<>();
        for (int i = 1; i <= 8; i++) {
            this.new_position = new ChessPosition(this.position.getRow() + i, this.position.getColumn() - i);
            this.new_move = make_move(this.new_position);
            if (null != this.new_move) {
                bishop_moves.add(new_move);
                if (just_killed) {
                    just_killed = false;
                    return bishop_moves;
                }
            } else {
                return bishop_moves;
            }
        }
        return bishop_moves;
    }

    public Collection<ChessMove> bishop_move_left_up () {
        List<ChessMove> bishop_moves = new java.util.ArrayList<>();
        for (int i = 1; i <= 8; i++) {
            this.new_position = new ChessPosition(this.position.getRow() - i, this.position.getColumn() + i);
            this.new_move = make_move(this.new_position);
            if (null != this.new_move) {
                bishop_moves.add(new_move);
                if (just_killed) {
                    just_killed = false;
                    return bishop_moves;
                }
            } else {
                return bishop_moves;
            }
        }
        return bishop_moves;
    }

        public Collection<ChessMove> bishop_move_left_down () {
            List<ChessMove> bishop_moves = new java.util.ArrayList<>();
            for (int i = 1; i <= 8; i++) {
                this.new_position = new ChessPosition(this.position.getRow() - i, this.position.getColumn() - i);
                this.new_move = make_move(this.new_position);
                if (null != this.new_move) {
                    bishop_moves.add(new_move);
                    if (just_killed) {
                        just_killed = false;
                        return bishop_moves;
                    }
                } else {
                    return bishop_moves;
                }
            }
            return bishop_moves;
        }
    }





