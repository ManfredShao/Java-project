package view;

import controller.AncientButton;

import javax.swing.*;
import java.awt.*;

/**
 * This class is to create basic JComponent.
 */
public class FrameUtil {
    public static JLabel createJLabel(JFrame frame, int width, int height, String text) {
        JLabel jLabel = new JLabel(text);
        jLabel.setSize(width, height);
        frame.add(jLabel);
        return jLabel;
    }

    public static JLabel createJLabel(JFrame frame, String name, Font font, int width, int height) {
        JLabel label = new JLabel(name);
        label.setFont(font);
        label.setSize(width, height);
        frame.add(label);
        return label;
    }

    public static JTextField createJTextField(JFrame frame, int width, int height) {
        JTextField jTextField = new JTextField();
        jTextField.setSize(width, height);
        frame.add(jTextField);
        return jTextField;
    }

    public static AncientButton createButton(JFrame frame, String name, int width, int height) {
        AncientButton button = new AncientButton(name);
        button.setSize(width, height);
        frame.add(button);
        return button;
    }
}
