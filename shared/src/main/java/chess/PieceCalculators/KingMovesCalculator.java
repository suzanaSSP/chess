package chess.PieceCalculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class KingMovesCalculator extends PieceMovesCalculator{
    public KingMovesCalculator(ChessPosition current_position, ChessBoard board) {
        super(current_position, board);
    }

    public Collection<ChessMove> king_moves() {
        List<ChessPosition> possible_positions = new ArrayList<>();
        //up
        new_position = new ChessPosition(position.getRow()+1, position.getColumn());
        possible_positions.add(new_position);
        //down
        new_position = new ChessPosition(position.getRow()-1, position.getColumn());
        possible_positions.add(new_position);
        //left
        new_position = new ChessPosition(position.getRow(), position.getColumn()-1);
        possible_positions.add(new_position);
        //right
        new_position = new ChessPosition(position.getRow(), position.getColumn()+1);
        possible_positions.add(new_position);
        //right up
        new_position = new ChessPosition(position.getRow()+1, position.getColumn()+1);
        possible_positions.add(new_position);
        //right down
        new_position = new ChessPosition(position.getRow()-1, position.getColumn()+1);
        possible_positions.add(new_position);
        //left_up
        new_position = new ChessPosition(position.getRow()+1, position.getColumn()-1);
        possible_positions.add(new_position);
        //left_down
        new_position = new ChessPosition(position.getRow()-1, position.getColumn()-1);
        possible_positions.add(new_position);

        //Check if can move
        for (int i=0;i<8;i++) {
            if (valid_position(possible_positions.get(i))){
                if (can_move(possible_positions.get(i))){
                    new_move = new ChessMove(position, possible_positions.get(i), null);
                    total_moves.add(new_move);
                }
            }
        }
        return total_moves;
    }


}
