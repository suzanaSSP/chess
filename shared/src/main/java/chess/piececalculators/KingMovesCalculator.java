package chess.piececalculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class KingMovesCalculator extends PieceMovesCalculator {
    public KingMovesCalculator(ChessPosition currentPosition, ChessBoard board) {
        super(currentPosition, board);
    }

    public Collection<ChessMove> kingMoves() {
        List<ChessPosition> possiblePositions = new ArrayList<>();
        //up
        newPosition = new ChessPosition(position.getRow()+1, position.getColumn());
        possiblePositions.add(newPosition);
        //down
        newPosition = new ChessPosition(position.getRow()-1, position.getColumn());
        possiblePositions.add(newPosition);
        //left
        newPosition = new ChessPosition(position.getRow(), position.getColumn()-1);
        possiblePositions.add(newPosition);
        //right
        newPosition = new ChessPosition(position.getRow(), position.getColumn()+1);
        possiblePositions.add(newPosition);
        //right up
        newPosition = new ChessPosition(position.getRow()+1, position.getColumn()+1);
        possiblePositions.add(newPosition);
        //right down
        newPosition = new ChessPosition(position.getRow()-1, position.getColumn()+1);
        possiblePositions.add(newPosition);
        //left_up
        newPosition = new ChessPosition(position.getRow()+1, position.getColumn()-1);
        possiblePositions.add(newPosition);
        //left_down
        newPosition = new ChessPosition(position.getRow()-1, position.getColumn()-1);
        possiblePositions.add(newPosition);

        //Check if can move
        checkEveryMove(possiblePositions);

        return totalMoves;
    }
}
