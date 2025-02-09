package ru.itis.semesterwork.client.panel.game.trap;

import lombok.Getter;
import ru.itis.semesterwork.client.panel.game.in.DeadMarkPanel;
import ru.itis.semesterwork.client.panel.game.in.PlayerFieldPanel;
import ru.itis.semesterwork.client.panel.game.in.PlayerPanel;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TrapsPanel extends JPanel {
    private static final int IMAGE_CYCLE = 128;
    protected int squareX;
    protected int squareY;
    @Getter
    private List<TrapPanel> trapPanelList;

    public TrapsPanel(List<TrapPanel> trapPanelList, int squareX, int squareY, int bounds) {
        this.trapPanelList = trapPanelList;
        this.squareX = squareX;
        this.squareY = squareY;
        setBounds(squareX, squareY, bounds, bounds);

        setOpaque(false);

        setDoubleBuffered(true);
    }

    public boolean moveTraps(JLayeredPane jLayeredPane, long startTime, PlayerFieldPanel playerFieldPanel, DeadMarkPanel deadMarkPanel, boolean playerIsAlive, PlayerPanel playerPanel) {
        return moveUniversal(false, jLayeredPane, startTime, playerFieldPanel, deadMarkPanel, playerIsAlive, playerPanel);
    }
    public boolean moveTraps(JLayeredPane jLayeredPane, long startTime, PlayerFieldPanel playerFieldPanel,DeadMarkPanel deadMarkPanel, boolean playerIsAlive) {
        return moveUniversal(true, jLayeredPane, startTime, playerFieldPanel, deadMarkPanel, playerIsAlive, null);
    }

    private boolean moveUniversal(boolean isOpponent, JLayeredPane jLayeredPane, long startTime, PlayerFieldPanel playerFieldPanel, DeadMarkPanel deadMarkPanel, boolean playerIsAlive, PlayerPanel playerPanel) {
        if (playerIsAlive) {
            for (TrapPanel trap : trapPanelList) {
                long curTime = System.nanoTime();
                if ((curTime - startTime) / 1e+6 < trap.getTrap().getEndTime()) {
                    if ((curTime - startTime) / 1e+6 >= trap.getTrap().getStartTime()) {
                        trap.setActive(true);
                        trap.moveTrap();
                    }
                    if (!isOpponent && trap.isActive() && trap.isCollision(playerPanel)) {
                        playerFieldPanel.remove(playerPanel);
                        jLayeredPane.add(deadMarkPanel, 0);
                        deadMarkPanel.startAnimation();
                        playerIsAlive = false;
                    }
                } else {
                    trap.setActive(false);
                }
            }
        }
        repaint();
        return playerIsAlive;
    }

        @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (TrapPanel trapPanel : trapPanelList) {
            if (trapPanel.isActive()) {
                Image[] images = trapPanel.getImages();
                int curImage = trapPanel.getCurImage();
                Image drawImage = images[curImage / (IMAGE_CYCLE / images.length)];
                trapPanel.setCurImage((curImage + 1) % IMAGE_CYCLE);

                g.drawImage(drawImage, (int) trapPanel.getCurX(), (int) trapPanel.getCurY(), this);
            }
        }
    }
}
