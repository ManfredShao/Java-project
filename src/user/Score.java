package user;

import model.Map; // 导入Map枚举
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Score implements Comparable<Score> {
    private final String playerName;
    private final int steps;
    private final Map map; // 使用Map枚举代替level
    private final LocalDateTime completionTime;

    public Score(String playerName, int steps, Map map, LocalDateTime completionTime) {
        this.playerName = playerName;
        this.steps = steps;
        this.map = map;
        this.completionTime = completionTime;
    }

    // 保留旧构造函数（自动记录当前时间）
    public Score(String playerName, int steps, Map map) {
        this(playerName, steps, map, LocalDateTime.now());
    }
    // Getters
    public String getPlayerName() { return playerName; }
    public int getSteps() { return steps; }
    public Map getMap() { return map; }
    public LocalDateTime getDate() {
        return completionTime;
    }
    @Override
    public int compareTo(Score other) {
        // 先按关卡难度降序，再按步数升序
        int levelCompare = Integer.compare(other.map.getDifficultyLevel(), this.map.getDifficultyLevel());
        if (levelCompare != 0) return levelCompare;
        return Integer.compare(this.steps, other.steps);
    }

    @Override
    public String toString() {
        return String.format("%s,%d,%s,%s",
                playerName,
                steps,
                map.name(), // 保存枚举名称
                completionTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    }

    public String toDisplayString() {
        return String.format("%s - %s(%s) - %d步",
                playerName,
                map.name(),
                map.getDifficultyStars(),
                steps);
    }
}
