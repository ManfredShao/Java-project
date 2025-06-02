package view.game;

import controller.GameFrame;
import view.login.IdentitySelectFrame;

import javax.naming.InitialContext;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CountdownTimer extends JPanel {
    private int INITIAL_COUNT = 300;
    private int count = INITIAL_COUNT;
    private final Timer timer;
    private final JLabel label;
    private int LeftTime;
    private int saveCounter = 0; // 保存计数器

    public CountdownTimer() {
        setLayout(new BorderLayout());
        label = new JLabel(formatTime(count), SwingConstants.CENTER);
        label.setForeground(new Color(245, 222, 179));
        float fontSize = 48f;
        label.setFont(label.getFont().deriveFont(fontSize));
        add(label, BorderLayout.CENTER);

        timer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                count--;
                saveCounter++;

                // 每30秒保存一次
                if (saveCounter >= 30) {
                    GameFrame gameFrame = (GameFrame) SwingUtilities.getWindowAncestor(CountdownTimer.this);
                    if (gameFrame.getController() != null && gameFrame.getUser().getUsername() != null) {
                        gameFrame.getController().saveGame(gameFrame.getUser());
                        System.out.println("游戏已自动保存");
                    }
                    saveCounter = 0; // 重置计数器
                }

                if (count >= 0) {
                    label.setText(formatTime(count));
                } else {
                    timer.stop();
                    // 获取当前窗口（GameFrame）
                    Window parentWindow = SwingUtilities.getWindowAncestor(CountdownTimer.this);
                    // 创建一个模态对话框
                    JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(CountdownTimer.this), "败走华容，无路可逃", true);
                    dialog.setLayout(new BorderLayout());
                    dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

                    JLabel message = new JLabel("<html><div style='text-align: center;'>来人，来人！<br>护卫何在？</div></html>", SwingConstants.CENTER);
                    message.setFont(new Font("楷体", Font.BOLD, 20));
                    dialog.add(message, BorderLayout.CENTER);
                    AncientButton confirmBtn = new AncientButton("已知晓");
                    confirmBtn.setFont(new Font("楷体", Font.BOLD, 16));
                    confirmBtn.addActionListener(f -> {
                        dialog.dispose(); // 关闭对话框
                        parentWindow.dispose();           // 关闭 GameFrame
                        new IdentitySelectFrame(); // 打开身份选择界面
                    });

                    JPanel buttonPanel = new JPanel();
                    buttonPanel.add(confirmBtn);
                    dialog.add(buttonPanel, BorderLayout.SOUTH);

                    dialog.setSize(300, 180);
                    dialog.setLocationRelativeTo(null); // 居中
                    dialog.setVisible(true);
                }
            }

        });
        timer.start();
    }

    private String formatTime(int seconds) {
        int m = seconds / 60;
        int s = seconds % 60;
        return String.format("%02d:%02d", m, s);
    }

    public void pause() {
        if (timer != null && timer.isRunning()) {
            timer.stop();
            LeftTime = INITIAL_COUNT - count;
        }
    }

    public void setLeftTime(String leftTime) {
        LeftTime = Integer.parseInt(leftTime);
        count = INITIAL_COUNT - LeftTime;
    }

    public String getLeftTime() {
        LeftTime = INITIAL_COUNT - count;
        int minute = LeftTime / 60;
        int second = LeftTime % 60;
        return String.format("%02d:%02d", minute, second);
    }

    public String getUsedTime() {
        LeftTime = INITIAL_COUNT - count;
        return String.valueOf(LeftTime);
    }

    public void addTime(){
        INITIAL_COUNT = INITIAL_COUNT + 60;
        count = count + 60;
    }
}