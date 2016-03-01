package com.github.booknara.server.classic.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by Daehee Han(@daniel_booknara) on 2/17/16.
 */
public class DateClient {
    public static void main(String[] args) throws IOException {
        // Client side configuration private IP address : ? , Port :9090
        String serverAddress = "192.168.1.194";
        int port = 9090;

        for(int i = 0; i < 10000; i++ ){
            Socket socket = new Socket(serverAddress, port);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response = reader.readLine();

            System.out.println(i + " : " + response);
        }

    }
}
