package com.github.booknara.server.nio.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

/**
 * Created by Daehee Han(@daniel_booknara) on 2/29/16.
 */
public class NioEchoClient {
    private static final int SERVER_PORT = 9091;
    private static final String SERVER_ADDRESS = "192.168.1.244";

    private Socket socket;

    public NioEchoClient() throws IOException {
        socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
    }

    public void listenFromServer() {
        Runnable listener = new Runnable() {
            public void run() {
                try {
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    while (true) {
                        System.out.println(in.readLine());
                    }
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        };
        new Thread(listener).start();
    }

    public void sendToServer() {
        Runnable sender = new Runnable() {
            public void run() {
                try {
                    PrintStream out = new PrintStream(socket.getOutputStream());
                    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
                    while (true) {
                        out.println(in.readLine());
                    }
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        };
        new Thread(sender).start();
    }

    public static void main(String[] args) {
        try {
            NioEchoClient client = new NioEchoClient();
            client.listenFromServer();
            client.sendToServer();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
