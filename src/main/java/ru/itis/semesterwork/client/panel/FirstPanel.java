package ru.itis.semesterwork.client.panel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class FirstPanel extends JPanel {
    public FirstPanel(JPanel panel, CardLayout cardLayout, Font font) {
        setBackground(Color.BLACK);
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Die or die");
        title.setForeground(Color.WHITE);
        title.setFont(font.deriveFont(96f));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setBorder(new EmptyBorder(10, 10, 10, 10));

        add(title, BorderLayout.NORTH);

        JLabel instructionLabel = new JLabel("Press any key to continue");
        instructionLabel.setForeground(Color.WHITE);
        instructionLabel.setFont(font.deriveFont(24f));
        instructionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        instructionLabel.setBorder(new EmptyBorder(10, 10, 10, 10));

        add(instructionLabel, BorderLayout.SOUTH);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                cardLayout.show(panel, "menuPanel");
            }
        });

        setFocusable(true);
    }
}
