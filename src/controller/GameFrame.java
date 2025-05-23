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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GameFrame extends JFrame {

    private GameState gameState;
    SocketServer server = new SocketServer(8888);
    private GameController controller;
    private JButton restartBtn;
    private JButton severBtn;
    private JButton clientBtn;
    private JButton revokeBtn;
    private JButton loadBtn;
    private JButton saveBtn;
    private JButton pauseBtn;
    private JButton resumeBtn;
    private JButton loginLabel;
    private JButton solveBtn;
    private JLabel userLabel;
    private CountdownTimer time = new CountdownTimer();
    private User user;
    private JLabel stepLabel;
    private GamePanel gamePanel;

    public CountdownTimer getTime() {
        return time;
    }

    public JButton getSaveBtn() {
        return saveBtn;
    }

    public JButton getLoadBtn() {
        return loadBtn;
    }

    public User getUser() {
        return user;
    }

    public GameFrame(Map level, User user) {
        super("華容道·漢末風雲");
        this.setLayout(new GridBagLayout());
        gamePanel = new GamePanel(level);
        gameState = new GameState();
        this.controller = new GameController(gamePanel, level);
        this.user = user;

        int height = 50;
        this.restartBtn = FrameUtil.createButton(this, "重整旗鼓", 80, height);
        this.loadBtn = FrameUtil.createButton(this, "讀取戰局", 80, height);
        this.saveBtn = FrameUtil.createButton(this, "寫入戰局", 80, height);
        this.solveBtn = FrameUtil.createButton(this, "神机妙算", 80, height);
        this.pauseBtn = FrameUtil.createButton(this, "凝思", 80, height);
        this.resumeBtn = FrameUtil.createButton(this, "续弈", 80, height);
        this.revokeBtn = FrameUtil.createButton(this, "撤兵", 80, height);
        this.clientBtn = FrameUtil.createButton(this, "客戶端", 80, height);
        this.severBtn = FrameUtil.createButton(this, "伺服器端", 80, height);
        this.stepLabel = FrameUtil.createJLabel(this, "佈陣開局", new Font("serif", Font.PLAIN, 22), 80, height);
        this.userLabel = FrameUtil.createJLabel(this, user.getUsername(), new Font("serif", Font.PLAIN, 22), 80, height);
        this.loginLabel = FrameUtil.createButton(this, "登錄", 80, height);
        gamePanel.setStepLabel(stepLabel);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int mapWidth = 4; // 假设地图的宽度为 4
        int mapHeight = 5; // 假设地图的高度为 5
        int gridWidth = mapWidth * gamePanel.getGRID_SIZE();
        int gridHeight = mapHeight * gamePanel.getGRID_SIZE();
        this.setSize(gridWidth + 250, gridHeight + (int) (height * 1.6));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(3, 3, 3, 3);

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
        bottomPanel.add(userLabel);
        bottomPanel.add(loginLabel);
        bottomPanel.add(restartBtn);
        bottomPanel.add(loadBtn);
        bottomPanel.add(saveBtn);
        bottomPanel.add(stepLabel);

        // 添加底部面板
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 4;
        add(bottomPanel, gbc);

        // 添加计时器
        GridBagConstraints gbcTimer = new GridBagConstraints();
        gbcTimer.gridx = 3;
        gbcTimer.gridy = 0;
        gbcTimer.gridwidth = 1;
        gbcTimer.gridheight = 1;
        gbcTimer.fill = GridBagConstraints.BOTH;
        gbcTimer.insets = new Insets(5, 5, 5, 5);  // 设置边距
        this.add(time, gbcTimer);

        // 创建新的右侧按钮面板
        JPanel rightControlPanel = new JPanel(new GridLayout(6, 1, 5, 10)); // 3行1列，垂直间距10
        rightControlPanel.add(solveBtn);
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
        gbcRightCtrl.insets = new Insets(5, 5, 5, 5);
        this.add(rightControlPanel, gbcRightCtrl);

        this.severBtn.addActionListener(e -> {
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
                JOptionPane.showMessageDialog(this, "伺服器已啟動，IP:" + server.getIp());
            } catch (SocketException ex) {
                throw new RuntimeException(ex);
            }
            gamePanel.requestFocusInWindow();
        });

        this.clientBtn.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(null, "请输入对方的IP地址：", "加入战局", JOptionPane.PLAIN_MESSAGE);
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
        this.solveBtn.addActionListener(e -> {
            gameState.solvePuzzle();
            gameState.startAnimation();
            gamePanel.requestFocusInWindow();
        });
        this.pauseBtn.addActionListener(e -> {
            gameState.pauseAnimation();
            gamePanel.requestFocusInWindow();
        });

        this.resumeBtn.addActionListener(e -> {
            gameState.resumeAnimation();
            gamePanel.requestFocusInWindow();
        });
        this.restartBtn.addActionListener(e -> {
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
            JOptionPane.showMessageDialog(this, "安營紮寨");
            gamePanel.requestFocusInWindow();
        });
        this.loginLabel.addActionListener(e -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
            time.pause();
            this.dispose();
        });

        JPanel directionPanel = new JPanel(new GridLayout(5, 3, 5, 5));// 3 行 3 列，格子间水平垂直5像素
        JButton upBtn = new JButton("↑");
        JButton downBtn = new JButton("↓");
        JButton leftBtn = new JButton("←");
        JButton rightBtn = new JButton("→");
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
        gbcDir.insets = new Insets(5, 5, 5, 5);
        this.add(directionPanel, gbcDir);

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
            if (gamePanel.getSteps() <= 0) {
                JOptionPane.showMessageDialog(this.gamePanel, "无法撤回，背水一战", "军情有变", JOptionPane.ERROR_MESSAGE);
            }
            int[][] lastMapModel = gamePanel.getAllSteps().get(gamePanel.getSteps() - 1);
            gamePanel.clear();
            gamePanel.setGamePanel(lastMapModel);
            GameController.model_changed.resetMatrix(lastMapModel);
            gamePanel.removeLastSteps();
            gamePanel.refreshStepLabel();
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
            // 不再发送 TRANSMISSION_END，因为你已经拆粘包处理了
        } catch (IOException ex) {
            ex.printStackTrace();
            server.sendLine("ERROR: " + ex.getMessage());
        }
    }

}
