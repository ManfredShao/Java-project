package view.game;

import controller.GameController;
import model.MapModel;
import user.User;
import view.FrameUtil;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {

    private GameController controller;
    private JButton restartBtn;
    private JButton loadBtn;
    private JButton loginLabel;
    private JLabel userLabel;
    private User user;
    private JLabel stepLabel;
    private GamePanel gamePanel;

    public GameFrame(MapModel mapModel, User user) {
        super("華容道·漢末風雲");
        this.setLayout(new GridBagLayout());
        gamePanel = new GamePanel();
        this.controller = new GameController(gamePanel, mapModel);
        this.user = user;

        int height = 50;
        this.restartBtn = FrameUtil.createButton(this, "重整旗鼓",  80, height);
        this.loadBtn = FrameUtil.createButton(this, "讀取戰局",  80, height);
        this.stepLabel = FrameUtil.createJLabel(this, "佈陣開局", new Font("serif", Font.PLAIN, 22), 80, height);
        this.userLabel = FrameUtil.createJLabel(this, user.getUsername(), new Font("serif", Font.PLAIN, 22),80, height);
        this.loginLabel = FrameUtil.createButton(this, "登錄",  80, height);
        gamePanel.setStepLabel(stepLabel);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;
        int mapWidth = 4; // 假设地图的宽度为 4
        int mapHeight = 5; // 假设地图的高度为 5
        int gridWidth = mapWidth * gamePanel.getGRID_SIZE();
        int gridHeight = mapHeight * gamePanel.getGRID_SIZE();
        this.setSize(gridWidth+80, gridHeight + (int) (height * 1.6));
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
        bottomPanel.add(restartBtn);
        bottomPanel.add(loadBtn);
        bottomPanel.add(loginLabel);
        bottomPanel.add(stepLabel);
        bottomPanel.add(userLabel);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 4;
        add(bottomPanel, gbc);

        this.restartBtn.addActionListener(e -> {
            controller.restartGame();
            gamePanel.requestFocusInWindow();//enable key listener
        });
        this.loadBtn.addActionListener(e -> {
            String string = JOptionPane.showInputDialog(this, "Input path:");
            System.out.println(string);
            gamePanel.requestFocusInWindow();//enable key listener
        });
        //todo: add other button here
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }
}
