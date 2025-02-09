package ru.itis.semesterwork.client;

import java.io.IOException;
import java.net.Socket;

public class Client {

    public static Socket getSocket(String host, int port) throws IOException {
        return new Socket(host, port);
    }
}
