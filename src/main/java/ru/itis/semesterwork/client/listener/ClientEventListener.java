package ru.itis.semesterwork.client.listener;

import lombok.Getter;
import ru.itis.semesterwork.exception.WrongDataForThisMessageTypeException;
import ru.itis.semesterwork.common.protocol.Message;

import java.net.Socket;

@Getter
public abstract class ClientEventListener {
    private Socket socket;
    protected int type;

    public ClientEventListener(Socket socket) {
        this.socket = socket;
    }

    public abstract void handle(Message message) throws WrongDataForThisMessageTypeException;
}
