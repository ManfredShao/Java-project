package view.game;

import view.login.IdentitySelectFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CountdownTimer extends JPanel {
    private final int INITIAL_COUNT = 600;
    private int count = INITIAL_COUNT;
    private Timer timer;
    private JLabel label;
    private float fontSize = 48f;
    private int LeftTime;

    public CountdownTimer() {
        setLayout(new BorderLayout());
        label = new JLabel(formatTime(count), SwingConstants.CENTER);
        label.setFont(label.getFont().deriveFont(fontSize));
        add(label, BorderLayout.CENTER);

        timer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                count--;
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
                    message.setFont(new Font("宋体", Font.PLAIN, 20));
                    dialog.add(message, BorderLayout.CENTER);

                    JButton confirmBtn = new JButton("已知晓");
                    confirmBtn.setFont(new Font("宋体", Font.BOLD, 16));
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
            LeftTime = INITIAL_COUNT-count;
        }
    }

    public void setLeftTime(String leftTime) {
        LeftTime = Integer.parseInt(leftTime);
        count = INITIAL_COUNT-LeftTime;
    }

    public String getLeftTime() {
        LeftTime = INITIAL_COUNT-count;
        int minute = LeftTime / 60;
        int second = LeftTime % 60;
        return String.format("%02d:%02d", minute,second);
    }
    public String getUsedTime(){
        LeftTime = INITIAL_COUNT-count;
        return String.valueOf(LeftTime);
    }
}