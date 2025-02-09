package ru.itis.semesterwork.server.logic;

import lombok.Getter;
import lombok.Setter;
import ru.itis.semesterwork.common.model.Player;
import ru.itis.semesterwork.common.protocol.Message;

import java.util.Timer;
import java.util.TimerTask;

public class GameRoom {
    @Getter
    private Player player1;
    @Getter
    private Player player2;
    private boolean p1Ready = false, p2Ready = false;
    @Getter @Setter
    private boolean gameOver = false;

    public GameRoom(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    public void setPlayerReady(Player player) {
        if (player.equals(player1)) {
            p1Ready = true;
        } else if (player.equals(player2)) {
            p2Ready = true;
        }
    }

    public boolean playersReady() {
        return p1Ready && p2Ready;
    }

    public void startGame() {
        Message message = new Message(new byte[0], Message.GAME_START);
        Message.sendMessage(player1.getSocket(), message);
        Message.sendMessage(player2.getSocket(), message);
        startGameTimer();
    }

    private void startGameTimer() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!gameOver) {
                    Player winner = null;
                    if (player1.isAlive() && !player2.isAlive()) {
                        winner = player1;
                    } else if (player2.isAlive() && !player1.isAlive()) {
                        winner = player2;
                    }
                    endGame(winner);
                }
            }
        }, 30 * 1000);
    }

    public void endGame(Player winner) {
        gameOver = true;
        if (winner != null) {
            Message winnerMessage = new Message("win:1".getBytes(), Message.GAME_RESULT);
            Message loserMessage = new Message("win:0".getBytes(), Message.GAME_RESULT);

            Player loser = winner.equals(player1) ? player2 : player1;

            Message.sendMessage(winner.getSocket(), winnerMessage);
            Message.sendMessage(loser.getSocket(), loserMessage);
        } else {
            Message drawMessage = new Message("win:-1".getBytes(), Message.GAME_RESULT);
            Message.sendMessage(player1.getSocket(), drawMessage);
            Message.sendMessage(player2.getSocket(), drawMessage);
        }

    }
}
