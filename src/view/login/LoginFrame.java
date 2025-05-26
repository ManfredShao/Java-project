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
    private boolean passwordVisible = false;
    private ImageIcon eyeOpenIcon;
    private ImageIcon eyeClosedIcon;

    public LoginFrame() {
        // 设置窗口属性
        this.setTitle("烽燧连天处，一局定乾坤");
        this.setSize(350, 280);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // 主面板和布局
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        this.add(mainPanel);

        // 眼睛图标加载与缩放
        ImageIcon originalEyeOpenIcon = new ImageIcon("eyeOpenIcon.jpg");
        ImageIcon originalEyeClosedIcon = new ImageIcon("eyeClosedIcon.jpg");
        int inputFieldHeight = new JPasswordField(20).getPreferredSize().height;
        eyeOpenIcon = new ImageIcon(originalEyeOpenIcon.getImage()
                .getScaledInstance(-1, inputFieldHeight, Image.SCALE_SMOOTH));
        eyeClosedIcon = new ImageIcon(originalEyeClosedIcon.getImage()
                .getScaledInstance(-1, inputFieldHeight, Image.SCALE_SMOOTH));

        // 标签和输入框初始化
        JLabel userLabel = new JLabel("帅印:");
        username = new JTextField(20);

        JLabel passLabel = new JLabel("兵符:");
        password = new JPasswordField(20);
        password.setEchoChar('*');

        togglePasswordVisibilityBtn = new JButton(eyeClosedIcon);
        togglePasswordVisibilityBtn.setBorderPainted(false);
        togglePasswordVisibilityBtn.setContentAreaFilled(false);
        togglePasswordVisibilityBtn.setPreferredSize(
                new Dimension(40, password.getPreferredSize().height)
        );

        submitBtn = new JButton("擂鼓进军");
        resetBtn = new JButton("重写军帖");
        registerBtn = new JButton("注册新帐");

        //设置布局约束并添加组件
        gbc.insets = new Insets(8, 5, 8, 5);

        gbc.ipady = 5;

        // 1. 用户名标签
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        mainPanel.add(userLabel, gbc);

        // 2. 用户名输入框（水平拉伸）
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        mainPanel.add(username, gbc);

        // 3. 密码标签
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(passLabel, gbc);

        // 4. 密码输入框（水平拉伸）
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        mainPanel.add(password, gbc);

        // 5. 切换可见按钮
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        mainPanel.add(togglePasswordVisibilityBtn, gbc);

        gbc.ipady = 0;

        // 6. 三个按钮：跨三列并居中
        gbc.gridwidth = 3;
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        gbc.gridy = 2;
        mainPanel.add(submitBtn, gbc);
        gbc.gridy = 3;
        mainPanel.add(resetBtn, gbc);
        gbc.gridy = 4;
        mainPanel.add(registerBtn, gbc);

        // 恢复默认列跨设置
        gbc.gridwidth = 1;

        togglePasswordVisibilityBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (passwordVisible) {
                    password.setEchoChar('*');
                    togglePasswordVisibilityBtn.setIcon(eyeClosedIcon);
                } else {
                    password.setEchoChar((char) 0);
                    togglePasswordVisibilityBtn.setIcon(eyeOpenIcon);
                }
                passwordVisible = !passwordVisible;
            }
        });

        submitBtn.addActionListener(e -> {
            System.out.println("Username = " + username.getText());
            System.out.println("Password = " + new String(password.getPassword()));

            if (UserController.validateUser(username.getText(), new String(password.getPassword()))) {
                Path userDir = Path.of("Save", username.getText());
                Path passwordFile = userDir.resolve("password.txt");
                if (Files.notExists(userDir)) {
                    JOptionPane.showMessageDialog(
                            this,
                            "此帐号未注册！",
                            "军情有变",
                            JOptionPane.ERROR_MESSAGE
                    );
                } else {
                    try {
                        String line = Files.readString(passwordFile).trim();
                        String hashed = hashPassword(new String(password.getPassword()));
                        if (!line.equals(hashed)) {
                            JOptionPane.showMessageDialog(
                                    this,
                                    "兵符有误，恐为敌军细作！",
                                    "军情有变",
                                    JOptionPane.ERROR_MESSAGE
                            );
                        } else {
                            this.setVisible(false);
                            User user = new User(username.getText(), new String(password.getPassword()));
                            selectLevel(user);
                        }
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "兵符有误，恐为敌军细作！",
                        "军情有变",
                        JOptionPane.ERROR_MESSAGE
                );
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
                    Files.writeString(passwordFile, hashPassword(new String(password.getPassword())));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                JOptionPane.showMessageDialog(
                        this,
                        "注册成功！请再次登录",
                        "万事俱备",
                        JOptionPane.INFORMATION_MESSAGE
                );
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "此帐号已被注册！",
                        "军情有变",
                        JOptionPane.ERROR_MESSAGE
                );
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