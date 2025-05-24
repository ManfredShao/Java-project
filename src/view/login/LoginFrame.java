package view.login;

import controller.UserController;
import user.User;
import view.FrameUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static view.login.IdentitySelectFrame.selectLevel;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginFrame extends JFrame {
    private JTextField username;
    private JPasswordField password;
    private JButton submitBtn;
    private JButton resetBtn;
    private JButton registerBtn;
    private JButton togglePasswordVisibilityBtn; // 控制密码可见性的按钮
    private boolean passwordVisible = false; // 密码是否可见的标志
    private ImageIcon eyeOpenIcon; // 眼睛打开的图标
    private ImageIcon eyeClosedIcon; // 眼睛闭上的图标

    public LoginFrame() {
        // 设置窗口属性
        this.setTitle("烽燧连天处，一局定乾坤");
        this.setSize(400, 250);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // 设置主面板的布局管理器
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        this.add(mainPanel);

        // 眼睛图标的加载
        ImageIcon originalEyeOpenIcon = new ImageIcon("eyeOpenIcon.jpg");
        ImageIcon originalEyeClosedIcon = new ImageIcon("eyeClosedIcon.jpg");

        // 获取输入框的高度
        int inputFieldHeight = new JPasswordField(20).getPreferredSize().height;

        // 等比例缩放图标以适配输入框的高度
        eyeOpenIcon = new ImageIcon(originalEyeOpenIcon.getImage().getScaledInstance(-1, inputFieldHeight, Image.SCALE_SMOOTH));
        eyeClosedIcon = new ImageIcon(originalEyeClosedIcon.getImage().getScaledInstance(-1, inputFieldHeight, Image.SCALE_SMOOTH));

        // 用户名标签和输入框
        JLabel userLabel = new JLabel("帅印:");
        username = new JTextField(20);

        // 密码标签和输入框
        JLabel passLabel = new JLabel("兵符:");
        password = new JPasswordField(20);
        password.setEchoChar('*');

        // 密码显示切换按钮
        togglePasswordVisibilityBtn = new JButton(eyeClosedIcon);
        togglePasswordVisibilityBtn.setBorderPainted(false);
        togglePasswordVisibilityBtn.setContentAreaFilled(false);

        // 设置按钮大小，使其高度与输入框一致
        togglePasswordVisibilityBtn.setPreferredSize(new Dimension(40, password.getPreferredSize().height));

        // 提交按钮
        submitBtn = new JButton("擂鼓进军");
        resetBtn = new JButton("重写军帖");
        registerBtn = new JButton("注册新帐");

        // 设置GridBagLayout布局约束并添加组件
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5); // 设置组件之间的行距和列距
        mainPanel.add(userLabel, gbc);

        gbc.gridx = 1;
        mainPanel.add(username, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(passLabel, gbc);

        gbc.gridx = 1;
        mainPanel.add(password, gbc);

        // 设置图标按钮的位置
        gbc.gridx = 2;
        mainPanel.add(togglePasswordVisibilityBtn, gbc);

        // 添加按钮
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        mainPanel.add(submitBtn, gbc);

        gbc.gridy = 3;
        mainPanel.add(resetBtn, gbc);

        gbc.gridy = 4;
        mainPanel.add(registerBtn, gbc);

        // 添加密码显示切换功能
        togglePasswordVisibilityBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (passwordVisible) {
                    password.setEchoChar('*');
                    togglePasswordVisibilityBtn.setIcon(eyeClosedIcon);
                    passwordVisible = false;
                } else {
                    password.setEchoChar((char) 0);
                    togglePasswordVisibilityBtn.setIcon(eyeOpenIcon);
                    passwordVisible = true;
                }
            }
        });

        // 提交按钮的功能
        submitBtn.addActionListener(e -> {
            System.out.println("Username = " + username.getText());
            System.out.println("Password = " + new String(password.getPassword()));

            if (UserController.validateUser(username.getText(), new String(password.getPassword()))) {
                Path userDir = Path.of("Save", username.getText());
                Path passwordFile = userDir.resolve("password.txt");
                if (Files.notExists(userDir)) {
                    JOptionPane.showMessageDialog(this, "此帐号未注册！", "军情有变", JOptionPane.ERROR_MESSAGE);
                } else {
                    try {
                        String line = Files.readString(passwordFile).trim();
                        String hashedPassword = hashPassword(new String(password.getPassword()));
                        assert hashedPassword != null;
                        if (!(line.equals(hashedPassword))) {
                            JOptionPane.showMessageDialog(this, "兵符有误，恐为敌军细作！", "军情有变", JOptionPane.ERROR_MESSAGE);
                        } else {
                            this.setVisible(false);
                            User user = new User(username.getText(), new String(password.getPassword()));
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

        // 重置按钮的功能
        resetBtn.addActionListener(e -> {
            username.setText("");
            password.setText("");
        });

        // 注册按钮的功能
        registerBtn.addActionListener(e -> {
            Path userDir = Path.of("Save", username.getText());
            Path passwordFile = userDir.resolve("password.txt");
            if (Files.notExists(userDir)) {
                try {
                    Files.createDirectories(userDir);
                    String hashedPassword = hashPassword(new String(password.getPassword()));
                    Files.writeString(passwordFile, hashedPassword);
                } catch (IOException d) {
                    throw new RuntimeException(d);
                }
                JOptionPane.showMessageDialog(this, "注册成功！请再次登录", "万事俱备", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "此帐号已被注册！", "军情有变", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashedBytes) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
