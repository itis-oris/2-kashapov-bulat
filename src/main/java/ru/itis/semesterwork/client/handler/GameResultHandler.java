package ru.itis.semesterwork.client.handler;

import ru.itis.semesterwork.exception.WrongDataForThisMessageTypeException;
import ru.itis.semesterwork.common.protocol.Message;

public interface GameResultHandler {
    void onGameResult(Message message) throws WrongDataForThisMessageTypeException;
}
