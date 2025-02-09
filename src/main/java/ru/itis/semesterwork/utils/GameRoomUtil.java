package ru.itis.semesterwork.utils;

import ru.itis.semesterwork.server.logic.GameRoom;

import java.net.Socket;
import java.util.List;

public class GameRoomUtil {
    public static GameRoom getGameRoom(Socket socket, List<GameRoom> gameRooms) {
        for (GameRoom room : gameRooms) {
            if (room.getPlayer1().getSocket().equals(socket) ||
            room.getPlayer2().getSocket().equals(socket)) {
                return room;
            }
        }
        return null;
    }
}
