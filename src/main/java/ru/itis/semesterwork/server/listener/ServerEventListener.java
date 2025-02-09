package ru.itis.semesterwork.server.listener;

import lombok.Getter;
import ru.itis.semesterwork.exception.WrongDataForThisMessageTypeException;
import ru.itis.semesterwork.common.protocol.Message;

import java.net.ServerSocket;
import java.net.Socket;

@Getter
public abstract class ServerEventListener {
    private ServerSocket serverSocket;
    protected int type;

    public ServerEventListener(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public abstract void handle(Socket socket, Message message) throws WrongDataForThisMessageTypeException;
}
