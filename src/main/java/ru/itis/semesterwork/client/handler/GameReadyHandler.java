package ru.itis.semesterwork.client.handler;

import ru.itis.semesterwork.exception.WrongDataForThisMessageTypeException;
import ru.itis.semesterwork.common.protocol.Message;

public interface GameReadyHandler {
    void onGameReady(Message message) throws WrongDataForThisMessageTypeException;
}
