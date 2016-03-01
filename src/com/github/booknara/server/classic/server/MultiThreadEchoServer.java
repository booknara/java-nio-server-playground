package com.github.booknara.server.classic.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Daehee Han(@daniel_booknara) on 2/25/16.
 */
public class MultiThreadEchoServer extends Thread {
    private static final int PORT = 9093;
    private static final int BUFFER_SIZE = 1024;

    private Socket socket;

    public MultiThreadEchoServer(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();

            byte[] buffer = new byte[BUFFER_SIZE];

            int count;
            while ((count = is.read(buffer)) != -1) {
                os.write(buffer, 0, count);
                System.out.write(buffer, 0, count);
            }

            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (socket != null)
                    socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);

        Socket socket = serverSocket.accept();
        new MultiThreadEchoServer(socket).start();
    }
}
