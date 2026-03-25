package ui;

import client.ServerFacade;
import requestsandresults.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Client {
    Scanner scanner = new Scanner(System.in);
    ServerFacade sf = new ServerFacade();
    private int signedIn = 0;// 0 if not signed in, 1 if it is signed in
    private String tokenUsing;
    private String currPlayerColor = "WHITE";

    Map<Integer, AlternativeGameData> gamesInClient = new HashMap<>();

    public void runMenu() {
        System.out.println("Lets play some Chess! Sign in to start:");

        //user input
        var result = "";

        while(!result.equals("quit")){
            if (signedIn == 1){
                signedInLoop();
            }
            printPrompt();

            int answer = Integer.parseInt(scanner.nextLine());

            try {
                result = evalFirstLoop(answer);
                System.out.println(result);
            } catch (Throwable e) {
                System.out.print(e + "\n");
                System.out.println("Try again: " + "\n");

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

    public String evalFirstLoop(int answer) throws URISyntaxException, IOException, InterruptedException {
        switch (answer){
            case 1:
                return registerClient();
            case 2:
                return loginClient();
            case 3:
                return "Register if this is your first time, login if you have already registered";
            case 4:
                return "quit";
            default:
                return "Type valid number";
        }
    }

    public String registerClient() throws URISyntaxException, IOException, InterruptedException {
        System.out.println("Choose a username: ");
        String username = scanner.nextLine();
        System.out.println("Choose a password: ");
        String password = scanner.nextLine();
        System.out.println("Type email: ");
        String email = scanner.nextLine();

        RegisterResult result = sf.registerServerFacade(username, password, email);
        signedIn = 1;
        tokenUsing = result.authToken();
        return result.toString();
    }

    public String loginClient() throws URISyntaxException, IOException, InterruptedException {
        System.out.println("Insert username: ");
        String username = scanner.nextLine();
        System.out.println("Insert Password: ");
        String password = scanner.nextLine();

        LoginResult result = sf.loginServerFacade(username, password);
        tokenUsing = result.authToken();
        signedIn = 1;
        return result.toString();
    }

    public void signedInLoop() {
        while (signedIn == 1){
            signedInPrompt();
            int answer = Integer.parseInt(scanner.nextLine());
            try {
                evalSecondLoop(answer);
            } catch (Throwable e) {
                System.out.print(e);
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
        switch (answer) {
            case 1:
                listGamesClient();
                break;
            case 2:
                joinGameClient();
                break;
            case 3:
                String gameId = createGameClient();
                System.out.println("Here is your game ID: " + gameId);
                break;
            case 4:
//                System.out.println("Which game to observe: ");
//                printGames();
//                int gameAnswer = scanner.nextInt();
                // fetch game from map
                drawChessboard();
                break;
            case 5:
                sf.logoutServerFacade(tokenUsing);
                signedIn = 0;
                System.out.println("logout Successfully");
                break;
            default:
                System.out.println("Type Valid Number");
                break;
        }
    }

    public void listGamesClient() throws URISyntaxException, IOException, InterruptedException {
        ListGamesResult result = sf.listGamesServerFacade(tokenUsing);
        System.out.println(result);
        if (result.games().isEmpty()){
            System.out.println("No games to list, create game");
        } else {
            int i = 1;
            for (AlternativeGameData game : result.games()){
                gamesInClient.put(i, game);
                i++;
            }
            printGames();
        }

    }

    public void printGames() {
        System.out.println("\n");
        for (Map.Entry<Integer, AlternativeGameData> game : gamesInClient.entrySet()){
            System.out.println(game.getKey() + ". " + game.getValue().gameName());
        }
        System.out.println("\n");
    }

    public String createGameClient() throws URISyntaxException, IOException, InterruptedException {
        System.out.println("What name do you want to give your new game: ");
        String answer = scanner.nextLine();
        int gameID = sf.createGameServerFacade(tokenUsing, answer);
        return String.valueOf(gameID);
    }

    public void joinGameClient() throws URISyntaxException, IOException, InterruptedException {
        if (gamesInClient.isEmpty()) {
            System.out.println("No games, create game");
        } else {
            printGames();
            System.out.println("Game you want to join:  ");
            int gameKey = Integer.parseInt(scanner.nextLine());
            System.out.println("Your piece color: ");
            String playercolor = scanner.nextLine();
            currPlayerColor = playercolor;

            AlternativeGameData gameResult = gamesInClient.get(gameKey);

            sf.joinGameServerFacade(playercolor.toUpperCase(), gameResult.gameID(), tokenUsing);

            drawChessboard();
        }

    }

    public void drawChessboard() {
        System.out.println("\n");
        new DrawChessBoard().drawBoard(currPlayerColor, System.out);
    }


}
