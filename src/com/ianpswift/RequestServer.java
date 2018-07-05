package com.ianpswift;

import com.ianpswift.utilities.ThreadPool;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.Function;

public class RequestServer {
    public static ThreadPool threadPool;

    public static void listen(Function callback, int port) throws IOException, InterruptedException {
        if (callback == null) {
            throw new NullPointerException("handleRequest cannot be null");
        }

        threadPool = new ThreadPool();

        ServerSocket server = new ServerSocket();
        server.bind(new InetSocketAddress(port));

        while(true) {
            Socket accept = server.accept();

            threadPool.getThread(()-> {
                try {
                    handleRequest(accept, callback);
                } catch(IOException e) {
                }
            }).run();
        }
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
        OutputStream outputStream = currentSocket.getOutputStream();
        outputStream.write(("HTTP/1.1 200 OK\r\nContent-Length: "+stringBuilder.toString().length() +"\r\n\r\n"+stringBuilder.toString()).getBytes());

        currentSocket.close();
    }
}
