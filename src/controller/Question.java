package controller;

import view.FrameUtil;
import view.game.GamePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

// todo: 将问题存储到一个 List 中，每次点击缓兵之计按钮，导入这个问题，显示这个问题的提示框（这个类中写方法）
// todo: 同时每次激发按钮事件时，需要先判断是否问题在合理的 index 之内，否则弹出警告，缓兵之计无法再用。（在 GameFrame 中实现警告对话框）

public enum Question {

    Q0("曹操的别号是？", "颍川王", "兖州牧", "刘豫州", 1),
    Q1("曹操在赤壁之战后返回魏国时的身份是？", "曹魏丞相", "曹魏大将军", "曹魏王", 2),
    Q2("“宁我负人，毋人负我”出自？", "《三国演义》", "《魏武王志》", "《曹操传》", 1),
    Q3("黄巾起义的领袖是？", "张角", "张飞", "关羽", 1),
    Q4("诸葛亮发明了什么兵器？", "木牛流马", "铁骑", "大弓", 1),
    Q5("刘备的妻子是谁？", "甄氏", "王异", "甘夫人", 3),
    Q6("谁被称为“临江王”？", "诸葛亮", "孙权", "刘备", 2),
    Q7("哪一场战役使刘备第一次建立蜀汉基业？", "官渡之战", "赤壁之战", "夷陵之战", 2),
    Q8("谁在曹操进攻许昌时“割席断交”", "袁绍", "吕布", "关羽", 3),
    Q9("“三英战吕布”中，除了关羽、张飞和刘备，还有谁曾与吕布对决？", "典韦", "王异", "赵云", 1),
    Q10("曹魏名将夏侯渊在哪场战役被斩杀？","夷陵之战","定军山之战","井陉之战",2),
    Q11("谁最早提出“联孙抗曹”？","周瑜","鲁肃","甘宁",2);

    private final String sanguoQuestion;
    private final String selection1;
    private final String selection2;
    private final String selection3;
    private final int indexOfTheCorrectBtn;

    private static final List<Question> questions = new ArrayList<>();

    static {
        questions.addAll(Arrays.asList(Question.values()));
    }

    public String getSanguoQuestion() {
        return sanguoQuestion;
    }

    public String getS1() {
        return selection1;
    }

    public String getS2() {
        return selection2;
    }

    public String getS3() {
        return selection3;
    }

    public int getIndexOfTheCorrectBtn() {
        return indexOfTheCorrectBtn;
    }

    Question(String sanguoQuestion, String s1, String s2, String s3, int indexOfTheCorrectBtn) {
        this.sanguoQuestion = sanguoQuestion;
        this.selection1 = s1;
        this.selection2 = s2;
        this.selection3 = s3;
        this.indexOfTheCorrectBtn = indexOfTheCorrectBtn;
    }

    public static void showQuestionDialog(int indexOfQuestion, GamePanel gamePanel) {
        if (indexOfQuestion < 0 || indexOfQuestion >= questions.size()) {
            JFrame frame = new JFrame("军情有变");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(320, 230);
            frame.setLocationRelativeTo(null);

            Image emptyIcon = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
            frame.setIconImage(emptyIcon);

            // 纯色背景面板
            JPanel panel = new JPanel();
            panel.setBackground(new Color(250, 245, 235));
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            JLabel label = new JLabel("锦囊用尽，背水一战");
            label.setFont(new Font("楷体", Font.BOLD, 22));
            label.setForeground(new Color(120,0,0));
            label.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(Box.createVerticalStrut(30));
            panel.add(label);
            panel.add(Box.createVerticalStrut(40));

            AncientButton confirmBtn = FrameUtil.createButton(frame, "已知晓", 80, 50);

            confirmBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

            panel.add(confirmBtn);

            confirmBtn.addActionListener(f -> {
                frame.dispose(); // 关闭对话框
            });

            frame.setContentPane(panel);
            frame.setVisible(true);
        } else {
            final JFrame frame = new JFrame("缓兵之计");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(320, 330);
            frame.setLocationRelativeTo(null);

            Image emptyIcon = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
            frame.setIconImage(emptyIcon);

            JPanel panel = new JPanel();
            panel.setBackground(new Color(250, 245, 235));
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            String questionText = questions.get(indexOfQuestion).getSanguoQuestion();

            String htmlText = "<html>"
                    + "<div style='width:200px; text-align:center;'>"
                    + questionText
                    + "</div>"
                    + "</html>";

            JLabel label = new JLabel(htmlText);
            label.setFont(new Font("楷体", Font.BOLD, 19));
            label.setForeground(new Color(120, 0, 0));
            label.setAlignmentX(Component.CENTER_ALIGNMENT);

            panel.add(Box.createVerticalStrut(10));
            panel.add(label);
            panel.add(Box.createVerticalStrut(20));

            AncientButton s1Btn = FrameUtil.createButton(frame, questions.get(indexOfQuestion).getS1(), 100, 50);
            AncientButton s2Btn = FrameUtil.createButton(frame, questions.get(indexOfQuestion).getS2(), 100, 50);
            AncientButton s3Btn = FrameUtil.createButton(frame, questions.get(indexOfQuestion).getS3(), 100, 50);

            s1Btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            s2Btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            s3Btn.setAlignmentX(Component.CENTER_ALIGNMENT);

            panel.add(s1Btn);
            panel.add(Box.createVerticalStrut(10));
            panel.add(s2Btn);
            panel.add(Box.createVerticalStrut(10));
            panel.add(s3Btn);

            if (questions.get(indexOfQuestion).getIndexOfTheCorrectBtn() == 1) {
                s1Btn.addActionListener(e -> {
                    frame.dispose();
                    gamePanel.setLeftTime();
                    createCorrectAnswerDialog();
                });
                s2Btn.addActionListener(e -> {
                    frame.dispose();
                    createWrongAnswerDialog();
                });
                s3Btn.addActionListener(e -> {
                    frame.dispose();
                    createWrongAnswerDialog();
                });
            } else if (questions.get(indexOfQuestion).getIndexOfTheCorrectBtn() == 2) {
                s1Btn.addActionListener(e -> {
                    frame.dispose();
                    createWrongAnswerDialog();
                });
                s2Btn.addActionListener(e -> {
                    frame.dispose();
                    gamePanel.setLeftTime();
                    createCorrectAnswerDialog();
                });
                s3Btn.addActionListener(e -> {
                    frame.dispose();
                    createWrongAnswerDialog();
                });
            } else {
                s1Btn.addActionListener(e -> {
                    frame.dispose();
                    createWrongAnswerDialog();
                });
                s2Btn.addActionListener(e -> {
                    frame.dispose();
                    createWrongAnswerDialog();
                });
                s3Btn.addActionListener(e -> {
                    frame.dispose();
                    gamePanel.setLeftTime();
                    createCorrectAnswerDialog();
                });
            }

            frame.setContentPane(panel);
            frame.setVisible(true);
        }
    }

    public static void createWrongAnswerDialog() {

        JFrame frame = new JFrame("军情有变");
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

        JLabel label = new JLabel("口令有误，继续行军");
        label.setFont(new Font("楷体", Font.BOLD, 20));
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

    }

    public static void createCorrectAnswerDialog() {
        JFrame frame = new JFrame("缓兵之计");
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

        JLabel label = new JLabel("口令正确，增时一刻");
        label.setFont(new Font("楷体", Font.BOLD, 20));
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


    }
}
