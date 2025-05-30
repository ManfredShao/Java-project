package model;

import java.util.Arrays;

public enum Map {
    轻骑试阵(1, "★", deepCopyMatrix(new int[][]{
            {3, 3, 1, 3},
            {3, 3, 1, 3},
            {4, 4, 0, 3},
            {4, 4, 0, 3},
            {2, 2, 1, 1}
    })),
    列阵迎敌(2, "★★", deepCopyMatrix(new int[][]{
            {3, 4, 4, 1},
            {3, 4, 4, 1},
            {3, 0, 1, 1},
            {3, 0, 3, 3},
            {2, 2, 3, 3}
    })),
    破围死战(3, "★★★", deepCopyMatrix(new int[][]{
            {3, 4, 4, 3},
            {3, 4, 4, 3},
            {3, 2, 2, 3},
            {3, 1, 1, 3},
            {1, 0, 0, 1}
    }));

    private final int difficultyLevel;
    private final String difficultyStars;
    private final int[][] matrix;

    Map(int difficultyLevel, String difficultyStars, int[][] matrix) {
        this.difficultyLevel = difficultyLevel;
        this.difficultyStars = difficultyStars;
        this.matrix = deepCopyMatrix(matrix);
    }

    // Getters
    public int getDifficultyLevel() {
        return difficultyLevel;
    }

    public String getDifficultyStars() {
        return difficultyStars;
    }

    public int[][] getMatrix() {
        return deepCopyMatrix(matrix);
    }

    private static int[][] deepCopyMatrix(int[][] original) {
        if (original == null) return null;
        return Arrays.stream(original).map(int[]::clone).toArray(int[][]::new);
    }

    // 根据名称获取枚举
    public static Map getByName(String chineseName) {
        for (Map map : values()) {
            if (map.name().equals(chineseName)) {
                return map;
            }
        }
        return 轻骑试阵; // 默认返回第一关
    }
}
