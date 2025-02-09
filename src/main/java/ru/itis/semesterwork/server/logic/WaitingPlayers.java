package ru.itis.semesterwork.server.logic;

import ru.itis.semesterwork.common.model.Player;

import java.util.concurrent.ConcurrentLinkedQueue;

public class WaitingPlayers {
    private static final ConcurrentLinkedQueue<Player> waitingPlayers = new ConcurrentLinkedQueue<>();

    public static void addPlayer(Player player) {
        waitingPlayers.add(player);
    }

    public static Player getNextPlayer() {
        return waitingPlayers.poll();
    }

    public static boolean hasWaitingPlayers() {
        return !waitingPlayers.isEmpty();
    }
}
