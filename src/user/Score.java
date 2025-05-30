package user;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Score implements Comparable<Score> {
    String playerName;
    int steps;
    private final LocalDateTime date; // 添加时间记录

    public Score(String name, int steps) {
        this.playerName = name;
        this.steps = steps;
        this.date = LocalDateTime.now();
    }

    @Override
    public int compareTo(Score other) {
        return Integer.compare(this.steps, other.steps); // 步数越小越靠前
    }

    @Override
    public String toString() {
        return String.format("%s - %d steps (on %s)",
                playerName, steps,
                date.format(DateTimeFormatter.ISO_LOCAL_DATE));
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getSteps() {
        return steps;
    }

    public LocalDateTime getDate() {
        return date;
    }
}
