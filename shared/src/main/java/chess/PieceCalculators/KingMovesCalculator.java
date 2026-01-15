package chess.PieceCalculators;
import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class KingMovesCalculator extends PieceMoveCalculator {
    public ChessPosition position;
    public KingMovesCalculator(ChessPosition current_postion) {
        super(current_postion);
        this.position = current_postion;

    }

    public Collection<ChessMove> king_moves (ChessBoard board) {
        Collection<ChessMove> kingmoves = new ArrayList<>();
        //right up
        ChessPosition new_position = new ChessPosition(this.position.getRow()+1, this.position.getColumn()+1);
        ChessMove move = new ChessMove(this.position, new_position, null);
        if (valid_position(board, move)){
            kingmoves.add(move);
        }
        //right down
        ChessPosition new_position2 = new ChessPosition(this.position.getRow()+1, this.position.getColumn()-1);
        ChessMove move2 = new ChessMove(this.position, new_position2, null);
        if (valid_position(board, move2)){
            kingmoves.add(move2);
        }
        //left up
        ChessPosition new_position3 = new ChessPosition(this.position.getRow()-1, this.position.getColumn()+1);
        ChessMove move3 = new ChessMove(this.position, new_position3, null);
        if (valid_position(board, move3)){
            kingmoves.add(move3);
        }
        //left down
        ChessPosition new_position4 = new ChessPosition(this.position.getRow()-1, this.position.getColumn()-1);
        ChessMove move4 = new ChessMove(this.position, new_position4, null);
        if (valid_position(board, move4)){
            kingmoves.add(move4);
        }
        //above
        ChessPosition new_position5 = new ChessPosition(this.position.getRow()+1, this.position.getColumn());
        ChessMove move5 = new ChessMove(this.position, new_position5, null);
        if (valid_position(board, move5)){
            kingmoves.add(move5);
        }
        //below
        ChessPosition new_position6 = new ChessPosition(this.position.getRow()-1, this.position.getColumn());
        ChessMove move6 = new ChessMove(this.position, new_position6, null);
        if (valid_position(board, move6)){
            kingmoves.add(move6);
        }
        //sideways left
        ChessPosition new_position7 = new ChessPosition(this.position.getRow(), this.position.getColumn()-1);
        ChessMove move7 = new ChessMove(this.position, new_position7, null);
        if (valid_position(board, move7)){
            kingmoves.add(move7);
        }
        //sideways right
        ChessPosition new_position8 = new ChessPosition(this.position.getRow(), this.position.getColumn()+1);
        ChessMove move8 = new ChessMove(this.position, new_position8, null);
        if (valid_position(board, move8)){
            kingmoves.add(move8);
        }


        return kingmoves;
    }

    public Boolean valid_position(ChessBoard board, ChessMove move) {
        ChessPosition position = move.getEndPosition();
        if (position.getRow() >= 1 && position.getRow() <= 8 && position.getColumn() >= 1 && position.getColumn() <= 8) {
            ChessPiece own_piece = board.getPiece(this.position);
            if (board.getPiece(position) != null){
                if (board.getPiece(position).getTeamColor() == own_piece.getTeamColor()) {
                    return false;
                }
                else {return true;}
            }
            else {return true;}

        } else {
            return false;
        }
    }
}
