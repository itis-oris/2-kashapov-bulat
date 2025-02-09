package ru.itis.semesterwork.client.listener;

import ru.itis.semesterwork.exception.WrongDataForThisMessageTypeException;
import ru.itis.semesterwork.client.handler.GameResultHandler;
import ru.itis.semesterwork.common.protocol.Message;

import java.net.Socket;

public class GameResultEventListener extends ClientEventListener {
    private GameResultHandler gameResultHandler;

    public GameResultEventListener(Socket socket, GameResultHandler gameResultHandler) {
        super(socket);
        this.gameResultHandler = gameResultHandler;
        this.type = Message.GAME_RESULT;
    }

    @Override
    public void handle(Message message) throws WrongDataForThisMessageTypeException {
        gameResultHandler.onGameResult(message);
    }
}
