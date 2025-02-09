package ru.itis.semesterwork.client.panel;

import lombok.Getter;
import ru.itis.semesterwork.ui.CustomButton;
import ru.itis.semesterwork.ui.CustomTextField;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class NameInputPanel extends JPanel {
    @Getter
    private JTextField textField;
    @Getter
    private JButton button;
    public NameInputPanel(Font font) {
        setBackground(Color.BLACK);
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Your name");
        title.setFont(font.deriveFont(48f));
        title.setForeground(Color.WHITE);
        title.setFont(font.deriveFont(96f));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setBorder(new EmptyBorder(10, 10, 10, 10));
        add(title, BorderLayout.NORTH);
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centerPanel.setBackground(Color.BLACK);
        textField = new CustomTextField(font.deriveFont(24f));
        centerPanel.add(textField);

        add(centerPanel, BorderLayout.CENTER);

        button = new CustomButton("OK", font.deriveFont(24f));
        add(button, BorderLayout.SOUTH);

    }
}
