package ru.itis.semesterwork.client.listener;

import ru.itis.semesterwork.exception.WrongDataForThisMessageTypeException;
import ru.itis.semesterwork.client.handler.OpponentDisconnectHandler;
import ru.itis.semesterwork.common.protocol.Message;

import java.net.Socket;

public class OpponentDisconnectEventListener extends ClientEventListener {

    private OpponentDisconnectHandler opponentDisconnectHandler;

    public OpponentDisconnectEventListener(Socket socket, OpponentDisconnectHandler opponentDisconnectHandler) {
        super(socket);
        this.opponentDisconnectHandler = opponentDisconnectHandler;
        this.type = Message.OPPONENT_DISCONNECT;
    }

    @Override
    public void handle(Message message) throws WrongDataForThisMessageTypeException {
        opponentDisconnectHandler.onOpponentDisconnected();
    }
}
