package chess.piececalculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PieceMovesCalculator {
    public ChessPosition position;
    public ChessBoard board;
    public boolean justKilled = false;

    public ChessPosition newPosition = null;
    public ChessMove newMove = null;

    public Collection<ChessMove> totalMoves = new ArrayList<>();

    public PieceMovesCalculator(ChessPosition currentPosition, ChessBoard board) {
        this.position = currentPosition;
        this.board = board;
    }

    public Boolean validPosition(ChessPosition newPos) {
        return newPos.getRow() >= 1 && newPos.getRow() <= 8 &&
                newPos.getColumn() >= 1 && newPos.getColumn() <= 8;
    }

    // Only for king and knight
    public void checkEveryMove(List<ChessPosition> possiblePositions) {
        //Check if can move
        for (int i=0;i<8;i++) {
            if (validPosition(possiblePositions.get(i))){
                if (canMove(possiblePositions.get(i))){
                    newMove = new ChessMove(position, possiblePositions.get(i), null);
                    totalMoves.add(newMove);
                }
            }
        }
    }

    public Boolean canMove(ChessPosition possiblePos) {
        if (board.getPiece(possiblePos) != null) {
            return board.getPiece(possiblePos).pieceColor != board.getPiece(position).pieceColor;
        }
        else{
            return true;
        }
    }

    public void makeMove() {
        if (validPosition(newPosition)) {
            if (board.getPiece(newPosition) == null) {
                newMove = new ChessMove(position, newPosition, null);
            } else {
                if (board.getPiece(newPosition).getTeamColor() != board.getPiece(position).pieceColor) {
                    newMove = new ChessMove(position, newPosition, null);
                    justKilled = true;
                } else {
                    newMove = null;
                }
            }
        } else {
            newMove = null;
        }


    }

    public void movesUp() {
        for (int i = 1; i <= 8; i++) {
            newPosition = new ChessPosition(position.getRow() + i, position.getColumn());
            makeMove();
            if (newMove != null) {
                totalMoves.add(newMove);
                if (justKilled) {
                    justKilled = false;
                    return;
                }
            } else {
                return;
            }
        }
    }

    public void movesDown() {
        for (int i = 1; i <= 8; i++) {
            newPosition = new ChessPosition(position.getRow() - i, position.getColumn());
            makeMove();
            if (newMove != null) {
                totalMoves.add(newMove);
                if (justKilled) {
                    justKilled = false;
                    return;
                }
            } else {
                return;
            }
        }

    }

    public void movesLeft() {
        for (int i = 1; i <= 8; i++) {
            newPosition = new ChessPosition(position.getRow(), position.getColumn() - i);
            makeMove();
            if (newMove != null) {
                totalMoves.add(newMove);
                if (justKilled) {
                    justKilled = false;
                    return;
                }
            } else {
                return;
            }
        }
    }

    public void movesRight() {
        for (int i = 1; i <= 8; i++) {
            newPosition = new ChessPosition(position.getRow(), position.getColumn() + i);
            makeMove();
            if (newMove != null) {
                totalMoves.add(newMove);
                if (justKilled) {
                    justKilled = false;
                    return;
                }
            } else {
                return;
            }
        }

    }

    //moving diagonal
    public void rightUp() {
        for (int i = 1; i <= 8; i++) {
            newPosition = new ChessPosition(position.getRow() + i, position.getColumn() + i);
            makeMove();
            if (newMove != null) {
                totalMoves.add(newMove);
                if (justKilled) {
                    justKilled = false;
                    return;
                }
            } else {
                return;
            }
        }
    }

    public void rightDown() {
        for (int i = 1; i <= 8; i++) {
            newPosition = new ChessPosition(position.getRow() - i, position.getColumn() + i);
            makeMove();
            if (newMove != null) {
                totalMoves.add(newMove);
                if (justKilled) {
                    justKilled = false;
                    return;
                }
            } else {
                return;
            }
        }
    }

    public void leftUp() {
        for (int i = 1; i <= 8; i++) {
            newPosition = new ChessPosition(position.getRow() + i, position.getColumn() - i);
            makeMove();
            if (newMove != null) {
                totalMoves.add(newMove);
                if (justKilled) {
                    justKilled = false;
                    return;
                }
            } else {
                return;
            }
        }
    }

    public void leftDown() {
        for (int i = 1; i <= 8; i++) {
            newPosition = new ChessPosition(position.getRow() - i, position.getColumn() - i);
            makeMove();
            if (newMove != null) {
                totalMoves.add(newMove);
                if (justKilled) {
                    justKilled = false;
                    return;
                }
            } else {
                return;
            }
        }
    }
}
