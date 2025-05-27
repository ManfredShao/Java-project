package view.login;

import controller.UserController;
import user.User;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.swing.*;
import java.awt.*;

import static view.login.IdentitySelectFrame.selectLevel;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginFrame extends JFrame {
    private final JTextField username;
    private final JPasswordField password;
    private final JButton togglePasswordVisibilityBtn; // 控制密码可见性的按钮
    private boolean passwordVisible = false;
    private final ImageIcon eyeOpenIcon;
    private final ImageIcon eyeClosedIcon;

    static class AncientButton extends JButton {
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
                public void mouseEntered(java.awt.event.MouseEvent e) {
                    setBackground(hoverColor);
                    repaint();
                }
                public void mouseExited(java.awt.event.MouseEvent e) {
                    setBackground(baseColor);
                    repaint();
                }
                public void mousePressed(java.awt.event.MouseEvent e) {
                    setBackground(pressedColor);
                    repaint();
                }
                public void mouseReleased(java.awt.event.MouseEvent e) {
                    setBackground(hoverColor);
                    repaint();
                }
            });
        }

        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
            super.paintComponent(g2);
            g2.dispose();
        }
    }

    public LoginFrame() {
        // 设置窗口属性
        this.setTitle("烽燧连天处，一局定乾坤");
        this.setSize(380, 330);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // 主面板和布局
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(27, 27, 27));
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
        userLabel.setFont(new Font("楷体", Font.BOLD, 15));
        userLabel.setForeground(new Color(245, 222, 179));
        username = new JTextField(20);
        username.setBackground(new Color(27,27,27));
        username.setForeground(new Color(245, 222, 179));       // 象牙白文字
        username.setBorder(
                BorderFactory.createLineBorder(new Color(245, 222, 179), 2)
        );

        JLabel passLabel = new JLabel("兵符:");
        passLabel.setFont(new Font("楷体", Font.BOLD, 15));

        passLabel.setForeground(new Color(245, 222, 179));
        password = new JPasswordField(20);
        password.setEchoChar('*');
        userLabel.setForeground(new Color(245, 222, 179));
        password.setBackground(new Color(27,27,27));
        password.setForeground(new Color(245, 222, 179));       // 象牙白文字
        password.setBorder(
                BorderFactory.createLineBorder(new Color(245, 222, 179), 2)
        );

        togglePasswordVisibilityBtn = new JButton(eyeClosedIcon);
        togglePasswordVisibilityBtn.setBorderPainted(false);
        togglePasswordVisibilityBtn.setContentAreaFilled(false);
        togglePasswordVisibilityBtn.setPreferredSize(
                new Dimension(40, password.getPreferredSize().height)
        );

        AncientButton submitBtn = new AncientButton("擂鼓进军");
        AncientButton resetBtn = new AncientButton("重写军帖");
        AncientButton registerBtn = new AncientButton("注册新帐");

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

        togglePasswordVisibilityBtn.addActionListener(e -> {
            if (passwordVisible) {
                password.setEchoChar('*');
                togglePasswordVisibilityBtn.setIcon(eyeClosedIcon);
            } else {
                password.setEchoChar((char) 0);
                togglePasswordVisibilityBtn.setIcon(eyeOpenIcon);
            }
            passwordVisible = !passwordVisible;
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