//Reference from PA1_Team18: credit Timmy Du, Jacob Behr and Benjamin Price


//Changes have been made to make this program parse "MemeX.jpg" instead of "JokeX.txt" but imageIO has not been implemented yet
//Compiles but does not properly give openable images
import java.net.ServerSocket;
import java.io.*;
import java.net.*;
public class server {
    public static void main(String[] args) {
        //First Check if there is just one argument which should be a port.
        if (args.length != 1) {
            System.out.println("Usage: java Server <port>");
            return;
        }
        //Going to try to parse arg as an integer for a port number
        try {
            Integer.parseInt(args[0]);
        }
        catch(NumberFormatException e) {
            System.out.println("Port must be after server");
            return;
        }
        int port = Integer.parseInt(args[0]);
        //Check if the port number is within the right range
        if (port > 65536 || port < 0) {
            System.out.println("Invalid port number! Port number must be between 0 to 65536");
            return;
        }
        try {
            //Open the socket
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Port opened at port: " + port);
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected.");

            //This is just for the initial send message
            boolean initMessage = true;

            while(true){
                String clientSentence = "";
                String stringToSend;
                BufferedReader inFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                DataOutputStream outToClient = new DataOutputStream(clientSocket.getOutputStream());
                String fileToSend = "";

                if (initMessage) {
                    stringToSend = "Received from server: Hello!";
                    outToClient.writeBytes(stringToSend + '\n');
                    initMessage = false;
                }

                //This is where we make sure the client user is sending in valid requests
                boolean jokeInput = false;
                while (!jokeInput) {
                    clientSentence = inFromClient.readLine();
                    System.out.println("Command received: " + clientSentence);
                    switch(clientSentence) {
                        case "Meme 1", "Meme 2", "Meme 3", "Meme 4", "Meme 5", "Meme 6", "Meme 7", "Meme 8", "Meme 9", "Meme 10" -> {
                            System.out.println("Request for " + clientSentence + " received.");
                            jokeInput = true;
                            fileToSend = "Memes/" + clientSentence.replaceAll("\\s", "") + ".jpg";
                        }
                        case "bye" -> {
                            clientSocket.close();
                            serverSocket.close();
                            System.out.println("Successfully closed sockets.");
                            return;
                        }
                        default -> {
                            stringToSend = "Invalid meme choice!"; //This else statement isn't needed anymore
                            outToClient.writeBytes(stringToSend);
                        }
                    };
                }
                //Now we read the file and then print it to the clients output stream
                BufferedReader fileReader = new BufferedReader(new FileReader(fileToSend));
                PrintWriter printToClient = new PrintWriter(clientSocket.getOutputStream(), true);
                String line;
                //Keep looping through the file being read (Even though in our case its just one line) where the output stream is constantly getting more lines
                while ((line = fileReader.readLine()) != null) {
                    printToClient.println(line);
                }
                printToClient.println("END"); //This is how we tell the client to stop trying to get more lines.
                fileReader.close();
                System.out.println("Successfully sent the meme");
            }
        }
        catch (BindException e) {
            System.out.println("Port is already in use");
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}