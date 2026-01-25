package chess.PieceCalculators;

import chess.*;

import java.util.Collection;
import java.util.Collections;

public class PawnMovesCalculator extends PieceMovesCalculator{
    public PawnMovesCalculator(ChessPosition current_position, ChessBoard board) {
        super(current_position, board);
    }

    public Collection<ChessMove> pawn_moves() {
        if (board.getPiece(position).pieceColor == ChessGame.TeamColor.WHITE){
            white_pawn_moves();
        }
        else {
            black_pawn_moves();
        }
        return total_moves;
    }

    public void white_pawn_moves() {
        //kill left
        new_position = new ChessPosition(position.getRow()+1, position.getColumn()-1);
        check_kill();
        //kill right
        new_position = new ChessPosition(position.getRow()+1, position.getColumn()+1);
        check_kill();

        //one up
        new_position = new ChessPosition(position.getRow()+1, position.getColumn());
        if (valid_position(new_position)){
            if (board.getPiece(new_position) == null){
                if (new_position.getRow() == 8) {
                    do_promotions();
                }
                else {
                    new_move = new ChessMove(position, new_position, null);
                    total_moves.add(new_move);
                }
            }
            else {
                return;
            }
        }
        // maybe two up
        if (position.getRow() == 2) {
            new_position = new ChessPosition(position.getRow()+2, position.getColumn());
            if (board.getPiece(new_position) == null){
                new_move = new ChessMove(position, new_position, null);
                total_moves.add(new_move);
            }
        }
    }
    public void black_pawn_moves() {
        //kill left
        new_position = new ChessPosition(position.getRow()-1, position.getColumn()-1);
        check_kill();
        //kill right
        new_position = new ChessPosition(position.getRow()-1, position.getColumn()+1);
        check_kill();

        //one up
        new_position = new ChessPosition(position.getRow()-1, position.getColumn());
        if (valid_position(new_position)){
            if (board.getPiece(new_position) == null){
                if (new_position.getRow() == 1) {
                    do_promotions();
                }
                else {
                    new_move = new ChessMove(position, new_position, null);
                    total_moves.add(new_move);
                }
            }
            else {
                return;
            }
        }
        // maybe two up
        if (position.getRow() == 7) {
            new_position = new ChessPosition(position.getRow()-2, position.getColumn());
            if (board.getPiece(new_position) == null){
                new_move = new ChessMove(position, new_position, null);
                total_moves.add(new_move);
            }
        }

    }

    public void check_kill() {
        if (valid_position(new_position)){
            if (board.getPiece(new_position) != null) {
                if (board.getPiece(new_position).getTeamColor() != board.getPiece(position).getTeamColor()){
                    if (new_position.getRow() == 8 || new_position.getRow() == 1) {
                        do_promotions();
                    }
                    else {
                        new_move = new ChessMove(position, new_position, null);
                        total_moves.add(new_move);
                    }
                }
            }
        }
    }
    public void do_promotions() {
        ChessMove move_to_queen = new ChessMove(position, new_position, ChessPiece.PieceType.QUEEN);
        ChessMove move_to_rook = new ChessMove(position, new_position, ChessPiece.PieceType.ROOK);
        ChessMove move_to_knight = new ChessMove(position, new_position, ChessPiece.PieceType.KNIGHT);
        ChessMove move_to_bishop = new ChessMove(position, new_position, ChessPiece.PieceType.BISHOP);

        Collections.addAll(total_moves, move_to_queen, move_to_bishop,
                move_to_knight, move_to_rook);
    }
}
