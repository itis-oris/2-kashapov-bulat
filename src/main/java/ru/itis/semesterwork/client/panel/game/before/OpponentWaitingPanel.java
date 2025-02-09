package ru.itis.semesterwork.client.panel.game.before;

import ru.itis.semesterwork.config.Constants;
import ru.itis.semesterwork.ui.CustomButton;

import javax.swing.*;
import java.awt.*;

public class OpponentWaitingPanel extends JPanel {

    public OpponentWaitingPanel(CardLayout cardLayout, JPanel mainPanel, Font font) {
        setBackground(Color.BLACK);
        setLayout(new BorderLayout());


        JLabel opponentWaitingLabel = new JLabel("Waiting for opponent");
        opponentWaitingLabel.setForeground(Color.WHITE);
        opponentWaitingLabel.setFont(font.deriveFont(36f));
        opponentWaitingLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(opponentWaitingLabel, BorderLayout.CENTER);

        JButton backButton = new CustomButton("Back", font.deriveFont(12f));
        backButton.addActionListener(e -> {
            cardLayout.previous(mainPanel);
        });

        add(backButton, BorderLayout.SOUTH);

        mainPanel.add(this, Constants.OPPONENT_WAITING);
    }
}
