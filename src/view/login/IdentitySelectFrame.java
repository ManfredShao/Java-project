package view.login;

import model.Map;
import model.MapModel;
import user.User;
import view.FrameUtil;
import view.game.GameFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class IdentitySelectFrame extends JFrame {
    public IdentitySelectFrame() {
        this.setSize(280, 280);
        this.setTitle("身份选择");
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setLayout(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton guestBtn = FrameUtil.createButton(this, "小试牛刀", 100, 40);
        JButton playerBtn = FrameUtil.createButton(this, "挂帅亲征", 100, 40);

        guestBtn.setBounds(80, 50, 100, 40);
        playerBtn.setBounds(80, 120, 100, 40);

        add(guestBtn);
        add(playerBtn);

        guestBtn.addActionListener(e -> {
            this.setVisible(false);
            selectLevel(new User(null, null));
        });

        playerBtn.addActionListener(e -> {
            this.setVisible(false);
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });

    }

    public static void selectLevel(User user) {
        JFrame frame = new JFrame("地图选择");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(300, 250);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel label = new JLabel("请选择一个地图等级：");
        label.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        panel.add(Box.createVerticalStrut(20));
        panel.add(label);
        panel.add(Box.createVerticalStrut(10));

        for (Map level : Map.values()) {
            JButton button = new JButton(level.name());
            button.setAlignmentX(JButton.CENTER_ALIGNMENT);
            panel.add(button);
            panel.add(Box.createVerticalStrut(10));

            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    frame.dispose();
                    GameFrame gameFrame = new GameFrame(level, user);
                    gameFrame.setVisible(true);
                    if (user.getPassword() == null && user.getUsername() == null) {
                        gameFrame.getSaveBtn().setVisible(false);
                        gameFrame.getLoadBtn().setVisible(false);
                    }
                }
            });
        }
        frame.setContentPane(panel);
        frame.setVisible(true);
    }
}
