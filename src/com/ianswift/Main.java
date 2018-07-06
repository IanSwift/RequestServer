package com.ianswift;

import java.io.IOException;

public class Main {
    public static void main(String args[]) throws IOException, InterruptedException {
        int port = args.length > 0 ? Integer.parseInt(args[0]) : 8080;

        RequestServer.listen((a) -> a, port);

        while (true) {
        }
    }
}
