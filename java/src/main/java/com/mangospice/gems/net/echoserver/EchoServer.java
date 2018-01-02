package com.mangospice.gems.net.echoserver;

import java.net.*;
import java.io.*;

public class EchoServer {


    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = null;
        boolean listening = true;

        try {

            serverSocket = new ServerSocket(Integer.parseInt(args[0]));

        } catch (IOException e) {
            System.err.println("Could not listen on port: " + args[0]);
            System.exit(1);
        }

        while (listening) {
            new SocketThread(serverSocket.accept()).start();
        }

        serverSocket.close();
    }
}

class SocketThread extends Thread {
    private Socket socket = null;


    public SocketThread(Socket socket) {
        super("SocketThread");
        this.socket = socket;
    }


    public void run() {

        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            socket.getInputStream()));

            String inputLine;
            String outputLine;

            outputLine = "Echo Server is here!";
            out.println(outputLine);

            while ((inputLine = in.readLine()) != null) {

                outputLine = "'" + inputLine + "'";
                out.println(outputLine);

                if (inputLine.equalsIgnoreCase("bye"))
                    break;
            }
            out.close();
            in.close();
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}