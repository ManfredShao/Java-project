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
        if (!file.exists()) {
            try {
                file.createNewFile();
                return;
            } catch (IOException e) {
                System.err.println("创建排行榜文件失败: " + e.getMessage());
                return;
            }
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // 调试输出
                System.out.println("Reading line: " + line);

                String[] parts = line.split(",");
                // 确保有足够的部分：玩家名,步数,地图名,时间(可选)
                if (parts.length >= 3) {
                    try {
                        String name = parts[0].trim();
                        int steps = Integer.parseInt(parts[1].trim());
                        String mapName = parts[2].trim();

                        // 查找对应的地图枚举
                        Map map = null;
                        for (Map m : Map.values()) {
                            if (m.name().equals(mapName)) {
                                map = m;
                                break;
                            }
                        }

                        if (map == null) {
                            System.err.println("未知地图名称: " + mapName);
                            map = Map.轻骑试阵; // 默认地图
                        }

                        // 处理时间（如果有）
                        LocalDateTime date = parts.length > 3 ? LocalDateTime.parse(parts[3].trim(), DateTimeFormatter.ISO_LOCAL_DATE_TIME) : LocalDateTime.now();

                        scores.add(new Score(name, steps, map));
                    } catch (Exception e) {
                        System.err.println("解析行失败: " + line + ", 错误: " + e.getMessage());
                    }
                } else {
                    System.err.println("忽略格式不正确的行: " + line);
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
