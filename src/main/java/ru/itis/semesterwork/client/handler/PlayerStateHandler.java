package ru.itis.semesterwork.client.handler;

import ru.itis.semesterwork.exception.WrongDataForThisMessageTypeException;
import ru.itis.semesterwork.common.protocol.Message;

public interface PlayerStateHandler {
    public void handle(Message message) throws WrongDataForThisMessageTypeException;
}
