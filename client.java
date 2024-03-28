//Reference from PA1_Team18: credit Timmy Du, Jacob Behr and Benjamin Price

//Changes have been made to make this program parse "MemeX.jpg" instead of "JokeX.txt" but imageIO has not been implemented yet
//Compiles but does not properly give openable images
import java.io.*;

import java.net.*;
import java.util.Scanner;
public class client {
    public static void main(String[] args) {
        //Checking if there are only 2 args (Ip and port)
        if (args.length != 2) {
            System.out.println("Usage: java Client <ip> <port>");
            return;
        }
        String hostname = args[0];
        //Make sure port is an integer
        try {
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
                System.out.println("Which meme do you want? (Input Meme 1-10, or bye)");
                String userInput = "";
                //Make sure the user sends in something that is valid or they will be stuck in a loop
                boolean validInput = false;
                while (!validInput) {
                    userInput = scanner.nextLine();
                    switch(userInput) {
                        case "Meme 1", "Meme 2", "Meme 3", "Meme 4", "Meme 5", "Meme 6", "Meme 7", "Meme 8", "Meme 9", "Meme 10" -> {
                            outToServer.writeBytes(userInput + '\n'); //Sends the command back
                            validInput = true;
                        }
                        case "bye" -> {
                            System.out.println("Disconnecting");
                            outToServer.writeBytes(userInput + '\n'); //Sends the command back
                            clientSocket.close();
                            System.out.println("Successfully closed socket.");
                            return;
                        }
                        default -> System.out.println("Invalid Option!");
                    };
                }
                //Create a new file corresponding to the request
                PrintWriter fileOutput = new PrintWriter(new FileWriter(userInput + ".jpg"));
                String line;
                //Keeps trying to get lines from the server until the 'END' is sent.
                while (!(line = inFromServer.readLine()).equals("END")) {
                    fileOutput.println(line);
                }
                fileOutput.close();
                System.out.println("Successfully received: " + userInput + ".jpg");
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