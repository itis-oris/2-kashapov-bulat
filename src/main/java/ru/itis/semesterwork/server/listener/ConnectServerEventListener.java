package ru.itis.semesterwork.server.listener;

import ru.itis.semesterwork.server.logic.WaitingPlayers;
import ru.itis.semesterwork.common.model.Player;
import ru.itis.semesterwork.exception.WrongDataForThisMessageTypeException;
import ru.itis.semesterwork.server.logic.GameRoom;
import ru.itis.semesterwork.common.protocol.Message;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class ConnectServerEventListener extends ServerEventListener {

    private List<GameRoom> gameRooms;

    public ConnectServerEventListener(ServerSocket serverSocket, List<GameRoom> gameRooms) {
        super(serverSocket);
        this.gameRooms = gameRooms;
        type = Message.CONNECT;
    }

    @Override
    public void handle(Socket socket, Message message) throws WrongDataForThisMessageTypeException {
        String[] data = new String(message.getData()).split(":");
        if (data.length == 2 && data[0].equals("name")) {
            Player p = Player.builder().socket(socket).name(data[1]).build();
            System.out.println("Player: " + p.getName() + " connected");
            synchronized (WaitingPlayers.class) {
                Player p2 = WaitingPlayers.getNextPlayer();
                if (p2 != null) {
                    createGameRoom(p, p2);
                } else {
                    WaitingPlayers.addPlayer(p);
                }
            }
        } else {
            throw new WrongDataForThisMessageTypeException();
        }

    }

    public void createGameRoom(Player p1, Player p2) {
        GameRoom gameRoom = new GameRoom(p1, p2);
        synchronized (gameRooms) {
            gameRooms.add(gameRoom);
        }
        Message p1Message = new Message(("name:" + p2.getName()).getBytes(), Message.GAME_READY);
        Message p2Message = new Message(("name:" + p1.getName()).getBytes(), Message.GAME_READY);

        Message.sendMessage(p1.getSocket(), p1Message);
        Message.sendMessage(p2.getSocket(), p2Message);
    }
}
