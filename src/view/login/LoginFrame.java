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
            //todo:登录？注册？游客身份游玩还未实现（需要为客人和注册用户实现登录选择界面。）
            if (UserController.validateUser(username.getText(), password.getText())) {//输入有效
                Path userDir = Path.of("Save", username.getText());
                Path passwordFile = userDir.resolve("password.txt");
                // 判断目录是否存在且确实是文件夹
                if (Files.notExists(userDir)) {//未创建文件夹，即还没注册
                    //注册一下，创建对应用户名的目录和下面的password.txt。实际data.txt在saveGame里面才创建
                    //有可能注册过却没保存？就不会有data.txt
                    try {
                        Files.createDirectories(userDir);
                        Files.writeString(passwordFile, password.getText());
                    } catch (IOException d) {
                        throw new RuntimeException(d);
                    }

                    this.setVisible(false);
                    MapModel mapModel = new MapModel(Map.LEVEL_1);
                    User user = new User(username.getText(), password.getText());
                    GameFrame gameFrame = new GameFrame(mapModel, user);
                    gameFrame.setVisible(true);

                } else {//已注册，比对密码
                    try {
                        String line = Files.readString(passwordFile).trim();
                        if (!(line.equals(password.getText()))) {
                            JOptionPane.showMessageDialog(this, "兵符有误，恐为敌军细作！", "军情有变", JOptionPane.ERROR_MESSAGE);
                        } else {
                            this.setVisible(false);
                            MapModel mapModel = new MapModel(Map.LEVEL_1);
                            User user = new User(username.getText(), password.getText());
                            GameFrame gameFrame = new GameFrame(mapModel, user);
                            gameFrame.setVisible(true);
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

        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}
