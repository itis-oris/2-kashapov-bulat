package ru.itis.semesterwork.server.listener;


import ru.itis.semesterwork.common.model.Player;
import ru.itis.semesterwork.server.logic.GameRoom;
import ru.itis.semesterwork.common.protocol.Message;
import ru.itis.semesterwork.utils.GameRoomUtil;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;


public class GameReadyServerEventListener extends ServerEventListener {

    private final List<GameRoom> gameRooms;

    public GameReadyServerEventListener(ServerSocket serverSocket, List<GameRoom> gameRooms) {
        super(serverSocket);
        this.gameRooms = gameRooms;
        type = Message.GAME_READY;
    }


    @Override
    public void handle(Socket socket, Message message) {

        GameRoom room = GameRoomUtil.getGameRoom(socket, gameRooms);
        Player player = room.getPlayer1().getSocket().equals(socket) ? room.getPlayer1() : room.getPlayer2();



        room.setPlayerReady(player);
        if (room.playersReady()) {
            room.startGame();
        }
    }
}
