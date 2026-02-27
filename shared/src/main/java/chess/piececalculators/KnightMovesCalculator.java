package chess.piececalculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class KnightMovesCalculator extends PieceMovesCalculator{
    public KnightMovesCalculator(ChessPosition currentPosition, ChessBoard board) {
        super(currentPosition, board);
    }

    public Collection<ChessMove> knightMoves(){
        List<ChessPosition> possiblePositions = new ArrayList<>();
        //2 right 1 up
        newPosition = new ChessPosition(position.getRow()+1, position.getColumn()+2);
        possiblePositions.add(newPosition);
        // 2 right 1 down
        newPosition = new ChessPosition(position.getRow()-1, position.getColumn()+2);
        possiblePositions.add(newPosition);
        //2 down 1 right
        newPosition = new ChessPosition(position.getRow()-2, position.getColumn()+1);
        possiblePositions.add(newPosition);
        //2 down 1 left
        newPosition = new ChessPosition(position.getRow()-2, position.getColumn()-1);
        possiblePositions.add(newPosition);
        //2 left 1 down
        newPosition = new ChessPosition(position.getRow()-1, position.getColumn()-2);
        possiblePositions.add(newPosition);
        //2 left 1 up
        newPosition = new ChessPosition(position.getRow()+1, position.getColumn()-2);
        possiblePositions.add(newPosition);
        //1 left 2 up
        newPosition = new ChessPosition(position.getRow()+2, position.getColumn()-1);
        possiblePositions.add(newPosition);
        //1 right 2 up
        newPosition = new ChessPosition(position.getRow()+2, position.getColumn()+1);
        possiblePositions.add(newPosition);

        //Check if can move
        checkEveryMove(possiblePositions);
        return totalMoves;


    }
}
