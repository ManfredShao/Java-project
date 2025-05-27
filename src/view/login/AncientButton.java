package view.login;

import javax.swing.*;
import java.awt.*;

class AncientButton extends JButton {
    private final Color baseColor = new Color(139, 0, 0);
    private final Color hoverColor = new Color(165, 42, 42);
    private final Color pressedColor = new Color(120, 0, 0);

    public AncientButton(String text) {
        super(text);
        setFont(new Font("楷体", Font.BOLD, 16));
        setForeground(new Color(245, 222, 179));
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(245, 222, 179), 2),
                BorderFactory.createEmptyBorder(8, 16, 8, 16)
        ));
        setBackground(baseColor);
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { setBackground(hoverColor); repaint(); }
            public void mouseExited(java.awt.event.MouseEvent e)  { setBackground(baseColor); repaint(); }
            public void mousePressed(java.awt.event.MouseEvent e) { setBackground(pressedColor); repaint(); }
            public void mouseReleased(java.awt.event.MouseEvent e){ setBackground(hoverColor); repaint(); }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
        super.paintComponent(g2);
        g2.dispose();
    }
}