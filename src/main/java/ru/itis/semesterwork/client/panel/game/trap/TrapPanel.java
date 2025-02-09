package ru.itis.semesterwork.client.panel.game.trap;

import lombok.Getter;
import lombok.Setter;
import ru.itis.semesterwork.common.model.Point;
import ru.itis.semesterwork.common.model.Trap;
import ru.itis.semesterwork.client.panel.game.in.PlayerPanel;

import javax.swing.*;
import java.awt.*;

import java.util.List;

public abstract class TrapPanel {
    @Getter
    protected Trap trap;
    protected int currentIndex = 0;
    @Getter
    protected double curX, curY;
    @Getter
    protected Image[] images;
    @Getter @Setter
    protected int curImage = 0;
    protected int squareX;
    protected int squareY;

    @Getter @Setter
    private boolean isActive = false;

    public TrapPanel(Trap trap, List<Image> images, int x, int y, int bounds) {
        this.trap = trap;
        curX = trap.getTrajectory()[0].getX();
        curY = trap.getTrajectory()[0].getY();
        this.images = images.toArray(new Image[images.size()]);
        squareX = x;
        squareY = y;
    }
    public void moveTrap() {
        if (currentIndex < trap.getTrajectory().length - 1) {
            Point currentPoint = trap.getTrajectory()[currentIndex];
            Point nextPoint = trap.getTrajectory()[currentIndex + 1];

            double deltaX = nextPoint.x - currentPoint.x;
            double deltaY = nextPoint.y - currentPoint.y;
            
            double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

            if (distance > 0) {
                double normalizedDeltaX = deltaX / distance;
                double normalizedDeltaY = deltaY / distance;

                double stepX = normalizedDeltaX * trap.getSpeed();
                double stepY = normalizedDeltaY * trap.getSpeed();


                double remainingDistance = Math.sqrt(Math.pow(nextPoint.x - curX, 2) + Math.pow(nextPoint.y - curY, 2));
                if (remainingDistance <= trap.getSpeed()) {
                    curX = nextPoint.x;
                    curY = nextPoint.y;
                    currentIndex++;
                } else {
                    curX += stepX;
                    curY += stepY;
                }
            } else {
                curX = nextPoint.x;
                curY = nextPoint.y;
                currentIndex++;
            }
        }
    }

    public abstract boolean isCollision(PlayerPanel playerPanel);
}
