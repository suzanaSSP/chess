package chess.PieceCalculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.*;

public class PawnMovesCalculator extends PieceMoveCalculator{
    public ChessPosition position;
    public ChessBoard board;
    public PawnMovesCalculator(ChessPosition current_position, ChessBoard board) {
        super(current_position, board);
        this.position = current_position;
        this.board = board;
    }

    public ChessMove can_promote(ChessPosition position) {
        if (position.getRow() == 8){
            ChessMove move = new ChessMove(this.position, position, ChessPiece.PieceType.QUEEN);
            return move;
        }
        else{return null;}
    }

    public Set<ChessMove> pawn_moves () {
        Set<ChessMove> all_moves = new HashSet<>();

        //one up
        ChessPosition new_position = new ChessPosition(this.position.getRow()+1, this.position.getColumn());
        if (valid_position(new_position)) {
            if (board.getPiece(new_position) == null) {
                if (new_position.getRow() == 8) {
                    all_moves.addAll(do_promotions(new_position));
                }
                else {
                    ChessMove move = new ChessMove(this.position, new_position, null);
                    all_moves.add(move);
                }
            }
        }

        //kill to the right
        ChessPosition possible_pos = new ChessPosition(this.position.getRow()+1, this.position.getColumn()+1);
        if (can_kill(possible_pos)){
            if (new_position.getRow() == 8) {
                all_moves.addAll(do_promotions(possible_pos));
            }
            else {
                ChessMove move = new ChessMove(this.position, new_position, null);
                all_moves.add(move);
            }
        }

        // kill to the left
        possible_pos = new ChessPosition(this.position.getRow()+1, this.position.getColumn()-1);
        if (can_kill(possible_pos)){
            if (new_position.getRow() == 8) {
                all_moves.addAll(do_promotions(possible_pos));
            }
            else {
                ChessMove move = new ChessMove(this.position, new_position, null);
                all_moves.add(move);
            }
        }

        // move two if in the beginning
        if (this.position.getRow() == 2) {
            new_position = new ChessPosition(this.position.getRow()+2, this.position.getColumn());
            if (valid_position(new_position)){
                ChessMove move = new ChessMove(this.position, new_position, null);
                all_moves.add(move);
            }
        }
        return all_moves;
    }

    public Boolean can_kill(ChessPosition possible_position) {
        if (board.getPiece(possible_position) == null) {
            return false;
        }
        else {
            if (board.getPiece(possible_position).getTeamColor() != board.getPiece(this.position).getTeamColor()){
                return true;
            }
            else {return false;}
        }
    }

    public Collection<ChessMove> do_promotions(ChessPosition new_position) {
        Collection<ChessMove> promotions = new ArrayList<>();
        ChessMove move_to_queen = new ChessMove(this.position, new_position, ChessPiece.PieceType.QUEEN);
        ChessMove move_to_bishop = new ChessMove(this.position, new_position, ChessPiece.PieceType.BISHOP);
        ChessMove move_to_rook = new ChessMove(this.position, new_position, ChessPiece.PieceType.ROOK);
        ChessMove move_to_knight = new ChessMove(this.position, new_position, ChessPiece.PieceType.KNIGHT);
        Collections.addAll(promotions, move_to_queen, move_to_knight, move_to_bishop, move_to_rook);
        return promotions;
    }
}

