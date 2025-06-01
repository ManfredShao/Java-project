package controller;

import Online.SocketClient;
import Online.SocketServer;
import model.Map;
import user.LeaderboardManager;
import user.Score;
import user.User;
import view.FrameUtil;
import view.game.CountdownTimer;
import view.game.GamePanel;
import view.login.IdentitySelectFrame;
import view.login.LoginFrame;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.SocketException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameFrame extends JFrame {

    private final GameState gameState;
    SocketServer server = new SocketServer(8888);
    private final GameController controller;
    private final AncientButton loadBtn;
    private final AncientButton saveBtn;
    private final AncientButton serverBtn;
    private final AncientButton loginBtn;

    private int drawCount = 0;
    private final Random random = new Random();

    public AncientButton getLoginBtn() {
        return loginBtn;
    }

    private final CountdownTimer time = new CountdownTimer();
    private final User user;
    private final GamePanel gamePanel;
    private boolean isServer = false;

    public boolean isServer() {
        return isServer;
    }

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

    public GameController getController() {
        return controller;
    }

    public AncientButton getServerBtn() {
        return serverBtn;
    }

    public GameFrame(Map level, User user) {
        super("華容道·漢末風雲");
        this.getContentPane().setBackground(new Color(27, 27, 27));
        this.setLayout(new GridBagLayout());
        gamePanel = new GamePanel(level, 1.2f);
        gamePanel.setBackground(new Color(27, 27, 27));
        gameState = new GameState();
        this.controller = new GameController(gamePanel, level);
        this.user = user;

        int height = 50;
        AncientButton restartBtn = FrameUtil.createButton(this, "重整旗鼓", 80, height);
        this.loadBtn = FrameUtil.createButton(this, "讀取戰局", 80, height);
        this.saveBtn = FrameUtil.createButton(this, "保存戰局", 80, height);
        this.loginBtn = FrameUtil.createButton(this, "登錄", 80, height);
        AncientButton exitBtn = FrameUtil.createButton(this, "扭轉乾坤", 80, height);
        AncientButton twoSolveBtn = FrameUtil.createButton(this, "錦囊妙計", 80, height);
        AncientButton pauseBtn = FrameUtil.createButton(this, "凝思", 80, height);
        AncientButton resumeBtn = FrameUtil.createButton(this, "续弈", 80, height);
        AncientButton rankingBtn = FrameUtil.createButton(this, "排行", 80, height);
        AncientButton aiBtn = FrameUtil.createButton(this, "AI", 80, height);
        AncientButton revokeBtn = FrameUtil.createButton(this, "撤兵", 80, height);
        AncientButton clientBtn = FrameUtil.createButton(this, "鉴棋", 80, height);
        AncientButton questionBtn = FrameUtil.createButton(this, "缓兵之计", 80, height);
        this.serverBtn = FrameUtil.createButton(this, "掌棋", 80, height);
        JLabel stepLabel = FrameUtil.createJLabel(this, "佈陣開局", new Font("楷体", Font.BOLD, 22), 80, height);
        stepLabel.setForeground(new Color(245, 222, 179));
        JLabel userLabel = FrameUtil.createJLabel(this, user.getUsername(), new Font("楷体", Font.BOLD, 22), 80, height);
        userLabel.setForeground(new Color(245, 222, 179));
        gamePanel.setStepLabel(stepLabel);

        int mapWidth = 4; // 假设地图的宽度为 4
        int mapHeight = 5; // 假设地图的高度为 5
        int gridWidth = mapWidth * gamePanel.getGRID_SIZE();
        int gridHeight = mapHeight * gamePanel.getGRID_SIZE();
        gamePanel.setPreferredSize(new Dimension(gridWidth, gridHeight));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(60, 50, 0, 20);

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
        bottomPanel.add(exitBtn);
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
        JPanel rightControlPanel = new JPanel(new GridLayout(9, 1, 5, 15)); // 3行1列，垂直间距10
        rightControlPanel.setBackground(new Color(27, 27, 27));
        rightControlPanel.add(aiBtn);
        rightControlPanel.add(twoSolveBtn);
        rightControlPanel.add(pauseBtn);
        rightControlPanel.add(resumeBtn);
        rightControlPanel.add(rankingBtn);
        rightControlPanel.add(revokeBtn);
        rightControlPanel.add(questionBtn);
        rightControlPanel.add(clientBtn);
        rightControlPanel.add(serverBtn);

        // 添加右侧控制面板
        GridBagConstraints gbcRightCtrl = new GridBagConstraints();
        gbcRightCtrl.gridx = 3;
        gbcRightCtrl.gridy = 1;  // 计时器在0行，此面板在1行
        gbcRightCtrl.gridwidth = 1;
        gbcRightCtrl.gridheight = 1;
        gbcRightCtrl.fill = GridBagConstraints.BOTH;
        gbcRightCtrl.insets = new Insets(5, 80, 20, 80);
        this.add(rightControlPanel, gbcRightCtrl);

        questionBtn.addActionListener(e -> {
            if (drawCount < 3) {
                Question[] all = Question.values();
                int randomIdx = random.nextInt(all.length);
                Question.showQuestionDialog(randomIdx, gamePanel);
                drawCount++;
            } else {
                Question.showQuestionDialog(-1, gamePanel);
            }
            gamePanel.requestFocusInWindow();
        });

        twoSolveBtn.addActionListener(e -> {
            JFrame frame = new JFrame("军师錦囊");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(320, 280);
            frame.setLocationRelativeTo(null);

            Image emptyIcon = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
            frame.setIconImage(emptyIcon);

            // 纯色背景面板
            JPanel panel = new JPanel();
            panel.setBackground(new Color(250, 245, 235));
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            JLabel label = new JLabel("錦囊有二，任擇其一");
            label.setFont(new Font("楷体", Font.BOLD, 22));
            label.setForeground(new Color(120, 0, 0));
            label.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(Box.createVerticalStrut(10));
            panel.add(label);
            panel.add(Box.createVerticalStrut(20));

            AncientButton solveBtnBFS = FrameUtil.createButton(this, "广域探骊BFS", 80, height);
            AncientButton solveBtnDFS = FrameUtil.createButton(this, "隐栈潜行DFS", 80, height);

            solveBtnBFS.setAlignmentX(Component.CENTER_ALIGNMENT);
            solveBtnDFS.setAlignmentX(Component.CENTER_ALIGNMENT);

            panel.add(solveBtnBFS);
            panel.add(Box.createVerticalStrut(15));
            panel.add(solveBtnDFS);

            solveBtnBFS.addActionListener(f -> {
                gameState.solvePuzzleBFS();
                gameState.startAnimation();
                gamePanel.requestFocusInWindow();
                frame.dispose();
            });

            solveBtnDFS.addActionListener(g -> {
                gameState.solvePuzzleDFS();
                gameState.startAnimation();
                gamePanel.requestFocusInWindow();
                frame.dispose();
            });

            frame.setContentPane(panel);
            frame.setVisible(true);
            gamePanel.requestFocusInWindow();
        });

        aiBtn.addActionListener(e -> {
            // 1. 创建非模态进度对话框（允许用户操作其他界面）
            JDialog loadingDialog = new JDialog(GameFrame.this, "AI思考中", false);
            loadingDialog.setLayout(new BorderLayout(10, 10));
            loadingDialog.setResizable(false);

            // 2. 添加装饰性面板
            JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
            contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
            contentPanel.setBackground(new Color(250, 245, 235));

            // 3. 加载提示
            JLabel loadingLabel = new JLabel(
                    "<html><center>正在运筹帷幄<br><span style='font-size:12px;color:#666;'>请稍候...</span></center></html>",
                    SwingConstants.CENTER
            );
            loadingLabel.setFont(new Font("楷体", Font.BOLD, 16));
            loadingLabel.setForeground(new Color(70, 70, 70));

            // 4. 进度条
            JProgressBar progressBar = new JProgressBar();
            progressBar.setIndeterminate(true);
            progressBar.setPreferredSize(new Dimension(200, 20));

            // 5. 取消按钮
            AncientButton cancelBtn = new AncientButton("取消");
            cancelBtn.setFont(new Font("楷体", Font.PLAIN, 14));
            cancelBtn.addActionListener(ev -> {
                AIClient.cancelCurrentRequest();
                loadingDialog.dispose();
                gamePanel.requestFocusInWindow();
            });

            // 6. 组装组件
            contentPanel.add(loadingLabel, BorderLayout.CENTER);
            contentPanel.add(progressBar, BorderLayout.SOUTH);

            JPanel btnPanel = new JPanel();
            btnPanel.setBackground(new Color(250, 245, 235));
            btnPanel.add(cancelBtn);

            loadingDialog.add(contentPanel, BorderLayout.CENTER);
            loadingDialog.add(btnPanel, BorderLayout.SOUTH);
            loadingDialog.pack();
            loadingDialog.setLocationRelativeTo(GameFrame.this);

            // 7. 显示输入对话框
            Window parentWindow = SwingUtilities.getWindowAncestor(GameFrame.this);
            JDialog inputDialog = new JDialog((Frame) parentWindow, "AI军师", true);
            inputDialog.setLayout(new BorderLayout());
            inputDialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
            inputDialog.getContentPane().setBackground(new Color(27, 27, 27));

            // 提示文字
            JLabel prompt = new JLabel("请输入您想咨询的问题：", SwingConstants.CENTER);
            prompt.setFont(new Font("楷体", Font.BOLD, 18));
            prompt.setForeground(new Color(245, 222, 179));
            prompt.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
            inputDialog.add(prompt, BorderLayout.NORTH);

            // 输入框
            JTextField question = new JTextField();
            question.setFont(new Font("楷体", Font.PLAIN, 14));
            question.setForeground(Color.WHITE);
            question.setBackground(new Color(50, 50, 50));
            question.setCaretColor(Color.WHITE);
            question.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            question.setPreferredSize(new Dimension(350, 30));

            JPanel center = new JPanel();
            center.setBackground(new Color(27, 27, 27));
            center.add(question);
            inputDialog.add(center, BorderLayout.CENTER);

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
            buttonPanel.setBackground(new Color(27, 27, 27));

            // 按钮区
            AncientButton okBtn = new AncientButton("确定");
            okBtn.setFont(new Font("楷体", Font.BOLD, 12));
            AncientButton cancelBtn2 = new AncientButton("取消");
            cancelBtn2.setFont(new Font("楷体", Font.BOLD, 12));

            buttonPanel.add(okBtn);
            buttonPanel.add(cancelBtn2);

            okBtn.addActionListener(e2 -> {
                // 获取输入框的文本并判断是否为空
                String text = question.getText().trim();
                if (!text.isEmpty()) {
                    // 8. 显示加载对话框
                    loadingDialog.setVisible(true);

                    // 9. 异步调用AI
                    AIClient.chatWithAIAsync(
                            text,
                            answer -> SwingUtilities.invokeLater(() -> {
                                loadingDialog.dispose();
                                showAIResponse(answer);
                            }),
                            error -> SwingUtilities.invokeLater(() -> {
                                loadingDialog.dispose();
                                showErrorDialog("AI军师暂时无法响应", error.getMessage());
                            })
                    );
                    inputDialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(inputDialog, "问题不能为空", "提示", JOptionPane.WARNING_MESSAGE);
                }
            });

            cancelBtn2.addActionListener(e2 -> {
                question.setText(null);
                inputDialog.dispose();
            });

            inputDialog.add(buttonPanel, BorderLayout.SOUTH);

            inputDialog.pack();
            inputDialog.setLocationRelativeTo(GameFrame.this);
            inputDialog.setVisible(true);
            gamePanel.requestFocusInWindow();
        });



// 显示错误对话框的独立方法

        rankingBtn.addActionListener(e -> {
            try {
                // 1. 创建主面板
                JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
                mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
                mainPanel.setBackground(new Color(250, 245, 235)); // 米色背景

                // 2. 创建标题
                JLabel titleLabel = new JLabel("<html><center>英雄榜<br><span style='font-size:12pt;color:#5C3317;'>华容道·群雄逐鹿</span></center></html>");
                titleLabel.setFont(new Font("楷体", Font.BOLD, 24));
                titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
                titleLabel.setForeground(new Color(120, 50, 30)); // 深棕色
                mainPanel.add(titleLabel, BorderLayout.NORTH);

                // 3. 准备表格数据
                List<Score> scores = LeaderboardManager.getScores();
                String[] columnNames = {"排名", "英雄名", "关卡", "难度", "步数", "时间"};
                Object[][] rowData = new Object[scores.size()][columnNames.length];

                // 4. 填充数据
                for (int i = 0; i < scores.size(); i++) {
                    Score score = scores.get(i);
                    rowData[i][0] = i + 1; // 排名
                    rowData[i][1] = score.getPlayerName();
                    rowData[i][2] = score.getMap().name();
                    rowData[i][3] = score.getMap().getDifficultyStars();
                    rowData[i][4] = score.getSteps();
                    rowData[i][5] = score.getDate().format(DateTimeFormatter.ofPattern("MM-dd HH:mm"));
                }

                // 5. 创建表格
                JTable table = new JTable(rowData, columnNames) {
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return false; // 禁止编辑
                    }
                };

                // 6. 表格样式设置
                table.setFont(new Font("宋体", Font.PLAIN, 14));
                table.setRowHeight(30);
                table.setGridColor(new Color(200, 180, 160));
                table.setShowHorizontalLines(true);
                table.setShowVerticalLines(false);
                table.setSelectionBackground(new Color(220, 200, 180));

                // 7. 表头样式
                JTableHeader header = table.getTableHeader();
                header.setFont(new Font("楷体", Font.BOLD, 16));
                header.setForeground(new Color(160, 80, 40));
                header.setBackground(new Color(230, 215, 195));
                header.setPreferredSize(new Dimension(header.getWidth(), 35));

                // 8. 列宽设置
                table.getColumnModel().getColumn(0).setPreferredWidth(50);  // 排名
                table.getColumnModel().getColumn(1).setPreferredWidth(100); // 名字
                table.getColumnModel().getColumn(2).setPreferredWidth(100); // 关卡
                table.getColumnModel().getColumn(3).setPreferredWidth(60);  // 难度
                table.getColumnModel().getColumn(4).setPreferredWidth(60);  // 步数
                table.getColumnModel().getColumn(5).setPreferredWidth(100); // 时间

                // 9. 单元格渲染
                DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
                centerRenderer.setHorizontalAlignment(JLabel.CENTER);
                for (int i = 0; i < table.getColumnCount(); i++) {
                    table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
                }

                // 10. 添加表格到滚动面板
                JScrollPane scrollPane = new JScrollPane(table);
                scrollPane.setBorder(BorderFactory.createLineBorder(new Color(180, 150, 120), 2));
                scrollPane.getViewport().setBackground(new Color(240, 230, 220));

                // 11. 添加装饰面板
                JPanel tablePanel = new JPanel(new BorderLayout());
                tablePanel.setBackground(new Color(240, 230, 220));
                tablePanel.add(scrollPane, BorderLayout.CENTER);
                mainPanel.add(tablePanel, BorderLayout.CENTER);

                // 12. 底部装饰
                JLabel footer = new JLabel("过关斩将显英豪，步步为营见真章");
                footer.setFont(new Font("楷体", Font.ITALIC, 14));
                footer.setHorizontalAlignment(SwingConstants.CENTER);
                footer.setForeground(new Color(150, 100, 70));
                mainPanel.add(footer, BorderLayout.SOUTH);

                // 13. 创建自定义对话框
                JDialog dialog = new JDialog();
                dialog.setTitle("华容道·英雄榜");
                dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
                dialog.setContentPane(mainPanel);
                dialog.pack();
                dialog.setSize(600, 400);
                dialog.setLocationRelativeTo(null);
                dialog.setVisible(true);

            } catch (Exception ex) {
                // 错误处理
                System.err.println("显示排行榜时出错: " + ex.getMessage());
                ex.printStackTrace();

                JOptionPane.showMessageDialog(null, "加载排行榜时出错，请检查数据文件\n" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            }
            gamePanel.requestFocusInWindow();
        });

        serverBtn.addActionListener(e -> {
            server.addConnectListener(socket -> {
                isServer = true;
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
                String msg = String.format("<html><div style='text-align:center;'>伺服器已啟動，IP: %s</div></html>", ip);
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

            Window parentWindow = SwingUtilities.getWindowAncestor(GameFrame.this);
            // 创建对话框
            JDialog inputDialog = new JDialog((Frame) parentWindow, "加入战局", true);
            inputDialog.setLayout(new BorderLayout());
            inputDialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
            inputDialog.getContentPane().setBackground(new Color(27, 27, 27));

            // 提示文字
            JLabel prompt = new JLabel("请输入对方的 IP 地址：", SwingConstants.CENTER);
            prompt.setFont(new Font("楷体", Font.BOLD, 17));
            prompt.setForeground(new Color(245, 222, 179));
            prompt.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
            inputDialog.add(prompt, BorderLayout.NORTH);

            // 输入框
            JTextField ipField = new JTextField();
            ipField.setFont(new Font("楷体", Font.PLAIN, 14));
            ipField.setForeground(Color.WHITE);
            ipField.setBackground(new Color(50, 50, 50));
            ipField.setCaretColor(Color.WHITE);
            ipField.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            ipField.setPreferredSize(new Dimension(250, 30));

            JPanel center = new JPanel();
            center.setBackground(new Color(27, 27, 27));
            center.add(ipField);
            inputDialog.add(center, BorderLayout.CENTER);

            // 按钮区
            AncientButton okBtn = new AncientButton("确定");
            okBtn.setFont(new Font("楷体", Font.BOLD, 14));
            AncientButton cancelBtn = new AncientButton("取消");
            cancelBtn.setFont(new Font("楷体", Font.BOLD, 14));

            okBtn.addActionListener(e2 -> inputDialog.dispose());
            cancelBtn.addActionListener(e2 -> {
                ipField.setText(null);
                inputDialog.dispose();
            });

            JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
            btnPanel.setBackground(new Color(27, 27, 27));
            btnPanel.add(okBtn);
            btnPanel.add(cancelBtn);
            inputDialog.add(btnPanel, BorderLayout.SOUTH);

            // 显示对话框并取得结果
            inputDialog.pack();
            inputDialog.setLocationRelativeTo(parentWindow);
            inputDialog.setVisible(true);

            // pack 之后就能通过 ipField.getText() 拿到用户输入
            String input = ipField.getText();
            if (input != null && !input.isBlank()) {
                SocketClient client = new SocketClient(input, 8888);
            } else {
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

        exitBtn.addActionListener(e -> {//退出到身份选择
            IdentitySelectFrame identitySelectFrame = new IdentitySelectFrame();
            identitySelectFrame.setVisible(true);
            time.pause();
            this.dispose();
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
                dialog.getContentPane().setBackground(new Color(250, 245, 235));
                JLabel message = new JLabel("<html><div style='text-align: center;'>无法撤回<br>背水一战！</div></html>", SwingConstants.CENTER);
                message.setForeground(new Color(120, 0, 0));
                message.setFont(new Font("楷体", Font.BOLD, 18));
                message.setOpaque(true);
                message.setBackground(new Color(250, 245, 235));
                dialog.add(message, BorderLayout.CENTER);
                AncientButton confirmBtn = new AncientButton("已知晓");
                confirmBtn.setFont(new Font("楷体", Font.BOLD, 16));
                confirmBtn.addActionListener(f -> {
                    dialog.dispose(); // 关闭对话框
                });

                JPanel buttonPanel = new JPanel();
                buttonPanel.setBackground(new Color(250, 245, 235));
                buttonPanel.add(confirmBtn);
                dialog.add(buttonPanel, BorderLayout.SOUTH);


                dialog.setSize(300, 180);
                dialog.setLocationRelativeTo(null); // 居中
                dialog.setVisible(true);
            } else {
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

    // 显示AI回复的独立方法
    // 显示AI回复的独立方法
    private void showAIResponse(String answer) {

        JDialog dialog = new JDialog(
                (Frame) SwingUtilities.getWindowAncestor(this),
                "AI回复",
                true
        );
        dialog.setLayout(new BorderLayout());
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);


        dialog.getContentPane().setBackground(new Color(250, 245, 235));

        JTextArea textArea = new JTextArea(answer);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setFont(new Font("楷体", Font.PLAIN, 16));
        textArea.setBackground(new Color(250, 245, 235)); // 文本区域背景设为 (250,245,235)
        textArea.setForeground(new Color(70, 70, 70));     // 文本颜色设为深灰

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 300));
        scrollPane.setBackground(new Color(250, 245, 235));
        scrollPane.getViewport().setBackground(new Color(250, 245, 235));

        dialog.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(250, 245, 235));
        AncientButton okButton = new AncientButton("确定");
        okButton.setFont(new Font("楷体", Font.PLAIN, 14));
        okButton.addActionListener(e -> {
            dialog.dispose();             // 点击后关闭对话框
            gamePanel.requestFocusInWindow(); // 如果需要让游戏面板继续获取焦点
        });
        buttonPanel.add(okButton);

        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
        gamePanel.requestFocusInWindow();
    }

    private void showErrorDialog(String title, String message) {
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(320, 200);
        frame.setLocationRelativeTo(null);

        Image emptyIcon = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        frame.setIconImage(emptyIcon);

        // 纯色背景面板
        JPanel panel = new JPanel();
        panel.setBackground(new Color(250, 245, 235));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel label = new JLabel(message);
        label.setFont(new Font("楷体", Font.BOLD, 14));
        label.setForeground(new Color(120,0,0));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(Box.createVerticalStrut(10));
        panel.add(label);
        panel.add(Box.createVerticalStrut(25));

        AncientButton confirmBtn = FrameUtil.createButton(frame, "已知晓", 80, 50);

        confirmBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(confirmBtn);

        confirmBtn.addActionListener(f -> {
            frame.dispose(); // 关闭对话框
        });

        frame.setContentPane(panel);
        frame.setVisible(true);
        gamePanel.requestFocusInWindow();
    }
}