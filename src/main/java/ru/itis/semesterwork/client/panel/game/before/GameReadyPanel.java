package ru.itis.semesterwork.client.panel.game.before;

import ru.itis.semesterwork.config.Constants;
import ru.itis.semesterwork.ui.CustomButton;
import ru.itis.semesterwork.exception.WrongDataForThisMessageTypeException;
import ru.itis.semesterwork.client.handler.GameReadyHandler;
import ru.itis.semesterwork.common.protocol.Message;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.net.Socket;
import java.util.Map;

public class GameReadyPanel extends JPanel implements GameReadyHandler {

    private CardLayout cardLayout;
    private JPanel mainPanel;
    private Font font;

    public GameReadyPanel(Socket socket, CardLayout cardLayout, JPanel mainPanel, Font font) {
        this.cardLayout = cardLayout;
        this.font = font;
        this.mainPanel = mainPanel;

        setBackground(Color.BLACK);
        setLayout(new BorderLayout());
        JLabel found = new JLabel("Opponent found");
        found.setForeground(Color.WHITE);
        found.setFont(font.deriveFont(72f));
        found.setHorizontalAlignment(SwingConstants.CENTER);
        found.setBorder(new EmptyBorder(10, 10, 10, 10));
        add(found, BorderLayout.NORTH);

        JButton readyButton = new CustomButton("Ready", font);
        readyButton.addActionListener(e -> {
            Message message = new Message(new byte[0], Message.GAME_READY);
            Message.sendMessage(socket, message);
            readyButton.setEnabled(false);
            readyButton.setForeground(Color.GRAY);
        });

        add(readyButton, BorderLayout.SOUTH);
    }

    @Override
    public void onGameReady(Message message) throws WrongDataForThisMessageTypeException {
        Map<String, String> map = Message.toMap(message);
        if (!map.containsKey("name")) {
            throw new WrongDataForThisMessageTypeException();
        } else {
            JLabel name = new JLabel("your opponent name is: " + map.get("name"));
            name.setForeground(Color.WHITE);
            name.setFont(font.deriveFont(36f));
            name.setHorizontalAlignment(SwingConstants.CENTER);
            name.setBorder(new EmptyBorder(10, 10, 10, 10));
            add(name, BorderLayout.CENTER);
            cardLayout.show(mainPanel, Constants.READY_PANEL);
        }
    }
}
