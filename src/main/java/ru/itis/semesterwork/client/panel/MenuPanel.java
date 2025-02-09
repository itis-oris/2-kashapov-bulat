package ru.itis.semesterwork.client.panel;

import lombok.Getter;
import ru.itis.semesterwork.config.Constants;
import ru.itis.semesterwork.ui.CustomButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class MenuPanel extends JPanel {

    private CardLayout cardLayout;
    private JButton startButton;
    @Getter
    private JTextField textField;

    public MenuPanel(JPanel panel, JFrame frame,  CardLayout cardLayout, Font font) {
        this.cardLayout = cardLayout;
        setBackground(Color.BLACK);
        setLayout(new BorderLayout());

        JLabel title = new JLabel(Constants.TITLE);
        title.setForeground(Color.WHITE);
        title.setFont(font.deriveFont(96f));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setBorder(new EmptyBorder(10, 10, 10, 10));

        add(title, BorderLayout.NORTH);

        JPanel buttonPanel = getButtonAndTextAreaPanel(panel, frame, font);

        add(buttonPanel, BorderLayout.CENTER);
    }
    public void setStartButton(ActionListener e) {
        startButton.addActionListener(e);
    }

    private JPanel getButtonAndTextAreaPanel(JPanel mainPanel, JFrame frame, Font font) {
        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 0, 10));
        buttonPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        buttonPanel.setBackground(Color.BLACK);


        startButton = new CustomButton("Start", font);

        startButton.addActionListener(e -> cardLayout.show(mainPanel, Constants.NAME_INPUT_PANEL));
        JButton exitButton = new CustomButton("Exit", font);

        exitButton.addActionListener(e -> frame.dispose());

        buttonPanel.add(startButton);
        buttonPanel.add(exitButton);
        return buttonPanel;
    }
}
