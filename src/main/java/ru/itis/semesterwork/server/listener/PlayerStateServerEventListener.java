package ru.itis.semesterwork.server.listener;

import ru.itis.semesterwork.common.model.Player;
import ru.itis.semesterwork.exception.WrongDataForThisMessageTypeException;
import ru.itis.semesterwork.server.logic.GameRoom;
import ru.itis.semesterwork.common.protocol.Message;
import ru.itis.semesterwork.utils.GameRoomUtil;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PlayerStateServerEventListener extends ServerEventListener {

    private final List<GameRoom> gameRooms;

    public PlayerStateServerEventListener(ServerSocket serverSocket, List<GameRoom> gameRooms) {
        super(serverSocket);
        this.gameRooms = gameRooms;
        type = Message.PLAYER_STATE;
    }

    @Override
    public void handle(Socket socket, Message message) throws WrongDataForThisMessageTypeException {
        GameRoom room = GameRoomUtil.getGameRoom(socket, gameRooms);
        Map<String, String> map = Message.toMap(message);
        Player player;
        Player opponent;
        if (room.getPlayer1().getSocket().equals(socket)) {
            opponent = room.getPlayer2();
            player = room.getPlayer1();
        } else {
            opponent = room.getPlayer1();
            player = room.getPlayer2();
        }
        if (!map.containsKey("alive") || !(map.get("alive").equals("0") || map.get("alive").equals("1"))) {
            throw new WrongDataForThisMessageTypeException();
        } else {
            player.setAlive(Objects.equals(map.get("alive"), "1"));
        }
        Message.sendMessage(opponent.getSocket(), message);
        if (!opponent.isAlive() && !player.isAlive()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            room.endGame(null);
        }

    }
}
