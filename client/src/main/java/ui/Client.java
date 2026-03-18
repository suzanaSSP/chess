package ui;

import client.ServerFacade;
import requestsandresults.RegisterResult;

import java.util.Scanner;

public class Client {
    Scanner scanner = new Scanner(System.in);
    ServerFacade sf = new ServerFacade();
    private int signedIn = 0; // 0 if not signed in, 1 if it is signed in

    public void runMenu(){
        System.out.println("Lets play some Chess! Sign in to start:");

        //user input
        var result = "";

        while(!result.equals("quit")){
            printPrompt();
            int answer = scanner.nextInt();

            try {
                eval(answer);
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

    public void eval(int answer){
        switch (answer){
            case 1 -> registerClient();

        }
    }

    public void registerClient(){
        System.out.println("Please provide information as follows: <your new username>, <your new password>, <your email>");
        String line = scanner.nextLine();
        var answers = line.split(", ");
        RegisterResult result = sf.registerServerFacade(answers[0],  answers[1], answers[2]);
        System.out.println("You're succesfully registered!");
        signedIn = 1;
    }
}
