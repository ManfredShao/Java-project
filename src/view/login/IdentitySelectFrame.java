package view.login;

import model.Map;
import user.User;
import controller.GameFrame;

import javax.swing.*;
import java.awt.*;


public class IdentitySelectFrame extends JFrame {
    public IdentitySelectFrame() {
        setTitle("身份选择");
        setSize(300, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 纯色背景面板
        JPanel panel = new JPanel();
        panel.setBackground(new Color(27, 27, 27));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // 标题
        JLabel title = new JLabel("来者何人");
        title.setFont(new Font("楷体", Font.BOLD, 22));
        title.setForeground(new Color(245, 222, 179));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(title);
        panel.add(Box.createVerticalStrut(40));

        // 按钮
        AncientButton guestBtn = new AncientButton("游侠暂驻·萍踪无迹");
        AncientButton playerBtn = new AncientButton("将军百战·沙场留名");
        guestBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        playerBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(guestBtn);
        panel.add(Box.createVerticalStrut(20));
        panel.add(playerBtn);

        // 事件
        guestBtn.addActionListener(e -> { dispose(); selectLevel(new User(null, null)); });
        playerBtn.addActionListener(e -> { dispose(); new LoginFrame().setVisible(true); });

        setContentPane(panel);
        setVisible(true);
    }

    public static void selectLevel(User user) {
        JFrame frame = new JFrame("地图选择");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(320, 330);
        frame.setLocationRelativeTo(null);

        // 纯色背景面板
        JPanel panel = new JPanel();
        panel.setBackground(new Color(27, 27, 27));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.add(Box.createVerticalStrut(10));
        JLabel label = new JLabel("择阵而入");
        label.setFont(new Font("楷体", Font.BOLD, 22));
        label.setForeground(new Color(245, 222, 179));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(label);
        panel.add(Box.createVerticalStrut(30));

        for (Map level : Map.values()) {
            AncientButton btn = new AncientButton(level.name());
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(btn);
            panel.add(Box.createVerticalStrut(15));
            btn.addActionListener(e -> {
                frame.dispose();
                GameFrame gf = new GameFrame(level, user);
                gf.setVisible(true);
                if (user.getPassword() == null && user.getUsername() == null) {

                    gf.getSaveBtn().setVisible(false);
                    gf.getLoadBtn().setVisible(false);
                    gf.getServerBtn().setVisible(false);

                }else{
                    gf.getLoginBtn().setVisible(false);
                }
            });
        }

        frame.setContentPane(panel);
        frame.setVisible(true);
    }
}
