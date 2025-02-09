package ru.itis.semesterwork.exception;

public class WrongDataForThisMessageTypeException extends Exception{
    public WrongDataForThisMessageTypeException() {
        super("Wrong data for this message type");
    }
}
