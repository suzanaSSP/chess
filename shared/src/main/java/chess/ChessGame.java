package chess;

import java.util.*;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    public ChessGame.TeamColor teamPlaying = TeamColor.WHITE;
    public ChessBoard currentBoard = new ChessBoard();

    ChessPosition whiteKing = new ChessPosition(1, 5);
    ChessPosition blackKing = new ChessPosition(8, 5);

    public ChessGame() {
        currentBoard.addStartPieces();
    }

    //DEEP COPY
    public ChessGame(ChessGame original) {
        this.teamPlaying = original.teamPlaying;
        this.whiteKing = original.whiteKing;
        this.blackKing = original.blackKing;

        //copy of board
        this.currentBoard = new ChessBoard(original.currentBoard);
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamPlaying;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        teamPlaying = team;
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
        ChessPiece currPiece = currentBoard.getPiece(startPosition);
        if (currPiece == null) {
            return null;
        }
        // all moves piece can make
        Collection<ChessMove> possibleMoves = currPiece.pieceMoves(currentBoard, startPosition);
        Collection<ChessMove> approvedMoves = new ArrayList<>();

        //clone board
        for (ChessMove move : possibleMoves) {
            ChessGame gameCopy = new ChessGame(this);
            gameCopy.currentBoard.addPiece(move.endPosition, currPiece);
            gameCopy.currentBoard.removePiece(move.startPosition);
            if (!gameCopy.isInCheck(currPiece.getTeamColor())) {
                approvedMoves.add(move);
            }
        }

        return approvedMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        if (validMoves(move.startPosition) == null || !validMoves(move.startPosition).contains(move) ||
        validMoves(move.startPosition).isEmpty()|| getTeamTurn() != currentBoard.getPiece(move.startPosition).getTeamColor()) {
            throw new InvalidMoveException();
        } else {
            ChessPiece piece = currentBoard.getPiece(move.startPosition);
            currentBoard.addPiece(move.endPosition, piece);
            currentBoard.removePiece(move.startPosition);
            if (move.promotionPiece != null) {
                piece.type = move.promotionPiece;
            }
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
        Collection<ChessPosition> enemyMoves = allPossibleEnemyMoves(teamColor);
        ChessPosition kingPos = findKingPos(teamColor);
        return enemyMoves.contains(kingPos); // returning wrong king position in move [3,7]

    }

    public Collection<ChessPosition> allPossibleEnemyMoves(TeamColor teamColor) {
        //For position
        Collection<ChessPosition> moves = new ArrayList<>();
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPiece currentPiece = currentBoard.getPiece(new ChessPosition(i, j));
                if (currentPiece != null) {
                    if (currentPiece.pieceColor != teamColor) {
                        Collection<ChessMove> pieceMoves = currentPiece.pieceMoves(currentBoard, new ChessPosition(i, j));
                        Collection<ChessPosition> endPos = getEndPositions(pieceMoves);
                        moves.addAll(endPos);
                    }
                }
            }
        }
        return moves;
    }

    public Collection<ChessPosition> getEndPositions(Collection<ChessMove> movesToCalc) {
        Collection<ChessPosition> endPositions = new ArrayList<>();
        for (ChessMove move : movesToCalc) {
            endPositions.add(move.endPosition);
        }
        return endPositions;
    }

    public ChessPosition findKingPos(TeamColor teamcolor) {
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPiece posPiece = currentBoard.getPiece(new ChessPosition(i, j));
                if (posPiece != null) {
                    if (posPiece.getPieceType() == ChessPiece.PieceType.KING && posPiece.getTeamColor() == teamcolor){
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
        /**Check for checkmate:
         * King can't move
         * Pieces can't kill
         * Pieces can't go in front
         */

        if (!isInCheck(teamColor)){
            return false;
        }
        return checkInCloneBoard(teamColor);
    }

    public Collection<ChessMove> ownPiecesMoves(TeamColor teamColor){
        Collection<ChessMove> moves = new ArrayList<>();
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPiece currentPiece = currentBoard.getPiece(new ChessPosition(i, j));
                if (currentPiece != null) {
                    if (currentPiece.pieceColor == teamColor) {
                        Collection<ChessMove> pieceMoves = currentPiece.pieceMoves(currentBoard, new ChessPosition(i, j));
                        moves.addAll(pieceMoves);
                    }
                }
            }
        }
        return moves;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if (isInCheck(teamColor)){
            return false;
        }

        return checkInCloneBoard(teamColor);
    }

    public Boolean checkInCloneBoard(TeamColor teamColor){
        Collection<ChessMove> myTeamMoves = ownPiecesMoves(teamColor);
        for (ChessMove move : myTeamMoves){
            ChessPiece currPiece = currentBoard.getPiece(move.startPosition);
            ChessGame gameCopy = new ChessGame(this);
            gameCopy.currentBoard.addPiece(move.endPosition, currPiece);
            gameCopy.currentBoard.removePiece(move.startPosition);
            if (!gameCopy.isInCheck(currPiece.getTeamColor())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        currentBoard = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return currentBoard;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return teamPlaying == chessGame.teamPlaying &&
                Objects.equals(currentBoard, chessGame.currentBoard) &&
                Objects.equals(whiteKing, chessGame.whiteKing) &&
                Objects.equals(blackKing, chessGame.blackKing);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamPlaying, currentBoard, whiteKing, blackKing);
    }
}
