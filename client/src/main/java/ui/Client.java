package ui;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import client.ClientExceptions;
import client.ServerFacade;
import client.WebSocketCommunicator;
import requestsandresults.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Client {
    Scanner scanner = new Scanner(System.in);
    ServerFacade sf = new ServerFacade(8080);
    private int signedIn = 0;// 0 if not signed in, 1 if it is signed in
    private String tokenUsing;
    public String currPlayerColor = "WHITE";
    Map<Integer, AlternativeGameData> gamesInClient = new HashMap<>();
    WebSocketCommunicator wsComunicator = new WebSocketCommunicator();
    private int gameplay = 0;
    private int currGamePLaying = 0;

    String role = "";
    ChessGame currGame;

    public Map<String, Integer> whiteIndexMap =
            Map.of("a", 1,
                    "b", 2,
                    "c", 3,
                    "d", 4,
                    "e", 5,
                    "f", 6,
                    "g", 7,
                    "h", 8);

    private final Map<String, Integer> blackIndexMap =
            Map.of("h", 1,
                    "g", 2,
                    "f", 3,
                    "e", 4,
                    "d", 5,
                    "c", 6,
                    "b", 7,
                    "a", 8);

    public void runMenu() {
        System.out.println("Lets play some Chess! Sign in to start:");

        //user input
        var result = "";

        while (!result.equals("quit")) {
            if (signedIn == 1) {
                signedInLoop();
            }
            printPrompt();
            try {
                int answer = Integer.parseInt(scanner.nextLine());
                result = evalFirstLoop(answer);
                if (answer != 4) {
                    System.out.println(result);
                }

            } catch (ClientExceptions e) {
                System.out.println(e.getMessage());
            } catch (Exception e) {
                System.out.println(" Please type valid response");
            }
        }
    }

    public void printPrompt() {
        System.out.println("1. Register");
        System.out.println("2. Login");
        System.out.println("3. Help");
        System.out.println("4. Quit");
        System.out.println("Type number: ");
    }

    public String evalFirstLoop(int answer) throws URISyntaxException,
            IOException, InterruptedException {
        switch (answer) {
            case 1:
                return registerClient();
            case 2:
                return loginClient();
            case 3:
                return "Register if this is your first time, login if you have already registered, and quit once finished";
            case 4:
                System.out.println("Goodbye");
                return "quit";
            default:
                return "Please type a valid number";
        }
    }

    public String registerClient() throws URISyntaxException,
            IOException, InterruptedException {
        System.out.println("Choose a username: ");
        String username = scanner.nextLine();
        System.out.println("Choose a password: ");
        String password = scanner.nextLine();
        System.out.println("Type email: ");
        String email = scanner.nextLine();

        RegisterResult result = sf.registerServerFacade(username, password, email);
        signedIn = 1;
        tokenUsing = result.authToken();
        return "Register Successfully";


    }

    public String loginClient() throws URISyntaxException,
            IOException, InterruptedException {
        System.out.println("Insert username: ");
        String username = scanner.nextLine();
        System.out.println("Insert Password: ");
        String password = scanner.nextLine();

        LoginResult result = sf.loginServerFacade(username, password);
        tokenUsing = result.authToken();
        signedIn = 1;
        return "Login Successfully";
    }

    public void signedInLoop() {
        while (signedIn == 1) {
            if (gameplay == 1) {
                gamePlayLoop();
            }
            try {
                signedInPrompt();
                int answer = Integer.parseInt(scanner.nextLine());
                evalSecondLoop(answer);
            } catch (ClientExceptions | URISyntaxException | IOException | InterruptedException
            | NumberFormatException e) {
                System.out.println("Please type a valid answer1");
            }
        }
    }

    public void signedInPrompt() {
        System.out.println("\n");
        System.out.println("1. List Games");
        System.out.println("2. Join game");
        System.out.println("3. Create Game");
        System.out.println("4. Observe Game");
        System.out.println("5. Log out");
        System.out.println("6. Help");
        System.out.println("Type number: ");

    }

    public void evalSecondLoop(int answer) throws URISyntaxException, IOException, InterruptedException {
        // create games in client
        setUpGamesClient(answer);
        switch (answer) {
            case 1:
                listGamesClient();
                break;
            case 2:
                joinGameClient();
                gameplay = 1;
                break;
            case 3:
                createGameClient();
                System.out.println("Game created successfully");
                break;
            case 4:
                observeGame();
                break;
            case 5:
                logOutClient();
                break;
            case 6:
                System.out.println("List games to see current running games, " +
                        "Join a Game once you picked a game, create your own game, " +
                        "observe a game other people are playing, and log out once finished");
                break;
            default:
                System.out.println("Type Valid Number");
                break;
        }
    }

    public void logOutClient() {
        try {
            sf.logoutServerFacade(tokenUsing);
            signedIn = 0;
            System.out.println("logout Successfully");
        } catch (ClientExceptions | URISyntaxException | IOException | InterruptedException e) {
            System.out.println("You registered incorrectly, quit now");
        }
    }

    public void observeGame() {
        if (!gamesInClient.isEmpty()) {
            printGames();
            System.out.println("Which game to observe: ");
            try {
                int gameAnswer = Integer.parseInt(scanner.nextLine());
                if (gamesInClient.containsKey(gameAnswer)) {
                    currPlayerColor = "WHITE";
                    role = "observer";
                    wsComunicator.connectSession("localhost", 8080, this);
                    wsComunicator.connectCommand(tokenUsing, gamesInClient.get(gameAnswer).gameID());
                    gameplay = 1;
                } else {
                    System.out.println("Please type valid number");
                }
                // fetch game from map
            } catch (Throwable e) {
                System.out.println("Please type valid answer");
            }
        }
    }

    public void listGamesClient() {
        if (!gamesInClient.isEmpty()) {
            printGames();
        }

        // list games is already called everytime
    }

    public void setUpGamesClient(int answer) throws URISyntaxException, IOException, InterruptedException {
        ListGamesResult result = sf.listGamesServerFacade(tokenUsing);
        if (result.games().isEmpty() && answer != 3 && answer != 5 && answer != 6) {
            System.out.println("No games, create game");
        } else {
            int counter = 1;
            for (AlternativeGameData game : result.games()) {
                if (!gamesInClient.containsValue(game)) {
                    gamesInClient.put(counter, game);
                }
                counter++;
            }
        }
    }

    public void printGames() {
        for (Map.Entry<Integer, AlternativeGameData> game : gamesInClient.entrySet()) {
            // Show players
            String whiteUser = game.getValue().whiteUsername();
            String blackUser = game.getValue().blackUsername();
            if (game.getValue().whiteUsername() == null) {
                whiteUser = "No Player";
            }
            if (game.getValue().blackUsername() == null) {
                blackUser = "No Player";
            }

            System.out.println(game.getKey() + ". " + game.getValue().gameName() +
                    " (White Player: " + whiteUser + "), (Black Player: " + blackUser + ")");
        }

    }

    public String createGameClient() throws URISyntaxException, IOException, InterruptedException {
        System.out.println("What name do you want to give your new game: ");
        String answer = scanner.nextLine();
        int gameID = sf.createGameServerFacade(tokenUsing, answer);
        return String.valueOf(gameID);
    }


    public void joinGameClient() {
        if (!gamesInClient.isEmpty()) {
            printGames();
            System.out.println("Game you want to join:  ");
            try {
                int gameKey = Integer.parseInt(scanner.nextLine());
                System.out.println("Your piece color: ");
                String playercolor = scanner.nextLine();
                currPlayerColor = playercolor.toUpperCase();
                AlternativeGameData gameResult = gamesInClient.get(gameKey);
                if (gameResult == null) {
                    throw new ClientExceptions("Invalid input");
                }
                sf.joinGameServerFacade(currPlayerColor, gameResult.gameID(), tokenUsing);
                role = "player";
                wsComunicator.connectSession("localhost", 8080, this);
                wsComunicator.connectCommand(tokenUsing, gameResult.gameID());
                gameplay = 1;
                currGamePLaying = gameResult.gameID();
                System.out.println(currPlayerColor);

            } catch (ClientExceptions e) {
                System.out.println("I'm in the client exception");
                System.out.println(e.getMessage());
            } catch (Throwable e) {
                System.out.println("Please Type a valid answer");
                this.gameplay = 0;
            }
        }

    }

    public void drawChessboard(ChessGame currGame) {
        System.out.println("\n");
        this.currGame = currGame;
        if (!currGame.wasMoved && currPlayerColor.equals("BLACK")) {
            currGame.currentBoard.flipNewBoard();
        }
        new DrawChessBoard(currGame.currentBoard, currPlayerColor).drawBoard(System.out, null, null);
    }

    public void gamePlayLoop() {
        while (gameplay == 1) {
            try {
                gamePlayPrompt();
                int answer = Integer.parseInt(scanner.nextLine());
                evalThirdLoop(answer);
            } catch (ClientExceptions | IOException e) {
                System.out.println("Please type a valid answer");
            } catch (Exception e) {
                System.out.println("Please type a valid answer");
            }
        }
    }

    public void gamePlayPrompt() {
        System.out.println("\n");
        System.out.println("1. Help");
        System.out.println("2. Redraw ChessBoard");
        System.out.println("3. Leave");
        System.out.println("4. Make Move");
        System.out.println("5. Resign");
        System.out.println("6. Highlight Legal moves");
        System.out.println("Type number: ");
    }

    public void evalThirdLoop(int answer) throws IOException {

        switch (answer) {
            case 1:
                System.out.println("Redraw board to get board again, leave game if you're done, make move when ready, " +
                        "call resign to say you lose, and highlight the valid moves of a piece");
                break;
            case 2:
                drawChessboard(currGame);
                break;
            case 3:
                gameplay = 0;
                currGame = new ChessGame();
                wsComunicator.leaveCommand(tokenUsing, currGamePLaying);
                break;
            case 4:
                makeMoveEval();
                break;
            case 5:
                System.out.println("Are you sure you want to call game over? Type Y or N");
                String confirmation = scanner.nextLine();
                if (confirmation.equals("Y")){
                    wsComunicator.resignCommand(tokenUsing, currGamePLaying);
                }
                break;
            case 6:
                highlightValidMoves();
                break;
            default:
                System.out.println("Type valid answer");
        }
    }

    public void makeMoveEval() {
        try {
            System.out.println("Input current position: (example: e5)");
            String currentPosition = scanner.nextLine();
            char fromColumn = currentPosition.charAt(0);
            int fromRow = Character.getNumericValue(currentPosition.charAt(1));
            System.out.println("Input next move: (example e6)");
            String nextPosition = scanner.nextLine();
            char nextColumn = nextPosition.charAt(0);
            int nextRow = Character.getNumericValue(nextPosition.charAt(1));

            ChessPosition startPos = null;
            ChessPosition nextPos = null;
            if (currPlayerColor.equals("WHITE")) {
                System.out.println(currPlayerColor);
                int column = whiteIndexMap.get(String.valueOf(fromColumn));
                startPos = new ChessPosition(fromRow, column);
                int nextCol = whiteIndexMap.get(String.valueOf(nextColumn));
                nextPos = new ChessPosition(nextRow, nextCol);
            } else {
                startPos = new ChessPosition(fromRow, blackIndexMap.get(String.valueOf(fromColumn)));
                System.out.println(blackIndexMap.get(String.valueOf(fromColumn)));
                nextPos = new ChessPosition(nextRow, blackIndexMap.get(String.valueOf(nextColumn)));
            }

            System.out.println(startPos);
            System.out.println(nextPos);

            ChessMove move;
            // Check promotion pieces
            if (readyForPromotion(startPos, nextPos)) {
                move = doPromotion(startPos, nextPos);
            } else {
                move = new ChessMove(startPos, nextPos, null);
            }
            wsComunicator.makeMoveCommand(tokenUsing, currGamePLaying, move);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Invalid input try again");
        }

    }

    public void highlightValidMoves() {
        System.out.println("What piece do you want to highlight its moves: (example input: e5)");
        String currentPosition = scanner.nextLine();
        char column = currentPosition.charAt(0);
        System.out.println(column);
        int row = Character.getNumericValue(currentPosition.charAt(1));
        System.out.println(blackIndexMap.get(String.valueOf(column)));
        ChessPosition currPosition = null;
        if (currPlayerColor.equals("WHITE")) {
            currPosition = new ChessPosition(row, whiteIndexMap.get(String.valueOf(column)));
        } else {
            currPosition = new ChessPosition(row, blackIndexMap.get(String.valueOf(column)));
        }
        Collection<ChessMove> moves = currGame.validMoves(currPosition);

        new DrawChessBoard(currGame.currentBoard, currPlayerColor).drawBoard(System.out, moves, currPosition);
    }

    public ChessMove doPromotion(ChessPosition startPos, ChessPosition nextPos) {
        System.out.println("You're promoted! Which piece do you want? Type one of these (Q or N)");
        String promotedPiece = scanner.nextLine();

        if (promotedPiece.equals("Q")) {
            return new ChessMove(startPos, nextPos, ChessPiece.PieceType.QUEEN);
        } else {
            return new ChessMove(startPos, nextPos, ChessPiece.PieceType.KNIGHT);
        }
    }

    public boolean readyForPromotion(ChessPosition startPos, ChessPosition nextPos) {
        ChessPiece currPiece = currGame.currentBoard.getPiece(startPos);
        if (currPiece.type.equals(ChessPiece.PieceType.PAWN) &&
                currPlayerColor.equals("WHITE") && nextPos.getRow() == 8) {
            return true;
        } else if (currPiece.type.equals(ChessPiece.PieceType.PAWN) &&
                currPlayerColor.equals("BLACK") && nextPos.getRow() == 1) {
            return true;
        } else {
            return false;
        }
    }

}
