package controller;

import Online.SocketClient;
import Online.SocketServer;
import model.Map;
import user.User;
import view.FrameUtil;
import view.game.CountdownTimer;
import view.game.GamePanel;
import view.login.LoginFrame;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.SocketException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class GameFrame extends JFrame {

    private final GameState gameState;
    SocketServer server = new SocketServer(8888);
    private final GameController controller;
    private final AncientButton loadBtn;
    private final AncientButton saveBtn;
    private final CountdownTimer time = new CountdownTimer();
    private final User user;
    private final GamePanel gamePanel;

    public CountdownTimer getTime() {
        return time;
    }

    public AncientButton getSaveBtn() {
        return saveBtn;
    }

    public AncientButton getLoadBtn() {
        return loadBtn;
    }

    public User getUser() {
        return user;
    }

    public GameFrame(Map level, User user) {
        super("華容道·漢末風雲");
        this.getContentPane().setBackground(new Color(27, 27, 27));
        this.setLayout(new GridBagLayout());
        gamePanel = new GamePanel(level);
        gamePanel.setBackground(new Color(27, 27, 27));
        gameState = new GameState();
        this.controller = new GameController(gamePanel, level);
        this.user = user;

        int height = 50;
        AncientButton restartBtn = FrameUtil.createButton(this, "重整旗鼓", 80, height);
        this.loadBtn = FrameUtil.createButton(this, "讀取戰局", 80, height);
        this.saveBtn = FrameUtil.createButton(this, "寫入戰局", 80, height);
        AncientButton solveBtnBFS = FrameUtil.createButton(this, "广域探骊BFS", 80, height);
        AncientButton solveBtnDFS = FrameUtil.createButton(this, "隐栈潜行DFS", 80, height);
        AncientButton pauseBtn = FrameUtil.createButton(this, "凝思", 80, height);
        AncientButton resumeBtn = FrameUtil.createButton(this, "续弈", 80, height);
        AncientButton revokeBtn = FrameUtil.createButton(this, "撤兵", 80, height);
        AncientButton clientBtn = FrameUtil.createButton(this, "鉴棋", 80, height);
        AncientButton severBtn = FrameUtil.createButton(this, "掌棋", 80, height);
        JLabel stepLabel = FrameUtil.createJLabel(this, "佈陣開局", new Font("楷体", Font.BOLD, 22), 80, height);
        stepLabel.setForeground(new Color(245, 222, 179));
        JLabel userLabel = FrameUtil.createJLabel(this, user.getUsername(), new Font("楷体", Font.BOLD, 22), 80, height);
        userLabel.setForeground(new Color(245, 222, 179));
        AncientButton loginBtn = FrameUtil.createButton(this, "登錄", 80, height);
        gamePanel.setStepLabel(stepLabel);

        int mapWidth = 4; // 假设地图的宽度为 4
        int mapHeight = 5; // 假设地图的高度为 5
        int gridWidth = mapWidth * gamePanel.getGRID_SIZE();
        int gridHeight = mapHeight * gamePanel.getGRID_SIZE();
        gamePanel.setPreferredSize(new Dimension(gridWidth, gridHeight));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(60, 30, 0, 20);

        // 添加游戏面板
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.gridwidth = 3;
        gbc.gridheight = 3;
        this.add(gamePanel, gbc);

        // 添加重启按钮
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        this.add(restartBtn, gbc);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBackground(new Color(27, 27, 27));
        bottomPanel.add(userLabel);
        bottomPanel.add(loginBtn);
        bottomPanel.add(restartBtn);
        bottomPanel.add(loadBtn);
        bottomPanel.add(saveBtn);
        bottomPanel.add(stepLabel);

        // 添加底部面板
        GridBagConstraints gbcBottom = new GridBagConstraints();
        gbcBottom.gridx = 0;
        gbcBottom.gridy = 3;
        gbcBottom.gridwidth = 4;
        gbcBottom.fill = GridBagConstraints.HORIZONTAL;
        gbcBottom.insets = new Insets(0, 3, 20, 3);
        this.add(bottomPanel, gbcBottom);

        // 添加计时器
        time.setBackground(new Color(27, 27, 27));
        time.setOpaque(true);
        GridBagConstraints gbcTimer = new GridBagConstraints();
        gbcTimer.gridx = 3;
        gbcTimer.gridy = 0;
        gbcTimer.gridwidth = 1;
        gbcTimer.gridheight = 1;
        gbcTimer.fill = GridBagConstraints.BOTH;
        gbcTimer.insets = new Insets(10, 0, 10, 20);  // 设置边距
        this.add(time, gbcTimer);

        // 创建新的右侧按钮面板
        JPanel rightControlPanel = new JPanel(new GridLayout(7, 1, 5, 15)); // 3行1列，垂直间距10
        rightControlPanel.setBackground(new Color(27, 27, 27));
        rightControlPanel.add(solveBtnBFS);
        rightControlPanel.add(solveBtnDFS);
        rightControlPanel.add(pauseBtn);
        rightControlPanel.add(resumeBtn);
        rightControlPanel.add(revokeBtn);
        rightControlPanel.add(severBtn);
        rightControlPanel.add(clientBtn);

        // 添加右侧控制面板
        GridBagConstraints gbcRightCtrl = new GridBagConstraints();
        gbcRightCtrl.gridx = 3;
        gbcRightCtrl.gridy = 1;  // 计时器在0行，此面板在1行
        gbcRightCtrl.gridwidth = 1;
        gbcRightCtrl.gridheight = 1;
        gbcRightCtrl.fill = GridBagConstraints.BOTH;
        gbcRightCtrl.insets = new Insets(5, 30, 20, 50);
        this.add(rightControlPanel, gbcRightCtrl);

        severBtn.addActionListener(e -> {
            server.addConnectListener(socket -> {
                System.out.println("客户端已连接！");
                controller.saveGame(user);
                try {
                    Path path = Path.of("Save/" + user.getUsername() + "/data.txt");
                    List<String> lines = Files.readAllLines(path);
                    for (String line : lines) {
                        System.out.println("正在发送: " + line);
                        server.sendLine(line);
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                    server.sendLine("读取棋盘数据失败！ERROR:" + ex.getMessage());
                }
            });
            server.addMessageListener(msg -> System.out.println("服务器收到消息: " + msg));
            server.addErrorListener(ex -> System.err.println("服务器异常: " + ex.getMessage()));
            server.start();
            try {
                // 获取当前窗口（GameFrame）
                Window parentWindow = SwingUtilities.getWindowAncestor(GameFrame.this);
                // 创建一个模态对话框
                JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(GameFrame.this), "", true);
                dialog.setLayout(new BorderLayout());
                dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

                String ip = SocketServer.getIp();
                String msg = String.format(
                        "<html><div style='text-align:center;'>伺服器已啟動，IP: %s</div></html>",
                        ip
                );
                JLabel message = new JLabel(msg, SwingConstants.CENTER);
                message.setFont(new Font("楷体", Font.BOLD, 20));
                dialog.add(message, BorderLayout.CENTER);
                AncientButton confirmBtn = new AncientButton("已知晓");
                confirmBtn.setFont(new Font("楷体", Font.BOLD, 16));
                confirmBtn.addActionListener(f -> {
                    dialog.dispose(); // 关闭对话框
                });

                JPanel buttonPanel = new JPanel();
                buttonPanel.add(confirmBtn);
                dialog.add(buttonPanel, BorderLayout.SOUTH);


                dialog.setSize(300, 180);
                dialog.setLocationRelativeTo(null); // 居中
                dialog.setVisible(true);
            } catch (SocketException ex) {
                throw new RuntimeException(ex);
            }
            gamePanel.requestFocusInWindow();
        });

        clientBtn.addActionListener(e -> {
            // 取得父窗口
            Window parentWindow = SwingUtilities.getWindowAncestor(GameFrame.this);
// 创建一个无标题、模态的对话框
            JDialog inputDialog = new JDialog((Frame) parentWindow, "加入战局", true);
            inputDialog.setLayout(new BorderLayout());
            inputDialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
            inputDialog.getContentPane().setBackground(new Color(27, 27, 27));

// 1. 提示文字
            JLabel prompt = new JLabel("请输入对方的 IP 地址：", SwingConstants.CENTER);
            prompt.setFont(new Font("楷体", Font.BOLD, 16));
            prompt.setForeground(new Color(245, 222, 179));
            prompt.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
            inputDialog.add(prompt, BorderLayout.NORTH);

// 2. 输入框
            JTextField ipField = new JTextField();
            ipField.setFont(new Font("楷体", Font.PLAIN, 14));
            ipField.setForeground(Color.WHITE);
            ipField.setBackground(new Color(50, 50, 50));
            ipField.setCaretColor(Color.WHITE);
            ipField.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            ipField.setPreferredSize(new Dimension(200, 30));

            JPanel center = new JPanel();
            center.setBackground(new Color(27, 27, 27));
            center.add(ipField);
            inputDialog.add(center, BorderLayout.CENTER);

// 3. 按钮区
            AncientButton okBtn = new AncientButton("确定");
            okBtn.setFont(new Font("楷体", Font.BOLD, 16));
            AncientButton cancelBtn = new AncientButton("取消");
            cancelBtn.setFont(new Font("楷体", Font.BOLD, 16));

            okBtn.addActionListener(e2 -> {
                inputDialog.dispose();
            });
            cancelBtn.addActionListener(e2 -> {
                ipField.setText(null);
                inputDialog.dispose();
            });

            JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
            btnPanel.setBackground(new Color(27, 27, 27));
            btnPanel.add(okBtn);
            btnPanel.add(cancelBtn);
            inputDialog.add(btnPanel, BorderLayout.SOUTH);

// 4. 显示对话框并取得结果
            inputDialog.pack();
            inputDialog.setLocationRelativeTo(parentWindow);
            inputDialog.setVisible(true);

// pack 之后就能通过 ipField.getText() 拿到用户输入
            String input = ipField.getText();
            if (input != null && !input.isBlank()) {
                // 用户点击“确定”并输入了内容
                SocketClient client = new SocketClient(input, 8888);
                // …后续逻辑…
            } else {
                // 用户取消或没输入
                System.out.println("用户取消或未输入 IP");
            }

            if (input != null) {
                SocketClient client = new SocketClient(input, 8888);
                client.addConnectListener(s -> System.out.println("客户端已连接服务器"));
                List<String> receivedLines = new ArrayList<>();
                client.addMessageListener(data -> {
                    System.out.println("客户端收到: " + data);

                    if (data.startsWith("ERROR")) {
                        System.err.println("传输错误: " + data.substring(6));
                    }
                    receivedLines.add(data);

                    // 达到 8 行后开始加载
                    if (receivedLines.size() == 8) {
                        controller.loadGame(new ArrayList<>(receivedLines));
                        receivedLines.clear();
                    }
                });
                client.addErrorListener(ex -> System.err.println("客户端异常: " + ex.getMessage()));
                client.connect();
            } else {
                System.out.println("用户取消了输入");
            }
            gamePanel.requestFocusInWindow();
        });
        solveBtnBFS.addActionListener(e -> {
            gameState.solvePuzzleBFS();
            gameState.startAnimation();
            gamePanel.requestFocusInWindow();
        });
        solveBtnDFS.addActionListener(e -> {
            gameState.solvePuzzleDFS();
            gameState.startAnimation();
            gamePanel.requestFocusInWindow();
        });
        pauseBtn.addActionListener(e -> {
            gameState.pauseAnimation();
            gamePanel.requestFocusInWindow();
        });

        resumeBtn.addActionListener(e -> {
            gameState.resumeAnimation();
            gamePanel.requestFocusInWindow();
        });
        restartBtn.addActionListener(e -> {
            controller.restartGame();
            gamePanel.requestFocusInWindow();//enable key listener
        });
        this.loadBtn.addActionListener(e -> {
            try {
                controller.loadGame(user);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            gamePanel.requestFocusInWindow();
        });
        this.saveBtn.addActionListener(e -> {
            controller.saveGame(user);
            // 获取当前窗口（GameFrame）
            Window parentWindow = SwingUtilities.getWindowAncestor(GameFrame.this);
            // 创建一个模态对话框
            JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(GameFrame.this), "休养生息", true);
            dialog.setLayout(new BorderLayout());
            dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
            JLabel message = new JLabel("<html><div style='text-align: center;'>安营扎寨</div></html>", SwingConstants.CENTER);
            message.setFont(new Font("楷体", Font.BOLD, 20));
            dialog.add(message, BorderLayout.CENTER);
            AncientButton confirmBtn = new AncientButton("已知晓");
            confirmBtn.setFont(new Font("楷体", Font.BOLD, 16));
            confirmBtn.addActionListener(f -> {
                dialog.dispose(); // 关闭对话框
            });

            JPanel buttonPanel = new JPanel();
            buttonPanel.add(confirmBtn);
            dialog.add(buttonPanel, BorderLayout.SOUTH);


            dialog.setSize(300, 180);
            dialog.setLocationRelativeTo(null); // 居中
            dialog.setVisible(true);
            gamePanel.requestFocusInWindow();
        });
        loginBtn.addActionListener(e -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
            time.pause();
            this.dispose();
        });

        JPanel directionPanel = new JPanel(new GridLayout(5, 3, 5, 5));// 3 行 3 列，格子间水平垂直5像素
        directionPanel.setBackground(new Color(27, 27, 27));
        AncientButton upBtn = new AncientButton("↑");
        AncientButton downBtn = new AncientButton("↓");
        AncientButton leftBtn = new AncientButton("←");
        AncientButton rightBtn = new AncientButton("→");
        directionPanel.add(new JLabel()); // (0,0) 空
        directionPanel.add(upBtn);        // (0,1)
        directionPanel.add(new JLabel()); // (0,2) 空
        directionPanel.add(leftBtn);      // (1,0)
        directionPanel.add(new JLabel()); // (1,1) 中心空
        directionPanel.add(rightBtn);     // (1,2)
        directionPanel.add(new JLabel()); // (2,0) 空
        directionPanel.add(downBtn);      // (2,1)
        directionPanel.add(new JLabel()); // (2,2) 空
        directionPanel.add(new JLabel());
        directionPanel.add(new JLabel());
        directionPanel.add(new JLabel());
        directionPanel.add(new JLabel());
        directionPanel.add(new JLabel());

        GridBagConstraints gbcDir = new GridBagConstraints();

        // 放在第 4 列 (gridx=3)，从第一行 (gridy=0) 开始
        gbcDir.gridx = 3;
        gbcDir.gridy = 2;
        gbcDir.gridwidth = 1;
        gbcDir.gridheight = 1;
        // 四周留点空隙
        gbcDir.insets = new Insets(20, 0, 5, 5);
        this.add(directionPanel, gbcDir);

        // 让 frame 大小根据各组件的 preferredSize 自动计算
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);


        upBtn.addActionListener(e -> {
            gamePanel.requestFocusInWindow();
            gamePanel.doMoveUp();
        });
        downBtn.addActionListener(e -> {
            gamePanel.requestFocusInWindow();
            gamePanel.doMoveDown();
        });
        leftBtn.addActionListener(e -> {
            gamePanel.requestFocusInWindow();
            gamePanel.doMoveLeft();
        });
        rightBtn.addActionListener(e -> {
            gamePanel.requestFocusInWindow();
            gamePanel.doMoveRight();
        });
        revokeBtn.addActionListener(e -> {
            gamePanel.requestFocusInWindow();
            if (gamePanel.getSteps() - controller.getInitialSteps() <= 0) {
                // 获取当前窗口（GameFrame）
                Window parentWindow = SwingUtilities.getWindowAncestor(GameFrame.this);
                // 创建一个模态对话框
                JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(GameFrame.this), "军情有变", true);
                dialog.setLayout(new BorderLayout());
                dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
                JLabel message = new JLabel("<html><div style='text-align: center;'>无法撤回<br>背水一战！</div></html>", SwingConstants.CENTER);
                message.setFont(new Font("楷体", Font.BOLD, 20));
                dialog.add(message, BorderLayout.CENTER);
                AncientButton confirmBtn = new AncientButton("已知晓");
                confirmBtn.setFont(new Font("楷体", Font.BOLD, 16));
                confirmBtn.addActionListener(f -> {
                    dialog.dispose(); // 关闭对话框
                });

                JPanel buttonPanel = new JPanel();
                buttonPanel.add(confirmBtn);
                dialog.add(buttonPanel, BorderLayout.SOUTH);


                dialog.setSize(300, 180);
                dialog.setLocationRelativeTo(null); // 居中
                dialog.setVisible(true);
            }else {
                int[][] lastMapModel = gamePanel.cloneMatrix(gamePanel.getAllSteps().get(gamePanel.getSteps() - 1));
                gamePanel.clear();
                gamePanel.setGamePanel(lastMapModel);
                GameController.model_changed.resetMatrix(lastMapModel);
                gamePanel.removeLastSteps();
                gamePanel.refreshStepLabel();
            }
        });
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }


    public void upDateGame() {
        try {
            Path path = Path.of("Save/" + user.getUsername() + "/data.txt");
            List<String> lines = Files.readAllLines(path);
            for (String line : lines) {
                server.sendLine(line);
            }
            // 不发送 TRANSMISSION_END
        } catch (IOException ex) {
            ex.printStackTrace();
            server.sendLine("ERROR: " + ex.getMessage());
        }
    }

}