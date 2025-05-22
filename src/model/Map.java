package model;

import java.util.Arrays;

public enum Map {
    LEVEL_1(deepCopyMatrix(new int[][]{
            {3, 3, 1, 3},
            {3, 3, 1, 3},
            {4, 4, 0, 3},
            {4, 4, 0, 3},
            {2, 2, 1, 1}
    })),
    LEVEL_2(deepCopyMatrix(new int[][]{
            {3, 4, 4, 1},
            {3, 4, 4, 1},
            {3, 0, 1, 1},
            {3, 0, 3, 3},
            {2, 2, 3, 3}
    })),
    LEVEL_3(deepCopyMatrix(new int[][]{
            {3, 4, 4, 3},
            {3, 4, 4, 3},
            {3, 2, 2, 3},
            {3, 1, 1, 3},
            {1, 0, 0, 1}
    }));

    private final int[][] matrix;

    Map(int[][] matrix) {
        this.matrix = deepCopyMatrix(matrix);
    }

    public int[][] getMatrix() {
        return deepCopyMatrix(matrix);
    }

    private static int[][] deepCopyMatrix(int[][] original) {
        if (original == null) return null;

        int[][] copy = new int[original.length][];
        for (int i = 0; i < original.length; i++) {
            copy[i] = Arrays.copyOf(original[i], original[i].length);
        }
        return copy;
    }
}
