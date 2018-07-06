package com.ianswift;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.Function;

public class RequestServer {
    public static void listen(Function callback, int port) throws IOException {
        if (callback == null) {
            throw new NullPointerException("handleRequest cannot be null");
        }

        ServerSocket server = new ServerSocket();
        server.bind(new InetSocketAddress(port));

        new Thread(() -> {
            while(true) {
                try {
                    Socket accept = server.accept();
                    new Thread(() -> {
                        try {
                            handleRequest(accept, callback);
                        } catch (IOException e) {
                        }
                    }).start();
                } catch (Exception e) {
                }
            }
        }).start();
    }

    private static void handleRequest(Socket currentSocket, Function callback) throws IOException {
        byte[] bytes = new byte[10000];
        InputStream inputStream = currentSocket.getInputStream();
        StringBuilder stringBuilder = new StringBuilder();
        while (true) {
            int read = inputStream.read(bytes);
            stringBuilder.append(new String(bytes));
            if (read < 10000) {
                break;
            }
        }
        byte[] response = (byte[]) callback.apply(stringBuilder.toString().getBytes());

        OutputStream outputStream = currentSocket.getOutputStream();
        outputStream.write(("HTTP/1.1 200 OK\r\nContent-Length: "+response.length+"\r\n\r\n"+new String(response)).getBytes());

        currentSocket.close();
    }
}
