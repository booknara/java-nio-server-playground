package com.github.booknara.server.classic.client;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

/**
 * Created by Daehee Han(@daniel_booknara) on 2/17/16.
 */
public class CapitalizeClient {
    private static final String serverAddress = "localhost";
    private static final int port = 9091;

    public static void main(String[] args) throws IOException {
        for (int i = 0; i < 3000; i++) {
            new Thread(new SendData(i)).start();
        }
    }

    public static class SendData implements Runnable {
        private int count;

        public SendData(int count) {
            this.count = count;
        }

        @Override
        public void run() {
            try {
                // Create and open a socket
                System.out.println("Connection : " + count);
                Socket socket = new Socket(serverAddress, port);

                // Open an input stream and output stream to the socket
                PrintStream writer = new PrintStream(socket.getOutputStream(), true);
                BufferedInputStream inputStream = new BufferedInputStream(socket.getInputStream());

                // Write to the stream according to the server's protocol
                writer.println("lower case letter ");

                byte[] data = new byte[1024];
                while (inputStream.read(data) > -1) {

                }

                System.out.print(data);

                System.out.println("Done");
                // Close the streams
                writer.close();
                inputStream.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
