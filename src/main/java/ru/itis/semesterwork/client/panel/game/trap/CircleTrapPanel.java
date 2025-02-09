package ru.itis.semesterwork.client.panel.game.trap;

import ru.itis.semesterwork.common.model.Trap;
import ru.itis.semesterwork.client.panel.game.in.PlayerPanel;

import java.awt.*;
import java.util.List;

import ru.itis.semesterwork.common.model.Point;

public class CircleTrapPanel extends TrapPanel {
    public CircleTrapPanel(Trap trap, List<Image> images, int x, int y, int bounds) {
        super(trap, images, x, y, bounds);
    }


    @Override
    public boolean isCollision(PlayerPanel player) {
        Point trapCenter = getCenter();
        Point playerCenter = player.getCenter();

        int dx = trapCenter.x - playerCenter.x;
        int dy = trapCenter.y - playerCenter.y;

        double distance = Math.sqrt(dx * dx + dy * dy);
        int combinedRadius = getRadius() + player.getRadius();

        return distance < combinedRadius;
    }

    public Point getCenter() {
        int centerX = squareX + (int) curX + images[0].getWidth(null) / 2;
        int centerY = squareY + (int) curY + images[0].getHeight(null) / 2;
        return new Point(centerX, centerY);
    }

    public int getRadius() {
        return (int) (images[0].getHeight(null) * 0.5 * 0.8);
    }
}
