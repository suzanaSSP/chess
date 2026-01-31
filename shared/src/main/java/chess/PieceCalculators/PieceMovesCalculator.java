package chess.PieceCalculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

public class PieceMovesCalculator {
    public ChessPosition position;
    public ChessBoard board;
    public boolean just_killed = false;

    public ChessPosition new_position = null;
    public ChessMove new_move = null;

    public Collection<ChessMove> total_moves = new ArrayList<>();

    public PieceMovesCalculator(ChessPosition current_position, ChessBoard board) {
        this.position = current_position;
        this.board = board;
    }

    public Boolean valid_position(ChessPosition new_pos) {
        return new_pos.getRow() >= 1 && new_pos.getRow() <= 8 &&
                new_pos.getColumn() >= 1 && new_pos.getColumn() <= 8;
    }

    // Only for king and knight
    public Boolean can_move(ChessPosition possible_pos) {
        if (board.getPiece(possible_pos) != null) {
            return board.getPiece(possible_pos).pieceColor != board.getPiece(position).pieceColor;
        }
        else{
            return true;
        }
    }

    public void make_move() {
        if (valid_position(new_position)) {
            if (board.getPiece(new_position) == null) {
                new_move = new ChessMove(position, new_position, null);
            } else {
                if (board.getPiece(new_position).getTeamColor() != board.getPiece(position).pieceColor) {
                    new_move = new ChessMove(position, new_position, null);
                    just_killed = true;
                } else {
                    new_move = null;
                }
            }
        } else {
            new_move = null;
        }


    }

    public void moves_up() {
        for (int i = 1; i <= 8; i++) {
            new_position = new ChessPosition(position.getRow() + i, position.getColumn());
            make_move();
            if (new_move != null) {
                total_moves.add(new_move);
                if (just_killed) {
                    just_killed = false;
                    return;
                }
            } else {
                return;
            }
        }
    }

    public void moves_down() {
        for (int i = 1; i <= 8; i++) {
            new_position = new ChessPosition(position.getRow() - i, position.getColumn());
            make_move();
            if (new_move != null) {
                total_moves.add(new_move);
                if (just_killed) {
                    just_killed = false;
                    return;
                }
            } else {
                return;
            }
        }

    }

    public void moves_left() {
        for (int i = 1; i <= 8; i++) {
            new_position = new ChessPosition(position.getRow(), position.getColumn() - i);
            make_move();
            if (new_move != null) {
                total_moves.add(new_move);
                if (just_killed) {
                    just_killed = false;
                    return;
                }
            } else {
                return;
            }
        }

    }

    public void moves_right() {
        for (int i = 1; i <= 8; i++) {
            new_position = new ChessPosition(position.getRow(), position.getColumn() + i);
            make_move();
            if (new_move != null) {
                total_moves.add(new_move);
                if (just_killed) {
                    just_killed = false;
                    return;
                }
            } else {
                return;
            }
        }

    }

    //moving diagonal
    public void right_up() {
        for (int i = 1; i <= 8; i++) {
            new_position = new ChessPosition(position.getRow() + i, position.getColumn() + i);
            make_move();
            if (new_move != null) {
                total_moves.add(new_move);
                if (just_killed) {
                    just_killed = false;
                    return;
                }
            } else {
                return;
            }
        }
    }

    public void right_down() {
        for (int i = 1; i <= 8; i++) {
            new_position = new ChessPosition(position.getRow() - i, position.getColumn() + i);
            make_move();
            if (new_move != null) {
                total_moves.add(new_move);
                if (just_killed) {
                    just_killed = false;
                    return;
                }
            } else {
                return;
            }
        }
    }

    public void left_up() {
        for (int i = 1; i <= 8; i++) {
            new_position = new ChessPosition(position.getRow() + i, position.getColumn() - i);
            make_move();
            if (new_move != null) {
                total_moves.add(new_move);
                if (just_killed) {
                    just_killed = false;
                    return;
                }
            } else {
                return;
            }
        }
    }

    public void left_down() {
        for (int i = 1; i <= 8; i++) {
            new_position = new ChessPosition(position.getRow() - i, position.getColumn() - i);
            make_move();
            if (new_move != null) {
                total_moves.add(new_move);
                if (just_killed) {
                    just_killed = false;
                    return;
                }
            } else {
                return;
            }
        }
    }
}
