package view.login;

import controller.UserController;
import model.Map;
import model.MapModel;
import user.User;
import view.FrameUtil;
import view.game.GameFrame;

import javax.swing.*;
import java.awt.*;


public class LoginFrame extends JFrame {
    private JTextField username;
    private JTextField password;
    private JButton submitBtn;
    private JButton resetBtn;

    public LoginFrame(int width, int height) {
        this.setTitle("烽燧连天处，一局定乾坤");
        this.setLayout(new GridLayout(6, 1, 10, 10));
        this.setSize(width, height);
        JLabel userLabel = FrameUtil.createJLabel(this, 70, 40, "帅印:");
        JLabel passLabel = FrameUtil.createJLabel(this, 70, 40, "兵符:");
        username = FrameUtil.createJTextField(this, 120, 40);
        password = FrameUtil.createJTextField(this, 120, 40);
        submitBtn = FrameUtil.createButton(this, "擂鼓进军", 100, 40);
        resetBtn = FrameUtil.createButton(this, "重写军帖", 100, 40);
        add(userLabel);
        add(username);
        add(passLabel);
        add(password);
        add(submitBtn);
        add(resetBtn);

        submitBtn.addActionListener(e -> {
            System.out.println("Username = " + username.getText());
            System.out.println("Password = " + password.getText());
            //todo: check login info
            if (UserController.validateUser(username.getText(), password.getText())) {
                this.setVisible(false);
                User user = new User(username.getText(), password.getText());
                MapModel mapModel = new MapModel(Map.LEVEL_1);
                GameFrame gameFrame = new GameFrame(mapModel, user);
                gameFrame.setVisible(true);
            }else{
                JOptionPane.showMessageDialog(this,"兵符有误，恐为敌军细作！", "军情有变", JOptionPane.ERROR_MESSAGE);
            }
        });
        resetBtn.addActionListener(e -> {
            username.setText("");
            password.setText("");
        });

        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}
