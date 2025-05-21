package view.login;

import controller.UserController;
import model.Map;
import model.MapModel;
import user.User;
import view.FrameUtil;
import view.game.GameFrame;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import static view.login.IdentitySelectFrame.selectLevel;


public class LoginFrame extends JFrame {
    private JTextField username;
    private JTextField password;
    private JButton submitBtn;
    private JButton resetBtn;
    private JButton registerBtn;

    public LoginFrame() {
        this.setTitle("烽燧连天处，一局定乾坤");
        this.setLayout(new GridLayout(7, 1, 1, 1));
        this.setSize(280, 280);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JLabel userLabel = FrameUtil.createJLabel(this, 70, 40, "帅印:");
        JLabel passLabel = FrameUtil.createJLabel(this, 70, 40, "兵符:");
        username = FrameUtil.createJTextField(this, 120, 40);
        password = FrameUtil.createJTextField(this, 120, 40);
        submitBtn = FrameUtil.createButton(this, "擂鼓进军", 100, 40);
        resetBtn = FrameUtil.createButton(this, "重写军帖", 100, 40);
        registerBtn = FrameUtil.createButton(this, "注册新帐", 100, 40);
        add(userLabel);
        add(username);
        add(passLabel);
        add(password);
        add(submitBtn);
        add(resetBtn);
        add(registerBtn);

        submitBtn.addActionListener(e -> {
            System.out.println("Username = " + username.getText());
            System.out.println("Password = " + password.getText());

            if (UserController.validateUser(username.getText(), password.getText())) {//输入有效
                Path userDir = Path.of("Save", username.getText());
                Path passwordFile = userDir.resolve("password.txt");
                // 判断目录是否存在
                if (Files.notExists(userDir)) {//未创建文件夹，即还没注册
                    JOptionPane.showMessageDialog(this, "此帐号未注册！", "军情有变", JOptionPane.ERROR_MESSAGE);
                } else {//已注册，比对密码
                    try {
                        String line = Files.readString(passwordFile).trim();
                        if (!(line.equals(password.getText()))) {
                            JOptionPane.showMessageDialog(this, "兵符有误，恐为敌军细作！", "军情有变", JOptionPane.ERROR_MESSAGE);
                        } else {
                            this.setVisible(false);
                            User user = new User(username.getText(), password.getText());
                            selectLevel(user);
                        }

                    } catch (IOException d) {
                        throw new RuntimeException(d);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "兵符有误，恐为敌军细作！", "军情有变", JOptionPane.ERROR_MESSAGE);
            }
        });
        resetBtn.addActionListener(e -> {
            username.setText("");
            password.setText("");
        });

        registerBtn.addActionListener(e -> {
            Path userDir = Path.of("Save", username.getText());
            Path passwordFile = userDir.resolve("password.txt");
            if (Files.notExists(userDir)) {
                try {
                    Files.createDirectories(userDir);
                    Files.writeString(passwordFile, password.getText());
                } catch (IOException d) {
                    throw new RuntimeException(d);
                }
                JOptionPane.showMessageDialog(this, "注册成功！请再次登录", "万事俱备", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "此帐号已被注册！", "军情有变", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
