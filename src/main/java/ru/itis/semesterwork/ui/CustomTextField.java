package ru.itis.semesterwork.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class CustomTextField extends JTextField{

    public CustomTextField(Font font) {
        super(20);
        setFont(font.deriveFont(24f));
        setPreferredSize(new Dimension(300, 30));
        setForeground(Color.BLACK);
        setFocusable(true);
    }
}
