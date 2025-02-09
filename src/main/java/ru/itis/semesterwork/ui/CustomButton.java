package ru.itis.semesterwork.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class CustomButton extends JButton implements MouseListener {

    public CustomButton(String text, Font font) {
        super(text);
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setPreferredSize(new Dimension(100, 50));
        setForeground(Color.WHITE);
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setFont(font.deriveFont(32f));
        setHorizontalAlignment(SwingConstants.CENTER);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        addMouseListener(this);

    }
    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        setForeground(Color.DARK_GRAY);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        setForeground(Color.GRAY);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        setForeground(Color.GRAY);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        setForeground(Color.WHITE);
    }
}
