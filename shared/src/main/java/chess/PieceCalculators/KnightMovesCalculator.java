package chess.PieceCalculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class KnightMovesCalculator extends PieceMovesCalculator{
    public KnightMovesCalculator(ChessPosition current_position, ChessBoard board) {
        super(current_position, board);
    }

    public Collection<ChessMove> knight_moves(){
        List<ChessPosition> possible_positions = new ArrayList<>();
        //2 right 1 up
        new_position = new ChessPosition(position.getRow()+1, position.getColumn()+2);
        possible_positions.add(new_position);
        // 2 right 1 down
        new_position = new ChessPosition(position.getRow()-1, position.getColumn()+2);
        possible_positions.add(new_position);
        //2 down 1 right
        new_position = new ChessPosition(position.getRow()-2, position.getColumn()+1);
        possible_positions.add(new_position);
        //2 down 1 left
        new_position = new ChessPosition(position.getRow()-2, position.getColumn()-1);
        possible_positions.add(new_position);
        //2 left 1 down
        new_position = new ChessPosition(position.getRow()-1, position.getColumn()-2);
        possible_positions.add(new_position);
        //2 left 1 up
        new_position = new ChessPosition(position.getRow()+1, position.getColumn()-2);
        possible_positions.add(new_position);
        //1 left 2 up
        new_position = new ChessPosition(position.getRow()+2, position.getColumn()-1);
        possible_positions.add(new_position);
        //1 right 2 up
        new_position = new ChessPosition(position.getRow()+2, position.getColumn()+1);
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
