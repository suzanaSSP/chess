package chess;

import java.util.*;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    public ChessGame.TeamColor team_playing = TeamColor.WHITE;
    public ChessBoard current_board = new ChessBoard();
    ChessPosition white_king = new ChessPosition(1, 5);
    ChessPosition black_king = new ChessPosition(8, 5);

    public ChessGame() {

    }

    //DEEP COPY
    public ChessGame(ChessGame original) {
        this.team_playing = original.team_playing;
        this.white_king = original.white_king;
        this.black_king = original.black_king;

        //copy of board
        this.current_board = new ChessBoard(original.current_board);
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return team_playing;
    }

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
        // all moves piece can make
        Collection<ChessMove> possible_moves = curr_piece.pieceMoves(current_board, startPosition);
        Collection<ChessMove> approved_moves = new ArrayList<>();
        //clone board
        for (ChessMove move : possible_moves) {
            ChessGame game_copy = new ChessGame(this);
            game_copy.current_board.addPiece(move.endPosition, curr_piece);
            game_copy.current_board.removePiece(move.startPosition);
            if (!game_copy.isInCheck(curr_piece.getTeamColor())) {
                approved_moves.add(move);
            }
        }

        if (approved_moves.isEmpty()) {
            return null;
        } else {
            return approved_moves;
        }
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
        } else {
            ChessPiece piece = current_board.getPiece(move.startPosition);
            current_board.addPiece(move.endPosition, piece);
            current_board.removePiece(move.startPosition);
            if (getTeamTurn() == TeamColor.WHITE) {
                setTeamTurn(TeamColor.BLACK);
            } else {
                setTeamTurn(TeamColor.WHITE);
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
        ChessPosition king_pos = find_king_pos(teamColor);
        return enemy_moves.contains(king_pos); // returning wrong king position in move [3,7]

    }

    public Collection<ChessPosition> all_possible_moves(TeamColor teamColor) {
        //For position
        Collection<ChessPosition> moves = new ArrayList<>();
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
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

    public Collection<ChessPosition> get_end_positions(Collection<ChessMove> moves_to_calc) {
        Collection<ChessPosition> end_positions = new ArrayList<>();
        for (ChessMove move : moves_to_calc) {
            end_positions.add(move.endPosition);
        }
        return end_positions;
    }

    public ChessPosition find_king_pos(TeamColor teamcolor) {
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPiece pos_piece = current_board.getPiece(new ChessPosition(i, j));
                if (pos_piece != null) {
                    if (pos_piece.getPieceType() == ChessPiece.PieceType.KING && pos_piece.getTeamColor() == teamcolor){
                        return new ChessPosition(i, j);
                    }
                }
            }
        }
        return null;
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
