import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

//This is unchanged from the reference client.java

public class clientUDP {
    public static void main(String[] args) {
        //Checking if there are only 2 args (Ip and port)
        if (args.length != 2) {
            System.out.println("Usage: java Client <ip> <port>");
            return;
        }
        //localhost, usually
        String hostname = args[0];
        //Make sure port is an integer
        try {
            //port number
            Integer.parseInt(args[1]);
        }
        catch (NumberFormatException e) {
            System.out.println("Port must be an integer after ip");
            return;
        }

        int port = Integer.parseInt(args[1]);
        //Make sure port is in the right range
        if (port < 0 || port > 65536) {
            System.out.println("Invalid port number! Port number must be between 0 and 65536");
        }
        boolean initMessage = true;
        Scanner scanner = new Scanner(System.in);
        try {
            //This is where we connect to the server
            Socket clientSocket = new Socket(hostname,port);
            while (true) {
                DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
                BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                if (initMessage) {
                    String initialMessage = inFromServer.readLine();
                    System.out.println(initialMessage);
                    initMessage = false;
                }
                System.out.println("Which joke do you want? (Joke 1, Joke 2, Joke 3, or bye)");
                String userInput = "";
                //Make sure the user sends in something that is valid or they will be stuck in a loop
                boolean validInput = false;
                while (!validInput) {
                    userInput = scanner.nextLine();
                    if (userInput.equals("Joke 1")) {
                        outToServer.writeBytes(userInput +'\n'); //Sends the command back
                        validInput = true;
                    }
                    else if (userInput.equals("Joke 2")) {
                        outToServer.writeBytes(userInput + '\n'); //Sends the command back
                        validInput = true;
                    }
                    else if (userInput.equals("Joke 3")) {
                        outToServer.writeBytes(userInput + '\n'); //Sends the command back
                        validInput = true;
                    }
                    else if (userInput.equals("bye")) {
                        System.out.println("Disconnecting");
                        outToServer.writeBytes(userInput + '\n'); //Sends the command back
                        clientSocket.close();
                        System.out.println("Successfully closed socket.");
                        return;
                    }
                    else {
                        System.out.println("Invalid Option!");
                    }
                }
                //Create a new file corresponding to the request
                PrintWriter fileOutput = new PrintWriter(new FileWriter(userInput + ".txt"));
                String line;
                //Keeps trying to get lines from the server until the 'END' is sent.
                while (!(line = inFromServer.readLine()).equals("END")) {
                    fileOutput.println(line);
                }
                fileOutput.close();
                System.out.println("Successfully received: " + userInput + ".txt");
            }
        }
        //This is when the ip does not lead anywhere
        catch (UnknownHostException e) {
            System.out.println("Could not find the host name: " + args[0]);
        }
        //This is when the ip is right, but it can not connect to the right port
        catch (ConnectException e) {
            System.out.println("Could not connect to: " + args[0] + ". On port: " + port + ". The port may not be open.");
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

//Implement UDP-socket version of the source code above
/*
* UDP implementation in Java
* Like TCP, but no established point to point connection between client and server
* UDP support is within java.net package
* Only need to import classes from java.net.DatagramSocket
* and java.net.DatagramPacket
*
*
*
* */

//Collect 10 images and save them to the machine
//Implement a random number generator to recieve the 10 images in random roder
//Report the total round-trip time to retrieve each meme remotely in the client process.
//TCP should also include setup time + IP address resolution in the client process and access time in the server process
//Automate the measurement 10 times randomly in one run, then calculate the statistics like PING