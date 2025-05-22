package controller;

import model.Direction;

import java.util.*;

import static controller.GameController.getModel_changed;

public class GameState {
    String path;
    int[][] board;

    public void solvePuzzle() {
        Queue<GameState> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();

        GameState start = new GameState();
        start.board = deepCopy(getModel_changed().getMatrix()); // 从组件提取棋盘状态
        start.path = "";

        queue.add(start);
        visited.add(serialize(start.board));

        while (!queue.isEmpty()) {
            GameState current = queue.poll();

            if (isGoal(current.board)) {
                System.out.println("找到解法！路径：" + current.path);
                return;
            }

            for (GameState nextState : generateNextStates(current)) {
                String hash = serialize(nextState.board);
                if (!visited.contains(hash)) {
                    visited.add(hash);
                    queue.add(nextState);
                }
            }
        }
        System.out.println("未找到解");
    }

    public String serialize(int[][] board) {
        StringBuilder sb = new StringBuilder();
        for (int[] row : board) {
            for (int cell : row) {
                sb.append(cell);
            }
        }
        return sb.toString();
    }

    public List<GameState> generateNextStates(GameState state) {
        List<GameState> result = new ArrayList<>();
        int[][] board = state.board;

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 4; j++) {
                for (Direction dir : Direction.values()) {
                    int[][] newBoard = tryMove(board, i, j, dir);
                    GameState newState = new GameState();
                    newState.board = newBoard;
                    newState.path = state.path + String.format("(%d,%d)-%s;", i, j, dir.name());
                    result.add(newState);
                }
            }
        }
        return result;
    }

    public int[][] tryMove(int[][] original, int row, int col, Direction direction) {
        int[][] board = deepCopy(original);
        int nextRow = row + direction.getRow();
        int nextCol = col + direction.getCol();
        if (checkInHeightSize(row) && checkInWidthSize(col)) {
            if (getId(board, row, col) == 1) {//卒1*1
                if (checkInHeightSize(nextRow) && checkInWidthSize(nextCol)) {//不越界
                    if (getId(board, nextRow, nextCol) == 0) {//不碰撞
                        setMatrix(board, row, col, 0);//能移动，将原位置更新为空
                        setMatrix(board, nextRow, nextCol, 1);//更新新位置
                    }
                }
            }

            if (checkInWidthSize(col + 1) && checkInHeightSize(nextRow) && checkInWidthSize(nextCol) && checkInWidthSize(nextCol + 1) && getId(board, row, col) == 2 && getId(board, row, col + 1) == 2) {//关羽1*2
                if ((direction == Direction.LEFT && getId(board, nextRow, nextCol) == 0) ||
                        (direction == Direction.RIGHT && getId(board, nextRow, nextCol + 1) == 0) ||
                        ((direction == Direction.UP || direction == Direction.DOWN) && getId(board, nextRow, nextCol) == 0 && getId(board, nextRow, nextCol + 1) == 0)) {
                    setMatrix(board, row, col, 0);
                    setMatrix(board, row, col + 1, 0);
                    setMatrix(board, nextRow, nextCol, 2);
                    setMatrix(board, nextRow, nextCol + 1, 2);
                }
            }

            if (checkInHeightSize(row + 1) && checkInWidthSize(nextCol) && checkInHeightSize(nextRow + 1) && checkInHeightSize(nextRow) && getId(board, row, col) == 3 && getId(board, row + 1, col) == 3) {//其他角色2*1
                if ((direction == Direction.UP && getId(board, nextRow, nextCol) == 0) ||
                        (direction == Direction.DOWN && getId(board, nextRow + 1, nextCol) == 0) ||
                        ((direction == Direction.LEFT || direction == Direction.RIGHT) && getId(board, nextRow, nextCol) == 0 && getId(board, nextRow + 1, nextCol) == 0)) {
                    setMatrix(board, row, col, 0);
                    setMatrix(board, row + 1, col, 0);
                    setMatrix(board, nextRow, nextCol, 3);
                    setMatrix(board, nextRow + 1, nextCol, 3);
                }
            }

            if (checkInHeightSize(row + 1) && checkInWidthSize(col + 1) && checkInHeightSize(nextRow) && checkInWidthSize(nextCol) && checkInHeightSize(nextRow + 1) && checkInWidthSize(nextCol + 1) && getId(board, row, col) == 4 && getId(board, row, col + 1) == 4 && getId(board, row + 1, col) == 4 && getId(board, row + 1, col + 1) == 4) {//曹操
                if ((direction == Direction.UP && getId(board, nextRow, nextCol) == 0 && getId(board, nextRow, nextCol + 1) == 0) ||
                        (direction == Direction.DOWN && getId(board, nextRow + 1, nextCol) == 0 && getId(board, nextRow + 1, nextCol + 1) == 0) ||
                        (direction == Direction.LEFT && getId(board, nextRow, nextCol) == 0 && getId(board, nextRow + 1, nextCol) == 0) ||
                        (direction == Direction.RIGHT && getId(board, nextRow, nextCol + 1) == 0 && getId(board, nextRow + 1, nextCol + 1) == 0)) {
                    setMatrix(board, row, col, 0);
                    setMatrix(board, row + 1, col, 0);
                    setMatrix(board, row, col + 1, 0);
                    setMatrix(board, row + 1, col + 1, 0);
                    setMatrix(board, nextRow, nextCol, 4);
                    setMatrix(board, nextRow + 1, nextCol, 4);
                    setMatrix(board, nextRow, nextCol + 1, 4);
                    setMatrix(board, nextRow + 1, nextCol + 1, 4);
                }
            }
        }

        return board;
    }

    public int[][] deepCopy(int[][] original) {
        int[][] copy = new int[5][4];
        for (int i = 0; i < 5; i++) {
            System.arraycopy(original[i], 0, copy[i], 0, 4);
        }
        return copy;
    }

    public boolean isGoal(int[][] board) {
        return board[4][1] == 4 && board[4][2] == 4;
    }

    // 下面这两个改成带参数版本，不再访问成员变量board
    public int getId(int[][] board, int row, int col) {
        return board[row][col];
    }

    public void setMatrix(int[][] board, int row, int col, int id) {
        board[row][col] = id;
    }

    public boolean checkInWidthSize(int col) {
        return col >= 0 && col < 4;
    }

    public boolean checkInHeightSize(int row) {
        return row >= 0 && row < 5;
    }
}
