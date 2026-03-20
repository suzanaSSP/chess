package ui;

import client.ServerFacade;
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
                result = eval(answer);
            } catch (Throwable e) {
                System.out.print(e.toString());
            }
            //print menu
            // evaluate user input
            // print result
            // catch errors
        }
    }

    public void printPrompt(){
        System.out.println("1. Register");
        System.out.println("2. Login");
        System.out.println("3. Help");
        System.out.println("4. Quit");
        System.out.println("Type number: ");
    }

    public String eval(int answer) throws URISyntaxException, IOException, InterruptedException {
        switch (answer){
            case 1:
                return registerClient();

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
}
