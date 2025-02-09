package ru.itis.semesterwork.client.listener;

import ru.itis.semesterwork.exception.WrongDataForThisMessageTypeException;
import ru.itis.semesterwork.client.handler.PlayerStateHandler;
import ru.itis.semesterwork.common.protocol.Message;

import java.net.Socket;

public class PlayerStateClientEventListener extends ClientEventListener {
    private PlayerStateHandler playerStateHandler;

    public PlayerStateClientEventListener(Socket socket, PlayerStateHandler playerStateHandler) {
        super(socket);
        this.playerStateHandler = playerStateHandler;
        type = Message.PLAYER_STATE;
    }

    @Override
    public void handle(Message message) throws WrongDataForThisMessageTypeException {
        playerStateHandler.handle(message);
    }
}
