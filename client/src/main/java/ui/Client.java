package ui;

import chess.ChessBoard;
import chess.ChessGame;
import client.ClientExceptions;
import client.ServerFacade;
import client.WebSocketCommunicator;
import requestsandresults.*;
import java.io.IOException;
import java.net.URISyntaxException;
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

    public void runMenu() throws URISyntaxException, IOException, InterruptedException {
        System.out.println("Lets play some Chess! Sign in to start:");

        //user input
        var result = "";

        while(!result.equals("quit")){
            if (signedIn == 1){
                signedInLoop();
            }
            printPrompt();
            try {
                int answer = Integer.parseInt(scanner.nextLine());
                result = evalFirstLoop(answer);
                if (answer == 3 || answer > 4 || answer < 4) {
                    System.out.println(result);
                }

            } catch (ClientExceptions e) {
                System.out.println(e.getMessage());
            } catch (Exception e) {
                System.out.println(" Please type valid response");
            }
        }
    }

    public void printPrompt(){
        System.out.println("1. Register");
        System.out.println("2. Login");
        System.out.println("3. Help");
        System.out.println("4. Quit");
        System.out.println("Type number: ");
    }

    public String evalFirstLoop(int answer) throws URISyntaxException,
            IOException, InterruptedException {
        switch (answer){
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
        while (signedIn == 1){
            if (gameplay == 1) {
                gamePlayLoop();
            }
            signedInPrompt();
            int answer = Integer.parseInt(scanner.nextLine());
            try {
                evalSecondLoop(answer);
            } catch (ClientExceptions | URISyntaxException | IOException | InterruptedException e) {
                System.out.println("Please type a valid answer");
            }
        }
    }

    public void signedInPrompt(){
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
                    drawChessboard(new ChessBoard());
                } else {
                    System.out.println("Please type valid number");
                }
                // fetch game from map
            } catch (Throwable e) {
                System.out.println("Please type valid answer");
            }
        }
    }

    public void listGamesClient() throws URISyntaxException, IOException, InterruptedException {
        if (!gamesInClient.isEmpty()) {
            printGames();
        }

        // list games is already called everytime
    }

    public void setUpGamesClient(int answer) throws URISyntaxException, IOException, InterruptedException {
        ListGamesResult result = sf.listGamesServerFacade(tokenUsing);
        if (result.games().isEmpty() && answer != 3 && answer != 5 && answer !=6){
            System.out.println("No games, create game");
        } else {
            int counter = 1;
            for (AlternativeGameData game : result.games()){
                if (!gamesInClient.containsValue(game)) {
                    gamesInClient.put(counter, game);
                }
                counter++;
            }
        }
    }

    public void printGames() {
        for (Map.Entry<Integer, AlternativeGameData> game : gamesInClient.entrySet()){
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


    public void joinGameClient() throws URISyntaxException, IOException, InterruptedException {
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
                wsComunicator.connectSession("localhost", 8080, currPlayerColor);
                wsComunicator.connectCommand( tokenUsing, gameResult.gameID());
                gameplay = 1;
                currGamePLaying = gameResult.gameID();

            } catch (ClientExceptions e) {
                System.out.println("I'm in the client exception");
                System.out.println(e.getMessage());
            } catch (Throwable e) {
                e.printStackTrace();
                System.out.println("Please Type a valid answer");
            }
        }

    }

    public void drawChessboard(ChessBoard board) {
        System.out.println("\n");
        new DrawChessBoard(board, currPlayerColor).drawBoard(System.out);
    }

    public void gamePlayLoop() {
        while (gameplay == 1) {
            gamePlayPrompt();
            int answer = Integer.parseInt(scanner.nextLine());
            try {
                evalThirdLoop(answer);
            } catch (ClientExceptions | IOException e) {
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
        switch (answer){
            case 2:
                wsComunicator.redrawSession(tokenUsing, currGamePLaying, currPlayerColor);
                break;
            case 3:
                gameplay = 0;
                wsComunicator.leaveCommand(tokenUsing, currGamePLaying);
                break;
        }
    }

}
