package chess;

import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    public ChessGame.TeamColor team_playing;
    public ChessBoard current_board = new ChessBoard();
    ChessPosition white_king = new ChessPosition(0, 4);
    ChessPosition black_king = new ChessPosition(7, 4);

    public ChessGame() {

    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn(){return team_playing;}

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        team_playing = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece curr_piece = current_board.getPiece(startPosition);
        Collection<ChessMove> possible_moves = curr_piece.pieceMoves(current_board, startPosition);
        if (possible_moves.isEmpty()){
            return null;
        }
        else {return possible_moves;}
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        if (validMoves(move.startPosition) == null){
            //throw sometime of error idk
        }
        else {
            ChessPiece piece = current_board.getPiece(move.startPosition);
            current_board.addPiece(move.endPosition, piece);
            current_board.removePiece(move.startPosition);
            if (piece.type == ChessPiece.PieceType.KING) {
                if (piece.pieceColor == TeamColor.WHITE) {
                    white_king = move.endPosition;
                }
                else {
                    black_king = move.endPosition;
                }
            }

        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        //Up, down, right, left, diagonal right up, diagonal left up, diagonal right down, diagonal left down
        int[][] directions = {{1,0}, {-1,0},{0,1}, {0,-1}, {1,1},{1,-1}, {-1, 1}, {-1,-1}};

        if (teamColor == TeamColor.WHITE) {
            for (int[] d : directions) {
                int r = white_king.getRow() + d[0];
                int c = white_king.getColumn() + d[1];

                while (isInsideBoard(r, c)) {
                    ChessPosition possible_enemy = new ChessPosition(r, c);
                    if (current_board.getPiece(possible_enemy) != null) {
                        ChessPiece maybe_enemy = current_board.getPiece(possible_enemy);
                        if (maybe_enemy.pieceColor != TeamColor.WHITE && (maybe_enemy.type == ChessPiece.PieceType.ROOK
                                || maybe_enemy.type == ChessPiece.PieceType.QUEEN
                        || maybe_enemy.type == ChessPiece.PieceType.BISHOP)){
                            return true;
                        }
                    }
                    r += d[0];
                    c += d[1];
                }
            }
        }
        return false;

    }

    public boolean isInsideBoard(int row, int col) {
        return row >=1 && row <=8 && col >=1 &&  col <=8;
    }
    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        current_board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return current_board;
    }
}
