package ru.itis.semesterwork.client.listener;

import ru.itis.semesterwork.client.handler.GameStartHandler;
import ru.itis.semesterwork.common.protocol.Message;

import java.net.Socket;

public class GameStartEventListener extends ClientEventListener {

    private GameStartHandler gameStartHandler;

    public GameStartEventListener(Socket socket, GameStartHandler gameStartHandler) {
        super(socket);
        type = Message.GAME_START;
        this.gameStartHandler = gameStartHandler;
    }

    @Override
    public void handle(Message message) {
        gameStartHandler.onGameStart();
    }
}
