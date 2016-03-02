package com.github.booknara.server.nio.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.SocketException;

/**
 * Created by Daehee Han(@daniel_booknara) on 2/29/16.
 */
public class NioEchoClient {
    private static final int SERVER_PORT = 9091;
    private static final String SERVER_ADDRESS = "192.168.1.244";

    private Thread listenThread;
    private Thread sendThread;
    private Socket socket;

    public NioEchoClient() throws IOException {
        socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
    }

    public void listen() {
        Runnable listener = new Runnable() {
            public void run() {
                try {
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    while (true) {
                        System.out.println(in.readLine());
                    }
                } catch (SocketException e) {
                    // Called when socket is closed
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        listenThread = new Thread(listener);
        listenThread.start();
    }

    public void send() {
        Runnable sender = new Runnable() {
            public void run() {
                try {
                    PrintStream out = new PrintStream(socket.getOutputStream());
                    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
                    while (true) {
                        String data = in.readLine();
                        if (data.equals("bye"))
                            break;

                        out.println(data);
                    }

                    closeClient();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        sendThread = new Thread(sender);
        sendThread.start();
    }

    private void closeClient() throws IOException {
        socket.close();
        listenThread.interrupt();
    }

    public static void main(String[] args) {
        try {
            NioEchoClient client = new NioEchoClient();
            client.listen();
            client.send();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
