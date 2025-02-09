package ru.itis.semesterwork.server;

import ru.itis.semesterwork.common.model.Player;
import ru.itis.semesterwork.exception.WrongDataForThisMessageTypeException;
import ru.itis.semesterwork.server.logic.GameRoom;
import ru.itis.semesterwork.server.listener.ConnectServerEventListener;
import ru.itis.semesterwork.server.listener.GameReadyServerEventListener;
import ru.itis.semesterwork.server.listener.PlayerStateServerEventListener;
import ru.itis.semesterwork.server.listener.ServerEventListener;
import ru.itis.semesterwork.common.protocol.Message;
import ru.itis.semesterwork.utils.GameRoomUtil;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private static final int PORT = 8080;
    private final List<ServerEventListener> listeners = new ArrayList<>();
    private List<GameRoom> gameRooms;

    public Server() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started on port " + PORT);
            gameRooms = new ArrayList<>();
            addAllListeners(listeners, serverSocket, gameRooms);
            new Thread(() -> removeEndedGameRooms(gameRooms)).start();
            while (true) {

                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());


                new Thread(() -> handleClient(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addAllListeners(List<ServerEventListener> listeners, ServerSocket serverSocket, List<GameRoom> gameRooms) {
        listeners.add(new ConnectServerEventListener(serverSocket, gameRooms));
        listeners.add(new GameReadyServerEventListener(serverSocket, gameRooms));
        listeners.add(new PlayerStateServerEventListener(serverSocket, gameRooms));
    }

    private void handleClient(Socket clientSocket) {
        try (InputStream input = clientSocket.getInputStream()) {
            byte[] buffer = new byte[1024];

            while ((input.read(buffer)) != -1) {
                Message message = Message.getMessage(buffer);

                for (ServerEventListener listener : listeners) {
                    if (listener.getType() == message.getMessageType()) {
                        listener.handle(clientSocket, message);
                        break;
                    }
                }
            }
        } catch (SocketException e) {
            GameRoom room = GameRoomUtil.getGameRoom(clientSocket, gameRooms);
            if (room != null) {
                Player opponent = room.getPlayer1().getSocket().equals(clientSocket) ? room.getPlayer2() : room.getPlayer1();
                Message message = new Message(new byte[0], Message.OPPONENT_DISCONNECT);
                Message.sendMessage(opponent.getSocket(), message);
                room.setGameOver(true);
            }
        } catch (IOException | WrongDataForThisMessageTypeException e) {
            e.printStackTrace();
        }
    }

    public void removeEndedGameRooms(List<GameRoom> gameRooms) {
        try {
            for (GameRoom room : gameRooms) {
                if (room.isGameOver()) {
                    if (!room.getPlayer1().getSocket().isClosed()) {
                        room.getPlayer1().getSocket().close();
                    }
                    if (!room.getPlayer2().getSocket().isClosed()) {
                        room.getPlayer2().getSocket().close();
                    }
                    gameRooms.remove(room);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Server();
    }
}