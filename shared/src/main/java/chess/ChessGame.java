package chess;

import java.util.*;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame implements Cloneable{

    public ChessGame.TeamColor team_playing;
    public ChessBoard current_board = new ChessBoard();
    ChessPosition white_king = new ChessPosition(1, 5);
    ChessPosition black_king = new ChessPosition(8, 5);

    public ChessGame() {

    }

    //CLONE
    public ChessGame clone() {
        try{
            ChessGame game_clone = (ChessGame) super.clone();
            ChessBoard clone_board = current_board.clone();
            game_clone.current_board = clone_board;
            game_clone.white_king = white_king;
            game_clone.black_king = black_king;
            game_clone.team_playing = team_playing;

            return game_clone;
        }catch (CloneNotSupportedException e) {
            throw  new RuntimeException(e);
        }
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
    public Collection<ChessMove> validMoves(ChessPosition startPosition)  {
        ChessPiece curr_piece = current_board.getPiece(startPosition);
        // all moves piece can make
        Collection<ChessMove> possible_moves = curr_piece.pieceMoves(current_board, startPosition);

        if (isInCheck(curr_piece.pieceColor)){
            //clone board
            ChessGame game_clone = this.clone();
            for (ChessMove move : possible_moves) {
                game_clone.current_board.addPiece(move.endPosition, curr_piece);
                game_clone.current_board.removePiece(move.startPosition);
                if (game_clone.isInCheck(curr_piece.getTeamColor())) {
                    possible_moves.remove(move);
                }
            }
        }
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
        if (validMoves(move.startPosition) == null || !validMoves(move.startPosition).contains(move)) {
            throw new InvalidMoveException();
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
        Collection<ChessPosition> enemy_moves = all_possible_moves(teamColor);
        if (teamColor == TeamColor.WHITE){
            return enemy_moves.contains(white_king);
        }
        else {
            return enemy_moves.contains(black_king);
        }

    }

    public Collection<ChessPosition> all_possible_moves(TeamColor teamColor){
        Collection<ChessPosition> moves = new ArrayList<>();
        for (int i=1; i<=8;i++) {
            for (int j=1; j<=8;j++) {
                ChessPiece current_piece = current_board.getPiece(new ChessPosition(i, j));
                if (current_piece != null) {
                    if (current_piece.pieceColor != teamColor) {
                        Collection<ChessMove> pieceMoves = current_piece.pieceMoves(current_board, new ChessPosition(i, j));
                        Collection<ChessPosition> end_pos = get_end_positions(pieceMoves);
                        moves.addAll(end_pos);
                    }
                }
            }
        }
        return moves;
    }

    public Collection<ChessPosition> get_end_positions (Collection<ChessMove> moves_to_calc){
        Collection<ChessPosition> end_positions = new ArrayList<>();
        for (ChessMove move : moves_to_calc) {
            end_positions.add(move.endPosition);
        }
        return end_positions;
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return team_playing == chessGame.team_playing && Objects.equals(current_board, chessGame.current_board) && Objects.equals(white_king, chessGame.white_king) && Objects.equals(black_king, chessGame.black_king);
    }

    @Override
    public int hashCode() {
        return Objects.hash(team_playing, current_board, white_king, black_king);
    }
}
