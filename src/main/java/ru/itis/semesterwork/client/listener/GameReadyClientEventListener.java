package ru.itis.semesterwork.client.listener;


import ru.itis.semesterwork.exception.WrongDataForThisMessageTypeException;
import ru.itis.semesterwork.client.handler.GameReadyHandler;
import ru.itis.semesterwork.common.protocol.Message;

import java.net.Socket;

public class GameReadyClientEventListener extends ClientEventListener {
    private GameReadyHandler gameReadyHandler;
    public GameReadyClientEventListener(Socket socket, GameReadyHandler gameReadyHandler) {
        super(socket);
        this.gameReadyHandler = gameReadyHandler;
        type = Message.GAME_READY;
    }

    @Override
    public void handle(Message message) throws WrongDataForThisMessageTypeException {
        gameReadyHandler.onGameReady(message);
    }
}
