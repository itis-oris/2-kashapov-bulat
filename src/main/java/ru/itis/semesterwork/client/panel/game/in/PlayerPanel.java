package ru.itis.semesterwork.client.panel.game.in;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import ru.itis.semesterwork.common.model.Point;

import lombok.Getter;

public class PlayerPanel extends JPanel implements ActionListener, KeyListener {
    private int x, y;
    private final int sideLength;
    private final int squareX, squareY;
    private final int speed = 5;
    @Getter
    private final Image image;

    private boolean up, down, left, right;


    public int getPlayerX() {
        return x;
    }
    public int getPlayerY() {
        return y;
    }
    public PlayerPanel(Image image, int squareX, int squareY, int sideLength) {
        this.image = image;
        this.squareX = squareX;
        this.squareY = squareY;
        this.sideLength = sideLength;

        this.x = squareX + sideLength / 2 - image.getWidth(this) / 2;
        this.y = squareY + sideLength / 2 - image.getHeight(this) / 2;

        setOpaque(false);

        this.setFocusable(true);
        this.addKeyListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;



        g2.setColor(Color.LIGHT_GRAY);
        g2.drawRect(squareX, squareY, sideLength, sideLength);


        g2.drawImage(image, x, y, this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (up && y > squareY) y -= speed;
        if (down && y + image.getHeight(this) < squareY + sideLength) y += speed;
        if (left && x > squareX) x -= speed;
        if (right && x + image.getWidth(this) < squareX + sideLength) x += speed;


        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP -> up = true;
            case KeyEvent.VK_DOWN -> down = true;
            case KeyEvent.VK_LEFT -> left = true;
            case KeyEvent.VK_RIGHT -> right = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP -> up = false;
            case KeyEvent.VK_DOWN -> down = false;
            case KeyEvent.VK_LEFT -> left = false;
            case KeyEvent.VK_RIGHT -> right = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }



    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Game Launcher");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);

            CardLayout cardLayout = new CardLayout();
            frame.getContentPane().setLayout(cardLayout);

            JPanel startPanel = new JPanel();
            startPanel.setLayout(new GridBagLayout());

            JButton startButton = new JButton("Start Game");
            startPanel.add(startButton);

            Image image1 = new ImageIcon("C:\\java\\test\\src\\main\\resources\\character.png").getImage();

            PlayerPanel gamePanel = new PlayerPanel(image1,100, 100, 400);

            frame.getContentPane().add(startPanel, "StartPanel");
            frame.add(gamePanel, "GamePanel");

            startButton.addActionListener(e -> {
                cardLayout.show(frame.getContentPane(), "GamePanel");
                gamePanel.requestFocusInWindow();
            });

            frame.setVisible(true);
        });
    }
    public Point getCenter() {
        int centerX = x + image.getWidth(this) / 2;
        int centerY = y + image.getHeight(this) / 2;
        return new Point(centerX, centerY);
    }

    public int getRadius() {
        return Math.min(image.getWidth(this), image.getHeight(this)) / 2;
    }
}
