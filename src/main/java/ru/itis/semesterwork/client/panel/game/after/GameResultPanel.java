package ru.itis.semesterwork.client.panel.game.after;

import ru.itis.semesterwork.config.Constants;
import ru.itis.semesterwork.ui.CustomButton;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class GameResultPanel extends JPanel {

    public GameResultPanel(JPanel panel, CardLayout cardLayout, Font font, String result) {
        setBackground(Color.BLACK);
        setLayout(new BorderLayout());

        JLabel label = new JLabel(result);
        label.setForeground(Color.WHITE);
        label.setFont(font.deriveFont(96f));
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.CENTER);
        add(label, BorderLayout.CENTER);

        JButton button = new CustomButton("OK", font.deriveFont(12f));

        button.addActionListener(e -> {
            cardLayout.show(panel, Constants.MENU_PANEL);
        });

        add(button, BorderLayout.SOUTH);


    }

    public static void main(String[] args) throws IOException, FontFormatException {
        JFrame frame = new JFrame("Die or die");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(Color.BLACK);

        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setUndecorated(true);
        frame.setLocationRelativeTo(null);
        CardLayout cardLayout = new CardLayout();
        JPanel mainPanel = new JPanel(cardLayout);
        Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File("C:\\java\\OrisGame\\src\\main\\resources\\yoster-island.regular.ttf")).deriveFont(12f);
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();

        ge.registerFont(customFont);
        frame.setFont(customFont);
        mainPanel.setFont(customFont);
        GameResultPanel gameResultPanel = new GameResultPanel(mainPanel, cardLayout, customFont, "You won");
        mainPanel.add(gameResultPanel, "gameResultPanel");
        frame.getContentPane().add(mainPanel);
        cardLayout.show(mainPanel, "gameResultPanel");
        frame.setVisible(true);
    }
}
