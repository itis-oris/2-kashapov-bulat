package ru.itis.semesterwork.client.panel.game.in;

import lombok.Getter;

import javax.swing.*;
import java.awt.*;

public class OpponentPanel extends JPanel {
    private int x;
    private int y;
    @Getter
    private Image image;

    private int squareX;
    private int squareY;
    private int sideLength;

    public OpponentPanel(Image image, int squareX, int squareY, int sideLength) {
        this.image = image;
        this.x = squareX + (sideLength / 2);
        this.y = squareY + (sideLength / 2);
        this.squareX = squareX;
        this.squareY = squareY;
        this.sideLength = sideLength;

        setOpaque(false);
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.LIGHT_GRAY);
        g.drawRect(squareX, squareY, sideLength, sideLength);
        g.drawImage(image, squareX + x - 100, squareY + y - 100, this);
    }

    public void updatePosition(int x, int y) {
        this.x = x;
        this.y = y;
        repaint();
    }
}
