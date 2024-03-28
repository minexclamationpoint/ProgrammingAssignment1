import java.io.*;
import java.net.*;

//Kind of mangled version of server.java with some slight editing to add UDP functions. Does not compile.

public class serverUDP {
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
            DatagramSocket serverSocket = new DatagramSocket(port);
            System.out.println("Port opened at port: " + port);

            //Socket clientSocket = serverSocket.accept();
            byte[] buffer = args[0].getBytes();
            DatagramPacket clientPacket = new DatagramPacket(buffer, buffer.length);
            serverSocket.receive(clientPacket);
            int clientPort = clientPacket.getPort();
            System.out.println("Client connected at port " + clientPort);

            //This is just for the initial send message
            boolean initMessage = true;

            while(true){
                InetAddress address = clientPacket.getAddress();
                String clientSentence = "";
                String stringToSend;
                String recieved = new String(clientPacket.getData(), 0, clientPacket.getLength());
                BufferedReader inFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                DataOutputStream outToClient = new DataOutputStream(clientSocket.getOutputStream());
                String fileToSend = "";

                if (initMessage) {
                    stringToSend = "Received from server: Hello!";
                    //serverSocket.send(clientPacket);
                    outToClient.writeBytes(stringToSend + '\n');
                    initMessage = false;
                }

                //This is where we make sure the client user is sending in valid requests
                boolean jokeInput = false;
                while (!jokeInput) {
                    clientSentence = inFromClient.readLine();
                    System.out.println("Command received: " + clientSentence);
                    if (clientSentence.equals("Joke 1")) {
                        System.out.println("Request for Joke 1 received.");
                        jokeInput = true;
                        fileToSend = "Jokes/Joke1.txt";
                    }
                    else if (clientSentence.equals("Joke 2")) {
                        System.out.println("Request for Joke 2 received.");
                        jokeInput = true;
                        fileToSend = "Jokes/Joke2.txt";
                    }
                    else if (clientSentence.equals("Joke 3")) {
                        System.out.println("Request for Joke 3 received.");
                        fileToSend = "Jokes/Joke3.txt";
                        jokeInput = true;
                    }
                    else if (clientSentence.equals("bye")) {
                        clientSocket.close();
                        serverSocket.close();
                        System.out.println("Successfully closed sockets.");
                        return;
                    }
                    else {
                        stringToSend = "Invalid joke choice!"; //This else statement isn't needed anymore
                        outToClient.writeBytes(stringToSend);
                    }
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
                System.out.println("Successfully sent the joke");
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