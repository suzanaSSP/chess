package chess.piececalculators;

import chess.*;

import java.util.Collection;
import java.util.Collections;

public class PawnMovesCalculator extends PieceMovesCalculator{
    public PawnMovesCalculator(ChessPosition currentPosition, ChessBoard board) {
        super(currentPosition, board);
    }

    public Collection<ChessMove> pawnMoves() {
        if (board.getPiece(position).pieceColor == ChessGame.TeamColor.WHITE){
            whitePawnMoves();
        }
        else {
            blackPawnMoves();
        }
        return totalMoves;
    }

    public void whitePawnMoves() {
        //kill left
        newPosition = new ChessPosition(position.getRow()+1, position.getColumn()-1);
        checkKill();
        //kill right
        newPosition = new ChessPosition(position.getRow()+1, position.getColumn()+1);
        checkKill();

        //one up
        newPosition = new ChessPosition(position.getRow()+1, position.getColumn());
        if (validPosition(newPosition)){
            if (board.getPiece(newPosition) == null){
                if (newPosition.getRow() == 8) {
                    doPromotions();
                }
                else {
                    newMove = new ChessMove(position, newPosition, null);
                    totalMoves.add(newMove);
                }
            }
            else {
                return;
            }
        }
        // maybe two up
        if (position.getRow() == 2) {
            newPosition = new ChessPosition(position.getRow()+2, position.getColumn());
            if (board.getPiece(newPosition) == null){
                newMove = new ChessMove(position, newPosition, null);
                totalMoves.add(newMove);
            }
        }
    }
    public void blackPawnMoves() {
        //kill left
        newPosition = new ChessPosition(position.getRow()-1, position.getColumn()-1);
        checkKill();
        //kill right
        newPosition = new ChessPosition(position.getRow()-1, position.getColumn()+1);
        checkKill();

        //one up
        newPosition = new ChessPosition(position.getRow()-1, position.getColumn());
        if (validPosition(newPosition)){
            if (board.getPiece(newPosition) == null){
                if (newPosition.getRow() == 1) {
                    doPromotions();
                }
                else {
                    newMove = new ChessMove(position, newPosition, null);
                    totalMoves.add(newMove);
                }
            }
            else {
                return;
            }
        }
        // maybe two up
        if (position.getRow() == 7) {
            newPosition = new ChessPosition(position.getRow()-2, position.getColumn());
            if (board.getPiece(newPosition) == null){
                newMove = new ChessMove(position, newPosition, null);
                totalMoves.add(newMove);
            }
        }

    }

    public void checkKill() {
        if (validPosition(newPosition)){
            if (board.getPiece(newPosition) != null) {
                if (board.getPiece(newPosition).getTeamColor() != board.getPiece(position).getTeamColor()){
                    if (newPosition.getRow() == 8 || newPosition.getRow() == 1) {
                        doPromotions();
                    }
                    else {
                        newMove = new ChessMove(position, newPosition, null);
                        totalMoves.add(newMove);
                    }
                }
            }
        }
    }
    public void doPromotions() {
        ChessMove moveToQueen = new ChessMove(position, newPosition, ChessPiece.PieceType.QUEEN);
        ChessMove moveToRook = new ChessMove(position, newPosition, ChessPiece.PieceType.ROOK);
        ChessMove moveToKnight = new ChessMove(position, newPosition, ChessPiece.PieceType.KNIGHT);
        ChessMove moveToBishop = new ChessMove(position, newPosition, ChessPiece.PieceType.BISHOP);

        Collections.addAll(totalMoves, moveToQueen, moveToBishop,
                moveToKnight, moveToRook);
    }
}
