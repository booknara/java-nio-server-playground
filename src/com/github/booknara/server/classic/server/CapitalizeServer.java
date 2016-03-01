package com.github.booknara.server.classic.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Daehee Han(@daniel_booknara) on 2/17/16.
 */
public class CapitalizeServer {
    public static void main(String[] args) throws IOException {
        // Server configuration port 9091
        int requestNumber = 0, port = 9091;
        ServerSocket serverSocket = new ServerSocket(port);

        try {
            while (true) {
                // The accept method blocks until a connection is made.
                Socket socket = serverSocket.accept();

                // Thread for request handling
                new Capitalizer(socket, requestNumber++).start();
            }
        } finally {
            serverSocket.close();
        }
    }

    private static class Capitalizer extends Thread {
        private Socket socket;
        private int clientNumber;

        public Capitalizer(Socket socket, int clientNumber) {
            this.socket = socket;
            this.clientNumber = clientNumber;
            System.out.println("New connection with client# " + clientNumber + " at " + socket);
        }

        @Override
        public void run() {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

                while (true) {
                    String input = reader.readLine();
                    if (input == null || input.equals("."))
                        break;

                    writer.println(input.toUpperCase());
                    writer.println(input.toUpperCase());
                }

                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
//                closeSocket(socket);
            }
        }

        private void closeSocket(Socket socket) {
            try {
                socket.close();
            } catch (IOException e) {
                System.out.println("Can't close the socket");
            }

            System.out.println("Connection Client # " + clientNumber + " closed");

        }
    }
}
