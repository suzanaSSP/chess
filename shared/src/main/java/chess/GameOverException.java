package chess;

public class GameOverException extends Error {
    ChessGame.TeamColor loser;
    ChessGame.TeamColor winner;
    public GameOverException(ChessGame.TeamColor loser) {
        this.loser = loser;
        if (loser == ChessGame.TeamColor.WHITE) {
            this.winner = ChessGame.TeamColor.BLACK;
        } else {
            this.winner = ChessGame.TeamColor.WHITE;
        }
    }

    public String getWinner(){return winner.toString();}

}
