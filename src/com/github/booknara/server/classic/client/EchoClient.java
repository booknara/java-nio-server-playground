package com.github.booknara.server.classic.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by Daehee Han(@daniel_booknara) on 2/25/16.
 */
public class EchoClient {
    private static final String HOST_ADDRESS = "localhost";
    private static final int PORT = 9091;
    private static final int BUFFER_SIZE = 1024;

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket(HOST_ADDRESS, PORT);

        OutputStream os = socket.getOutputStream();
        InputStream is = socket.getInputStream();

        byte[] buffer = new byte[BUFFER_SIZE];

        int read;
        while ((read = System.in.read(buffer)) > 0) {
            os.write(buffer, 0, read);
            read = is.read(buffer);
            System.out.write(buffer, 0, read);
        }

        os.close();

        while ((read = is.read(buffer)) > 0)
            System.out.write(buffer, 0, read);

        System.out.close();

        if (socket != null)
            socket.close();
    }
}
