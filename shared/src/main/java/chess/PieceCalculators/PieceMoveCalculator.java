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

    public PieceMoveCalculator(ChessPosition current_position, ChessBoard board) {
        this.board = board;
        this.position = current_position;
    }

    public Boolean valid_position(ChessPosition position) {
        if (position.getRow() >= 1 && position.getRow() <= 8 && position.getColumn() >= 1 && position.getColumn() <= 8) {
            return true;
        }
        else {return false;}
    }


    public Collection<ChessMove> moves_up () {
        Collection<ChessMove> rook_moves = new ArrayList<>();

        for (int i = 1; i <= 8; i++) {
            ChessPosition new_position = new ChessPosition(this.position.getRow() + i, this.position.getColumn());
            ChessMove move = new ChessMove(this.position, new_position, null);
            if (valid_position(new_position)) {
                if (this.board.getPiece(new_position) != null) {
                    ChessPiece own_piece = this.board.getPiece(this.position);
                    if (this.board.getPiece(new_position).getTeamColor() == own_piece.getTeamColor()) {
                        break;
                    } else {
                        rook_moves.add(move);
                        break;
                    }
                } else {
                    rook_moves.add(move);
                }
            }
            else {break;}
        }
        return rook_moves;
    }

    public Collection<ChessMove> moves_down () {
        Collection<ChessMove> rook_moves = new ArrayList<>();

        for (int i = 1; i <= 8; i++) {
            ChessPosition new_position = new ChessPosition(this.position.getRow() - i, this.position.getColumn());
            ChessMove move = new ChessMove(this.position, new_position, null);
            if (valid_position(new_position)) {
                if (this.board.getPiece(new_position) != null) {
                    ChessPiece own_piece = this.board.getPiece(this.position);
                    if (this.board.getPiece(new_position).getTeamColor() == own_piece.getTeamColor()) {
                        break;
                    } else {
                        rook_moves.add(move);
                        break;
                    }
                } else {
                    rook_moves.add(move);
                }
            }
            else {break;}
        }
        return rook_moves;
    }

    public Collection<ChessMove> moves_left () {
        Collection<ChessMove> rook_moves = new ArrayList<>();

        for (int i = 1; i <= 8; i++) {
            ChessPosition new_position = new ChessPosition(this.position.getRow(), this.position.getColumn() - i);
            ChessMove move = new ChessMove(this.position, new_position, null);
            if (valid_position(new_position)) {
                if (this.board.getPiece(new_position) != null) {
                    ChessPiece own_piece = this.board.getPiece(this.position);
                    if (this.board.getPiece(new_position).getTeamColor() == own_piece.getTeamColor()) {
                        break;
                    } else {
                        rook_moves.add(move);
                        break;
                    }
                } else {
                    rook_moves.add(move);
                }
            }
            else {break;}
        }
        return rook_moves;
    }

    public Collection<ChessMove> moves_right () {
        Collection<ChessMove> rook_moves = new ArrayList<>();

        for (int i = 1; i <= 8; i++) {
            ChessPosition new_position = new ChessPosition(this.position.getRow(), this.position.getColumn() + i);
            ChessMove move = new ChessMove(this.position, new_position, null);
            if (valid_position(new_position)) {
                if (this.board.getPiece(new_position) != null) {
                    ChessPiece own_piece = this.board.getPiece(this.position);
                    if (this.board.getPiece(new_position).getTeamColor() == own_piece.getTeamColor()) {
                        break;
                    } else {
                        rook_moves.add(move);
                        break;
                    }
                } else {
                    rook_moves.add(move);
                }
            }
            else {break;}
        }
        return rook_moves;
    }

    public Collection<ChessMove> bishop_move_right_up () {
        List<ChessMove> bishop_moves = new java.util.ArrayList<>();
        for (int i = 1; i <= 8; i++) {
            ChessPosition new_position = new ChessPosition(this.position.getRow() + i, this.position.getColumn() + i);
            ChessMove move = new ChessMove(new ChessPosition(this.position.getRow(), this.position.getColumn()),
                    new_position, null);
            if (valid_position(new_position)) {
                if (this.board.getPiece(new_position) != null) {
                    ChessPiece own_piece = this.board.getPiece(this.position);
                    if (this.board.getPiece(new_position).getTeamColor() == own_piece.getTeamColor()) {
                        break;
                    } else {
                        bishop_moves.add(move);
                        break;
                    }
                } else {
                    bishop_moves.add(move);
                }
            }
            else {break;}
        }
        return bishop_moves;
    }

    public Collection<ChessMove> bishop_move_right_down () {
        List<ChessMove> bishop_moves = new java.util.ArrayList<>();
        for (int i = 1; i <= 8; i++) {
            ChessPosition new_position = new ChessPosition(this.position.getRow() + i, this.position.getColumn() - i);
            ChessMove move = new ChessMove(new ChessPosition(this.position.getRow(), this.position.getColumn()),
                    new_position, null);
            if (valid_position(new_position)) {
                if (this.board.getPiece(new_position) != null) {
                    ChessPiece own_piece = this.board.getPiece(this.position);
                    if (this.board.getPiece(new_position).getTeamColor() == own_piece.getTeamColor()) {
                        break;
                    } else {
                        bishop_moves.add(move);
                        break;
                    }
                } else {
                    bishop_moves.add(move);
                }
            }
            else {break;}
        }
        return bishop_moves;
    }

    public Collection<ChessMove> bishop_move_left_up () {
        List<ChessMove> bishop_moves = new java.util.ArrayList<>();
        for (int i = 1; i <= 8; i++) {
            ChessPosition new_position = new ChessPosition(this.position.getRow() - i, this.position.getColumn() + i);
            ChessMove move = new ChessMove(new ChessPosition(this.position.getRow(), this.position.getColumn()),
                    new_position, null);
            if (valid_position(new_position)) {
                if (this.board.getPiece(new_position) != null) {
                    ChessPiece own_piece = this.board.getPiece(this.position);
                    if (this.board.getPiece(new_position).getTeamColor() == own_piece.getTeamColor()) {
                        break;
                    } else {
                        bishop_moves.add(move);
                        break;
                    }
                } else {
                    bishop_moves.add(move);
                }

            }
            else {break;}
        }
        return bishop_moves;
    }

        public Collection<ChessMove> bishop_move_left_down () {
            List<ChessMove> bishop_moves = new java.util.ArrayList<>();
            for (int i = 1; i <= 8; i++) {
                ChessPosition new_position = new ChessPosition(this.position.getRow() - i, this.position.getColumn() - i);
                ChessMove move = new ChessMove(new ChessPosition(this.position.getRow(), this.position.getColumn()), new_position, null);
                if (valid_position(new_position)) {
                    if (this.board.getPiece(new_position) != null) {
                        ChessPiece own_piece = this.board.getPiece(this.position);
                        if (this.board.getPiece(new_position).getTeamColor() == own_piece.getTeamColor()) {
                            break;
                        } else {
                            bishop_moves.add(move);
                            break;
                        }
                    } else {
                        bishop_moves.add(move);
                    }
                }
                else {break;}
            }
            return bishop_moves;
        }
    }





