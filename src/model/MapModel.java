package model;

/**
 * This class is to record the map of one game. For example:
 */
public class MapModel {
    int[][] matrix;

    public MapModel(Map level) {
        this.matrix = level.getMatrix();
    }

    public MapModel(int[][] matrix) {
        this.matrix = matrix;
    }

    public int getWidth() {
        return this.matrix[0].length;
    }

    public int getHeight() {
        return this.matrix.length;
    }

    public int getId(int row, int col) {
        return matrix[row][col];
    }

    public void setMatrix(int row, int col, int id) {
        this.matrix[row][col] = id;
    }

    public int[][] getMatrix() {
        return matrix;
    }

    public boolean checkInWidthSize(int col) {
        return col >= 0 && col < matrix[0].length;
    }

    public boolean checkInHeightSize(int row) {
        return row >= 0 && row < matrix.length;
    }

    public void resetMatrix(Map level) {
        this.matrix = level.getMatrix();
    }

    public void resetMatrix(int[][] matrix) {
        this.matrix = matrix;
    }
}
