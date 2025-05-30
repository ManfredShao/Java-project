package user;

import java.io.*;
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
        if (newScore == null || newScore.playerName == null || newScore.playerName.trim().isEmpty()) {
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
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) { // 添加数据验证
                    scores.add(new Score(parts[0], Integer.parseInt(parts[1])));
                }
            }
        } catch (IOException | NumberFormatException e) {
            // 至少记录日志
            System.err.println("加载排行榜失败: " + e.getMessage());
            // 或者初始化一个空排行榜
        }
    }

    private static void save() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH))) {
            for (Score score : scores) {
                writer.println(score.playerName + "," + score.steps);
            }
        } catch (IOException ignored) {
        }
    }
}
