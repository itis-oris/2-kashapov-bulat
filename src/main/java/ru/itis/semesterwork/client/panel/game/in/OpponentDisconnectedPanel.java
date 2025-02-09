package ru.itis.semesterwork.client.panel.game.in;

import ru.itis.semesterwork.config.Constants;
import ru.itis.semesterwork.ui.CustomButton;

import javax.swing.*;
import java.awt.*;

public class OpponentDisconnectedPanel extends JPanel {
    public OpponentDisconnectedPanel(JPanel panel, CardLayout cardLayout, Font font) {
        setBackground(Color.BLACK);
        setLayout(new BorderLayout());

        JLabel label = new JLabel("Opponent disconnected");
        label.setForeground(Color.WHITE);
        label.setFont(font.deriveFont(72f));
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.CENTER);
        add(label, BorderLayout.CENTER);

        JButton button = new CustomButton("OK", font.deriveFont(12f));

        button.addActionListener(e -> {
            cardLayout.show(panel, Constants.MENU_PANEL);
        });

        add(button, BorderLayout.SOUTH);

    }
}
