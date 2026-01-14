package chess.PieceCalculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.Collection;
import java.util.List;

public class BishopMovesCalculator extends  PieceMoveCalculator{
    public ChessPosition position;
    public BishopMovesCalculator(ChessPosition current_position){
        super(current_position);
        this.position = current_position;
    }

    public Collection<ChessMove> bishop_moves_calculator(ChessBoard board) {
        List<ChessMove> bishop_moves = new java.util.ArrayList<>();
        bishop_moves.addAll(bishop_move_right_up(board));
        bishop_moves.addAll(bishop_move_right_down(board));
        bishop_moves.addAll(bishop_move_left_up(board));
        bishop_moves.addAll(bishop_move_left_down(board));
        return bishop_moves;
    }

    public Collection<ChessMove> bishop_move_right_up(ChessBoard board) {
        List<ChessMove> bishop_moves = new java.util.ArrayList<>();
        for (int i = 1; i <= 8; i++) {
            ChessPosition new_position = new ChessPosition(this.position.getRow()+i, this.position.getColumn()+i);
            ChessMove move = new ChessMove(new ChessPosition(this.position.getRow(), this.position.getColumn()),
                    new_position, null);
            if (valid_position(board, move)) {
                if (board.getPiece(new_position) != null) {
                  ChessPiece own_piece = board.getPiece(this.position);
                  if (board.getPiece(new_position).getTeamColor() == own_piece.getTeamColor()) {
                      break;
                  }
                  else {
                      bishop_moves.add(move);
                      break;
                  }
                }
                else {
                    bishop_moves.add(move);
                }

            }
        }
        return bishop_moves;
    }

    public Collection<ChessMove> bishop_move_right_down(ChessBoard board) {
        List<ChessMove> bishop_moves = new java.util.ArrayList<>();
        for (int i = 1; i <= 8; i++) {
            ChessPosition new_position = new ChessPosition(this.position.getRow()+i, this.position.getColumn()-i);
            ChessMove move = new ChessMove(new ChessPosition(this.position.getRow(), this.position.getColumn()),
                    new_position, null);
            if (valid_position(board, move)) {
                if (board.getPiece(new_position) != null) {
                    ChessPiece own_piece = board.getPiece(this.position);
                    if (board.getPiece(new_position).getTeamColor() == own_piece.getTeamColor()) {
                        break;
                    }
                    else {
                        bishop_moves.add(move);
                        break;
                    }
                }
                else {
                    bishop_moves.add(move);
                }

            }
        }
        return bishop_moves;
    }

    public Collection<ChessMove> bishop_move_left_up(ChessBoard board) {
        List<ChessMove> bishop_moves = new java.util.ArrayList<>();
        for (int i = 1; i <= 8; i++) {
            ChessPosition new_position = new ChessPosition(this.position.getRow()-i, this.position.getColumn()+i);
            ChessMove move = new ChessMove(new ChessPosition(this.position.getRow(), this.position.getColumn()),
                    new_position, null);
            if (valid_position(board, move)) {
                if (board.getPiece(new_position) != null) {
                    ChessPiece own_piece = board.getPiece(this.position);
                    if (board.getPiece(new_position).getTeamColor() == own_piece.getTeamColor()) {
                        break;
                    }
                    else {
                        bishop_moves.add(move);
                        break;
                    }
                }
                else {
                    bishop_moves.add(move);
                }

            }
        }
        return bishop_moves;
    }

    public Collection<ChessMove> bishop_move_left_down(ChessBoard board) {
        List<ChessMove> bishop_moves = new java.util.ArrayList<>();
        for (int i = 1; i <= 8; i++) {
            ChessPosition new_position = new ChessPosition(this.position.getRow()-i, this.position.getColumn()-i);
            ChessMove move = new ChessMove(new ChessPosition(this.position.getRow(), this.position.getColumn()),
                    new_position, null);
            if (valid_position(board, move)) {
                if (board.getPiece(new_position) != null) {
                    ChessPiece own_piece = board.getPiece(this.position);
                    if (board.getPiece(new_position).getTeamColor() == own_piece.getTeamColor()) {
                        break;
                    }
                    else {
                        bishop_moves.add(move);
                        break;
                    }
                }
                else {
                    bishop_moves.add(move);
                }

            }
        }
        return bishop_moves;
    }

}
