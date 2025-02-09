package ru.itis.semesterwork.client.panel.game.in;

import javax.swing.*;
import java.awt.*;

public class DeadMarkPanel extends JPanel {

    private Image image;
    private int curBounds;
    private int targetBounds;
    private float curSpeed = 1f;
    private Timer timer;


    public DeadMarkPanel(Image image, int x, int y, int bounds) {
        this.image = image;
        curBounds = bounds;
        targetBounds = image.getHeight(null) * 2;
        setLayout(null);
        setBounds(x, y, bounds, bounds);
        setOpaque(false);
        timer = new Timer(8, e -> {
            if (curBounds - (int)curSpeed > targetBounds) {
                curBounds -= (int) curSpeed;
                curSpeed *= 1.17f;
                repaint();
            } else {
                curBounds = targetBounds;
                repaint();
                timer.stop();
            }
        });

    }

    public void startAnimation() {
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int x = (getWidth() - curBounds) / 2;
        int y = (getHeight() - curBounds) / 2;
        g.drawImage(image, x, y, curBounds, curBounds, this);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Image Resize Animation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ImageIcon imageIcon = new ImageIcon("C:\\java\\OrisGame\\src\\main\\resources\\dead.png");
        Image image = imageIcon.getImage();

        DeadMarkPanel panel = new DeadMarkPanel(image, 0, 0, 600);

        frame.add(panel);
        frame.setSize(400, 400);
        frame.setLocationRelativeTo(null);
        panel.startAnimation();
        frame.setVisible(true);
    }
}
