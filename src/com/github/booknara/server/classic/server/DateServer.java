package com.github.booknara.server.classic.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

/**
 * Created by Daehee Han(@daniel_booknara) on 2/16/16.
 */
public class DateServer {
    public static void main(String[] args) throws IOException {
        // Server side configuration port :9090
        ServerSocket serverSocket = new ServerSocket(9090);

        try {
            while (true) {
                Socket socket = serverSocket.accept();
                try {
                    PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
                    Date today = new Date();
                    writer.println(today.toString());
                    System.out.println(today.toString());
                } finally {
                    socket.close();
                }
            }
        } finally {
            serverSocket.close();
        }
    }
}
