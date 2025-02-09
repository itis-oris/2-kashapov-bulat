package ru.itis.semesterwork.client.panel.game.trap;

import ru.itis.semesterwork.common.model.Trap;
import ru.itis.semesterwork.client.panel.game.in.PlayerPanel;

import java.awt.*;
import java.util.List;

public class RectTrapPanel extends TrapPanel {
    public RectTrapPanel(Trap trap, List<Image> images, int x, int y, int bounds) {
        super(trap, images, x, y, bounds);
    }

    @Override
    public boolean isCollision(PlayerPanel playerPanel) {
        Rectangle bounds = getHitBoxBounds();
        Rectangle playerBounds = new Rectangle(playerPanel.getPlayerX(), playerPanel.getPlayerY(), playerPanel.getImage().getWidth(null), playerPanel.getImage().getHeight(null));
        return bounds.intersects(playerBounds);
    }

    public Rectangle getHitBoxBounds() {
        int hitBoxWidth = (int) (images[0].getWidth(null) * 0.8);
        int hitBoxHeight = (int) (images[0].getHeight(null) * 0.8);

        int hitBoxX = squareX + (int) curX + (images[0].getWidth(null) - hitBoxWidth) / 2;
        int hitBoxY = squareY + (int) curY + (images[0].getHeight(null) - hitBoxHeight) / 2;

        return new Rectangle(hitBoxX, hitBoxY, hitBoxWidth, hitBoxHeight);
    }
}
