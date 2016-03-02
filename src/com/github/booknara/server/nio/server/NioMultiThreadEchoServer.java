package com.github.booknara.server.nio.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by Daehee Han(@daniel_booknara) on 2/29/16.
 */
public class NioMultiThreadEchoServer implements Runnable {
    private static final int PORT = 9091;
    private static final int BUFFER_SIZE = 1024;

    private Set<SocketChannel> clients = new HashSet<>();
    private Selector selector;

    public NioMultiThreadEchoServer() throws IOException {
        selector = Selector.open();
    }

    @Override
    public void run() {
        try {
            openServerSocket();

            while(true) {
                int numKeys = selector.select();
                if (numKeys <= 0 )
                    continue;

                Set<SelectionKey> readyClients = new HashSet<>();

                Set<SelectionKey> selectedKeys = selector.selectedKeys();

                Iterator<SelectionKey> i = selectedKeys.iterator();
                while (i.hasNext()) {
                    SelectionKey key = i.next();
                    i.remove();

                    if (key.isAcceptable()) {
                        handleAccept(key);
                    }

                    if (key.isConnectable()) {
                        handleConnect(key);
                    }

                    if (key.isReadable()) {
                        readyClients.add(key);
                    }

                }

                if (!readyClients.isEmpty())
                    handleRead(readyClients);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void openServerSocket() throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        ServerSocket socket = serverSocketChannel.socket();
        socket.bind(new InetSocketAddress(PORT));

        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        System.out.println("ServerSocket is registered to selector");
    }

    // Handling ServerSocketChannel (SelectionKey.OP_ACCEPT)
    private void handleAccept(SelectionKey key) {
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();

        try {
            SocketChannel channel = serverSocketChannel.accept();
            if (channel == null) {
                System.out.println("null socket channel");
                return;
            }

            int socketOps = SelectionKey.OP_CONNECT | SelectionKey.OP_READ;

            channel.configureBlocking(false);
            channel.register(selector, socketOps);
            clients.add(channel);
            System.out.println("Socket accepted : " + channel);

            ByteBuffer welComeMessage = ByteBuffer.wrap("Welcome to chatroom!\n".getBytes());
            try {
                channel.write(welComeMessage);
            } catch (IOException e) {
                closeConnection(channel);
                e.printStackTrace();
            }

        } catch (ClosedChannelException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }  catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleConnect(SelectionKey key) {
        SocketChannel channel = (SocketChannel) key.channel();

        try {
            System.out.println("Client is connectable");
            if (channel.isConnectionPending()) {
                System.out.println("about to finish the connection");
                channel.finishConnect();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleRead(Set<SelectionKey> readyClients) {
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);

        Iterator<SelectionKey> rc = readyClients.iterator();
        while (rc.hasNext()) {
            SocketChannel channel = (SocketChannel) (rc.next()).channel();

            try {
                channel.read(buffer);
            } catch (IOException e) {
                closeConnection(channel);
                e.printStackTrace();
            }

            buffer.flip();

            Iterator<SocketChannel> c = clients.iterator();
            while (c.hasNext()) {
                SocketChannel outChannel = c.next();

                try {
                    outChannel.write(buffer);
                } catch (IOException e) {
                    closeConnection(channel);
                    e.printStackTrace();
                }

                buffer.rewind();
            }

            buffer.clear();
        }
    }

    private void closeConnection(SocketChannel channel) {
        System.out.println("Close Connection");
        try {
            clients.remove(channel);
            channel.keyFor(selector).cancel();
            channel.socket().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        new Thread(new NioMultiThreadEchoServer()).start();
    }
}
