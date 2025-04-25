package model;

import java.util.Arrays;

public enum Map {
    // 枚举常量必须位于类首部
    LEVEL_1(deepCopyMatrix(new int[][]{
            {3,1,1,3},
            {3,2,2,3},
            {1,4,4,1},
            {3,4,4,3},
            {3,0,0,3}
    }));

    // 声明字段存储二维数组
    private final int[][] matrix;

    Map(int[][] matrix) {
        // 再次防御性拷贝
        this.matrix = deepCopyMatrix(matrix);
    }

    public int[][] getMatrix() {
        // 返回深拷贝副本
        return deepCopyMatrix(matrix);
    }

    // 深拷贝工具方法
    private static int[][] deepCopyMatrix(int[][] original) {
        if (original == null) return null;

        int[][] copy = new int[original.length][];
        for (int i = 0; i < original.length; i++) {
            copy[i] = Arrays.copyOf(original[i], original[i].length);
        }
        return copy;
    }
}
