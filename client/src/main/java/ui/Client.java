package ui;

import client.ServerFacade;
import requestsandresults.ListGamesResult;
import requestsandresults.LoginResult;
import requestsandresults.RegisterResult;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Scanner;

public class Client {
    Scanner scanner = new Scanner(System.in);
    ServerFacade sf = new ServerFacade();
    private int signedIn = 0; // 0 if not signed in, 1 if it is signed in

    public void runMenu() throws URISyntaxException, IOException, InterruptedException {
        System.out.println("Lets play some Chess! Sign in to start:");

        //user input
        var result = "";

        while(!result.equals("quit")){
            printPrompt();
            int answer = scanner.nextInt();

            try {
                result = evalFirstLoop(answer);
                if (signedIn == 1){
                    signedInLoop();
                }
                System.out.println(result);
            } catch (Throwable e) {
                System.out.print(e.toString() + "\n");
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
        String username = scanner.next();
        System.out.println("Choose a password: ");
        String password = scanner.next();
        System.out.println("Type email: ");
        String email = scanner.next();

        RegisterResult result = sf.registerServerFacade(username, password, email);
        signedIn = 1;
        return result.toString();
    }

    public String loginClient() throws URISyntaxException, IOException, InterruptedException {
        System.out.println("Insert username: ");
        String username = scanner.next();
        System.out.println("Insert Password: ");
        String password = scanner.next();

        LoginResult result = sf.loginServerFacade(username, password);
        signedIn = 1;
        return result.toString();
    }

    public void signedInLoop() throws URISyntaxException, IOException, InterruptedException {
        var result = " ";
        while (signedIn == 1){
            signedInPrompt();
            int answer = scanner.nextInt();

            try {

            } catch (Throwable e) {
                System.out.print(e.toString());
            }
        }
    }

    public void signedInPrompt(){
        System.out.println("\n" + "You're in");
        System.out.println("1. List Games");
        System.out.println("2. Join game");
        System.out.println("3. Create Game");
        System.out.println("4. Observe Game");
        System.out.println("5. Log out");
        System.out.println("6. Help");
        System.out.println("Type number: ");
    }
//
//    public String evalSecondLoop(int answer){
//        switch (answer) {
//            case 1:
//
//        }
//    }
//
//    public ListGamesResult listGamesClient() {
//
//    }

}
