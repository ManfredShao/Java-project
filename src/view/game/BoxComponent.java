package view.game;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BoxComponent extends JComponent {
    private Color baseColor;
    private int row;
    private int col;
    private boolean isSelected;
    private Image image;

    public void setImage(String i) {
        this.image = Toolkit.getDefaultToolkit().getImage(i);
    }

    public BoxComponent(Color color, int row, int col) {
        this.baseColor = color;
        this.row = row;
        this.col = col;
        isSelected = false;
        this.setFont(new Font("PingFang SC", Font.ITALIC, 20));
        this.setOpaque(false); // 透明背景
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);


        int width = getWidth();
        int height = getHeight();
        int arc = 20; // 圆角

        GradientPaint gradient = new GradientPaint(0, 0, baseColor.brighter(), 0, height, baseColor.darker());
        g2.setPaint(gradient);
        g2.fillRoundRect(0, 0, width, height, arc, arc);

        if (image != null) {
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.drawImage(image, 0, 0, getWidth(), getHeight(), this);
        }

        // 柔和边框
        g2.setColor(baseColor.darker().darker());
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawRoundRect(0, 0, width - 1, height - 1, arc, arc);

        // 高亮选中时添加黄色边框
        if (isSelected) {
            g2.setColor(new Color(255, 215, 0, 180)); // 半透明金黄
            g2.setStroke(new BasicStroke(3f));
            g2.drawRoundRect(2, 2, width - 5, height - 5, arc, arc);
        }

        g2.dispose();
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
        this.repaint();
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

}
