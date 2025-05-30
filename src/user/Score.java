package user;

import model.Map; // 导入Map枚举
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Score implements Comparable<Score> {
    private final String playerName;
    private final int steps;
    private final Map map; // 使用Map枚举代替level
    private final LocalDateTime date;

    public Score(String name, int steps, Map map) {
        this.playerName = name;
        this.steps = steps;
        this.map = map;
        this.date = LocalDateTime.now();
    }

    // Getters
    public String getPlayerName() { return playerName; }
    public int getSteps() { return steps; }
    public Map getMap() { return map; }
    public LocalDateTime getDate() { return date; }

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
                date.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    }

    public String toDisplayString() {
        return String.format("%s - %s(%s) - %d步",
                playerName,
                map.name(),
                map.getDifficultyStars(),
                steps);
    }
}
