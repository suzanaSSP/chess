package chess.PieceCalculators;

import chess.*;

import java.util.*;

public class PawnMovesCalculator extends PieceMoveCalculator{
    public ChessPosition position;
    public ChessBoard board;
    public PawnMovesCalculator(ChessPosition current_position, ChessBoard board) {
        super(current_position, board);
        this.position = current_position;
        this.board = board;
    }


    public Set<ChessMove> white_pawn_moves() {
        Set<ChessMove> all_moves = new HashSet<>();

        //kill to the right
        ChessPosition possible_pos = new ChessPosition(this.position.getRow()+1, this.position.getColumn()+1);
        if (can_kill(possible_pos)){
            all_moves.addAll(kill(possible_pos));
        }

        // kill to the left
        possible_pos = new ChessPosition(this.position.getRow()+1, this.position.getColumn()-1);
        if (can_kill(possible_pos)){
            all_moves.addAll(kill(possible_pos));
        }

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
            else {
                return all_moves;
            }
        }

        // move two if in the beginning
        if (this.position.getRow() == 2) {
            new_position = new ChessPosition(this.position.getRow()+2, this.position.getColumn());
            if (valid_position(new_position)){
                if (board.getPiece(new_position) == null) {
                    ChessMove move = new ChessMove(this.position, new_position, null);
                    all_moves.add(move);
                }
            }
        }
        return all_moves;
    }

    public Set<ChessMove> black_pawn_moves() {
        Set<ChessMove> all_moves = new HashSet<>();

        //kill to the right
        ChessPosition possible_pos = new ChessPosition(this.position.getRow()-1, this.position.getColumn()+1);
        if (valid_position(possible_pos)) {
            all_moves.addAll(kill(possible_pos));
        }


        // kill to the left
        possible_pos = new ChessPosition(this.position.getRow()-1, this.position.getColumn()-1);
        if (can_kill(possible_pos)){
            all_moves.addAll(kill(possible_pos));
        }

        //one up (one up before moving two is important to break out of function if there's a piece in front
        ChessPosition new_position = new ChessPosition(this.position.getRow()-1, this.position.getColumn());
        if (valid_position(new_position)) {
            //check if there's a piece in front
            if (board.getPiece(new_position) == null) {
                if (new_position.getRow() == 1) {
                    all_moves.addAll(do_promotions(new_position));
                }
                else {
                    ChessMove move = new ChessMove(this.position, new_position, null);
                    all_moves.add(move);
                }
            }
            else {
                return all_moves;
            }

        }

        // move two if in the beginning
        if (this.position.getRow() == 7) {
            new_position = new ChessPosition(this.position.getRow()-2, this.position.getColumn());
            if (valid_position(new_position)){
                if (board.getPiece(new_position) == null) {
                    ChessMove move = new ChessMove(this.position, new_position, null);
                    all_moves.add(move);
                }
            }
        }
        return all_moves;
    }

    public Set<ChessMove> pawn_moves () {
        Set<ChessMove> all_moves = new HashSet<>();
        if (board.getPiece(this.position).getTeamColor() == ChessGame.TeamColor.WHITE) {
            all_moves = white_pawn_moves();
        }
        else {
            all_moves = black_pawn_moves();
        }
        return all_moves;
    }

    public Boolean can_kill(ChessPosition possible_position) {
        if (valid_position(possible_position)) {
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
        return false;

    }

    public Collection<ChessMove> kill(ChessPosition possible_pos) {
        Collection<ChessMove> all_moves = new ArrayList<>();
        if (valid_position(possible_pos)) {
            if (can_kill(possible_pos)){
                if (possible_pos.getRow() == 1 || possible_pos.getRow() == 8) {
                    all_moves.addAll(do_promotions(possible_pos));
                }
                else {
                    ChessMove move = new ChessMove(this.position, possible_pos, null);
                    all_moves.add(move);
                }
            }
        }
        return all_moves;
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

