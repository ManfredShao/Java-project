package user;

import model.Map;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LeaderboardManager {
    private static final String FILE_PATH = "Save/leaderboard.txt";
    private static final List<Score> scores = new ArrayList<>();

    // 静态初始化块，类加载时自动读取
    static {
        new File(FILE_PATH).getParentFile().mkdirs();
        load();
    }

    public static void addScore(Score newScore) {
        if (newScore == null || newScore.getPlayerName() == null || newScore.getPlayerName().trim().isEmpty()) {
            return;
        }
        System.out.println("Adding new score: " + newScore);  // 调试输出
        scores.add(newScore);
        Collections.sort(scores);
        if (scores.size() > 10) {
            scores.subList(10, scores.size()).clear();
        }
        save();

        // 调试：打印当前所有分数
        System.out.println("Current leaderboard:");
        for (Score s : scores) {
            System.out.println(s);
        }
    }

    public static List<Score> getScores() {
        return scores;
    }

    private static void load() {
        scores.clear();
        File file = new File(FILE_PATH);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    String[] parts = line.split(",", 4); // 明确分割4部分
                    if (parts.length == 4) {
                        String name = parts[0].trim();
                        int steps = Integer.parseInt(parts[1].trim());
                        Map map = Map.valueOf(parts[2].trim());
                        LocalDateTime time = LocalDateTime.parse(parts[3].trim());

                        scores.add(new Score(name, steps, map, time)); // 使用保存的时间
                    }
                } catch (Exception e) {
                    System.err.println("忽略无效记录: " + line + " 错误: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("加载排行榜失败: " + e.getMessage());
        }
    }

    private static void save() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH))) {
            for (Score score : scores) {
                writer.println(String.format("%s,%d,%s,%s",
                        score.getPlayerName(),
                        score.getSteps(),
                        score.getMap().name(),
                        score.getDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)));
            }
        } catch (IOException e) {
            System.err.println("保存排行榜失败: " + e.getMessage());
        }
    }
}
