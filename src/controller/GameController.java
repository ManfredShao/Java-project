package controller;

import model.Direction;
import model.Map;
import model.MapModel;
import user.User;
import view.game.BoxComponent;
import view.game.GamePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.List;
import javax.swing.Timer;

import static model.Direction.LEFT;

public class GameController extends Component {
    public static GamePanel view;
    public static MapModel model_changed;
    private Map level;

    public GameController(GamePanel view, Map mapLevel) {
        this.view = view;
        this.level = mapLevel;
        model_changed = new MapModel(level);
        view.setController(this);

        SwingUtilities.invokeLater(() -> {
            JLabel content = new JLabel("<html><div style='" + "font-family: \"楷体\",\"华文楷体\",serif;" + "text-align: center;" + "color: #5C3317;" + "font-size: 14pt;" + "'>" + "<p>建安十三年冬，曹公兵败赤壁</p>" + "<p>率残部经华容道遁走</p>" + "<p>今关云长镇守要隘，持青龙偃月刀立雪相候</p>" + "<p>■ 红袍为云长，当引其让路</p>" + "<p>■ 绿甲乃孟德，需助其脱困</p>" + "<p>■ 黄巾乃士卒，可纵横驱驰</p>" + "<p>■ 蓝衣乃将领，如子龙守关</p>" + "</div></html>");

            JLabel titleLabel = new JLabel("<html><div style='color:#8B0000; font-size:18pt;'>漢末華容道</div></html>", SwingConstants.CENTER);

            JDialog dialog = new JDialog();
            dialog.setTitle(""); // 清空默认标题
            dialog.getContentPane().setLayout(new BorderLayout(10, 10));
            dialog.getContentPane().add(titleLabel, BorderLayout.NORTH);
            dialog.getContentPane().add(content, BorderLayout.CENTER);

            AncientButton confirmBtn = new AncientButton("领命出征");
            confirmBtn.addActionListener(e -> dialog.dispose());
            JPanel btnPanel = new JPanel();
            btnPanel.add(confirmBtn);
            dialog.getContentPane().add(btnPanel, BorderLayout.SOUTH);

            //  移除java图标
            dialog.setIconImage(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB));
            dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
            dialog.pack();
            dialog.setLocationRelativeTo(view);
            dialog.setVisible(true);
        });
    }

    public void restartGame() {
        model_changed.resetMatrix(level);
        this.view.resetGame();
        this.view.requestFocus();
        System.out.println("restartGame");
    }

    public static boolean doMove(int row, int col, Direction direction) {
        int nextRow = row + direction.getRow();
        int nextCol = col + direction.getCol();
        if (model_changed.getId(row, col) == 1) {//卒1*1
            if (model_changed.checkInHeightSize(nextRow) && model_changed.checkInWidthSize(nextCol)) {//不越界
                if (model_changed.getId(nextRow, nextCol) == 0) {//不碰撞
                    model_changed.setMatrix(row, col, 0);//能移动，将原位置更新为空
                    model_changed.setMatrix(nextRow, nextCol, 1);//更新新位置
                    refreshCoordinate(nextRow, nextCol, 1, 1, view);
                    return true;
                }
            }
        }

        if (model_changed.getId(row, col) == 2) {//关羽1*2
            if (model_changed.checkInHeightSize(nextRow) && model_changed.checkInWidthSize(nextCol) && model_changed.checkInWidthSize(nextCol + 1)) {
                if ((direction == LEFT && model_changed.getId(nextRow, nextCol) == 0) || (direction == Direction.RIGHT && model_changed.getId(nextRow, nextCol + 1) == 0) || ((direction == Direction.UP || direction == Direction.DOWN) && model_changed.getId(nextRow, nextCol) == 0) && model_changed.getId(nextRow, nextCol + 1) == 0) {
                    model_changed.setMatrix(row, col, 0);
                    model_changed.setMatrix(row, col + 1, 0);
                    model_changed.setMatrix(nextRow, nextCol, 2);
                    model_changed.setMatrix(nextRow, nextCol + 1, 2);
                    refreshCoordinate(nextRow, nextCol, 2, 1, view);
                    return true;
                }
            }
        }
        if (model_changed.getId(row, col) == 3) {//其他角色2*1
            if (model_changed.checkInWidthSize(nextCol) && model_changed.checkInHeightSize(nextRow + 1) && model_changed.checkInHeightSize(nextRow)) {
                if ((direction == Direction.UP && model_changed.getId(nextRow, nextCol) == 0) || (direction == Direction.DOWN && model_changed.getId(nextRow + 1, nextCol) == 0) || ((direction == LEFT || direction == Direction.RIGHT) && model_changed.getId(nextRow, nextCol) == 0) && model_changed.getId(nextRow + 1, nextCol) == 0) {
                    model_changed.setMatrix(row, col, 0);
                    model_changed.setMatrix(row + 1, col, 0);
                    model_changed.setMatrix(nextRow, nextCol, 3);
                    model_changed.setMatrix(nextRow + 1, nextCol, 3);
                    refreshCoordinate(nextRow, nextCol, 1, 2, view);
                    return true;
                }
            }
        }
        if (model_changed.getId(row, col) == 4) {//曹操
            if (model_changed.checkInHeightSize(nextRow) && model_changed.checkInWidthSize(nextCol) && model_changed.checkInHeightSize(nextRow + 1) && model_changed.checkInWidthSize(nextCol + 1)) {
                if ((direction == Direction.UP && model_changed.getId(nextRow, nextCol) == 0 && model_changed.getId(nextRow, nextCol + 1) == 0) || (direction == Direction.DOWN && model_changed.getId(nextRow + 1, nextCol) == 0 && model_changed.getId(nextRow + 1, nextCol + 1) == 0) || (direction == LEFT && model_changed.getId(nextRow, nextCol) == 0 && model_changed.getId(nextRow + 1, nextCol) == 0) || (direction == Direction.RIGHT && model_changed.getId(nextRow, nextCol + 1) == 0 && model_changed.getId(nextRow + 1, nextCol + 1) == 0)) {
                    model_changed.setMatrix(row, col, 0);
                    model_changed.setMatrix(row + 1, col, 0);
                    model_changed.setMatrix(row, col + 1, 0);
                    model_changed.setMatrix(row + 1, col + 1, 0);
                    model_changed.setMatrix(nextRow, nextCol, 4);
                    model_changed.setMatrix(nextRow + 1, nextCol, 4);
                    model_changed.setMatrix(nextRow, nextCol + 1, 4);
                    model_changed.setMatrix(nextRow + 1, nextCol + 1, 4);
                    refreshCoordinate(nextRow, nextCol, 2, 2, view);
                    return true;
                }
            }
        }
        return false;
    }

    private static void refreshCoordinate(int nextRow, int nextCol, int width, int height, GamePanel view) {
        BoxComponent box = view.getSelectedBox();
        if (box == null) {
            return;
        }
        box.setRow(nextRow);//更新逻辑坐标
        box.setCol(nextCol);
        //更新像素坐标
        view.moveBoxSmoothly(box, nextRow, nextCol);
    }

    public void saveGame(User user) {
        int[][] map = model_changed.getMatrix();
        List<String> gameData = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for (int[] line : map) {
            for (int i : line) {
                sb.append(i).append(" ");
            }
            gameData.add(sb.toString());
            sb.setLength(0);//clear
        }
        gameData.add(String.valueOf(this.view.getSteps()));
        gameData.add(String.valueOf(((GameFrame) SwingUtilities.getWindowAncestor(this.view)).getTime().getUsedTime()));
        gameData.add(String.valueOf(this.view.getLevel()));
        String path = String.format("Save/%s", user.getUsername());
        try {
            Files.write(Path.of(path + "/data.txt"), gameData);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    public void loadGame(User user) throws IOException {
        int[][] map = new int[5][4];
        if (Files.notExists(Path.of("Save/" + user.getUsername() + "/data.txt"))) {
            JOptionPane.showMessageDialog(this.view, "此帐号未保存战局！", "军情有变", JOptionPane.ERROR_MESSAGE);
        } else if (checkChange(user)) {
            try {
                List<String> lines = Files.readAllLines(Path.of("Save/" + user.getUsername() + "/data.txt"));
                for (int j = 0; j < 5; j++) {
                    String s = lines.get(j).replace(" ", "");
                    for (int i = 0; i < 4; i++) {
                        map[j][i] = Integer.parseInt(s.substring(i, i + 1));
                    }
                }
                this.view.clear();
                int steps = Integer.parseInt(lines.get(lines.size() - 3));
                this.view.loadGamePanel(this.view.cloneMatrix(map), steps, lines.get(lines.size() - 2), lines.get(lines.size() - 1));
                this.view.refreshStepLabel(steps);
                model_changed.resetMatrix(map);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            // 获取当前窗口（GameFrame）
            Window parentWindow = SwingUtilities.getWindowAncestor(GameController.this);
            // 创建一个模态对话框
            JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(GameController.this), "军情有变", true);
            dialog.setLayout(new BorderLayout());
            dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
            JLabel message = new JLabel("<html><div style='text-align: center;'>战局篡改<br>有奸细！</div></html>", SwingConstants.CENTER);
            message.setFont(new Font("楷体", Font.BOLD, 20));
            dialog.add(message, BorderLayout.CENTER);
            AncientButton confirmBtn = new AncientButton("已知晓");
            confirmBtn.setFont(new Font("楷体", Font.BOLD, 16));
            confirmBtn.addActionListener(f -> {
                dialog.dispose(); // 关闭对话框
            });

            JPanel buttonPanel = new JPanel();
            buttonPanel.add(confirmBtn);
            dialog.add(buttonPanel, BorderLayout.SOUTH);


            dialog.setSize(300, 180);
            dialog.setLocationRelativeTo(null); // 居中
            dialog.setVisible(true);
        }
    }

    public void loadGame(List<String> lines) {
        int[][] map = new int[5][4];
        try {
            for (int j = 0; j < 5; j++) {
                String s = lines.get(j).replace(" ", "");
                for (int i = 0; i < 4; i++) {
                    map[j][i] = Integer.parseInt(s.substring(i, i + 1));
                }
            }
            int steps = Integer.parseInt(lines.get(lines.size() - 3));
            String difficulty = lines.get(lines.size() - 2);
            String level = lines.get(lines.size() - 1);

            this.view.clear();
            this.view.loadGamePanel(this.view.cloneMatrix(map), steps, difficulty, level);
            this.view.refreshStepLabel(steps);
            model_changed.resetMatrix(map);
            System.out.println("✅ 从服务器加载战局成功");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this.view, "⚠️ 无法从服务器加载战局数据", "数据错误", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }


    public boolean checkChange(User user) throws IOException {
        StringBuilder sb = new StringBuilder();
        List<String> lines = Files.readAllLines(Path.of("Save/" + user.getUsername() + "/data.txt"));
        for (int j = 0; j < 5; j++) {
            String s = lines.get(j).replace(" ", "");
            sb.append(s);
        }
        int[] counts = new int[10];
        for (int i = 0; i < sb.length(); i++) {
            char c = sb.charAt(i);
            counts[c - '0']++;
        }
        return counts[0] == 2 && counts[1] == 4 && counts[2] == 2 && counts[3] == 8 && counts[4] == 4;
    }
}

class GameState {
    String path;
    int[][] board;
    int cost;

    public void solvePuzzleBFS() {
        Queue<GameState> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();

        GameState start = new GameState();
        start.board = deepCopy(GameController.model_changed.getMatrix()); // 从组件提取棋盘状态
        start.path = "";

        queue.add(start);
        visited.add(serialize(start.board));

        while (!queue.isEmpty()) {
            GameState current = queue.poll();

            if (isGoal(current.board)) {
                System.out.println("找到解法(BFS)！路径：" + current.path);
                solutionPath = current.path;
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
        System.out.println("未找到解(BFS)");
    }

    public void solvePuzzleDFS() {
        // 限制最大深度，防止无限递归
        final int MAX_DEPTH = 200;
        Stack<GameState> stack = new Stack<>();
        Set<String> visited = new HashSet<>();

        GameState start = new GameState();
        start.board = deepCopy(GameController.model_changed.getMatrix());
        start.path = "";
        start.cost = 0;

        stack.push(start);
        visited.add(serialize(start.board));

        while (!stack.isEmpty()) {
            GameState current = stack.pop();

            if (isGoal(current.board)) {
                System.out.println("找到解法(DFS)！路径：" + current.path);
                solutionPath = current.path;
                return;
            }

            // 限制搜索深度
            if (current.cost >= MAX_DEPTH) {
                continue;
            }

            // 生成所有可能的下一个状态
            List<GameState> nextStates = generateNextStates(current);

            // 对下一步状态进行排序 - 优先尝试看起来更有希望的方向
            nextStates.sort((a, b) -> {
                // 简单的启发式：优先移动曹操方块
                boolean aHasCao = hasCaoCao(a.board);
                boolean bHasCao = hasCaoCao(b.board);
                if (aHasCao && !bHasCao) return -1;
                if (!aHasCao && bHasCao) return 1;
                return 0;
            });

            for (GameState nextState : nextStates) {
                String hash = serialize(nextState.board);
                if (!visited.contains(hash)) {
                    nextState.cost = current.cost + 1;
                    visited.add(hash);
                    stack.push(nextState);
                }
            }
        }
        System.out.println("未找到解/在限制深度内无解(DFS)");
    }

    private boolean hasCaoCao(int[][] board) {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 4; j++) {
                if (board[i][j] == 4) {
                    return true;
                }
            }
        }
        return false;
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
                    if (newBoard != null) {
                        GameState newState = new GameState();
                        newState.board = newBoard;
                        newState.path = state.path + String.format("(%d,%d)-%s;", i, j, dir.name());
                        result.add(newState);
                    }
                }
            }
        }
        return result;
    }

    public int[][] tryMove(int[][] original, int row, int col, Direction direction) {
        int[][] board = deepCopy(original);
        int nextRow = row + direction.getRow();
        int nextCol = col + direction.getCol();
        boolean moved = false;

        if (checkInHeightSize(row) && checkInWidthSize(col) && getId(board, row, col) != 0) {
            if (getId(board, row, col) == 1) {//卒1*1
                if (checkInHeightSize(nextRow) && checkInWidthSize(nextCol)) {//不越界
                    if (getId(board, nextRow, nextCol) == 0) {//不碰撞
                        setMatrix(board, row, col, 0);//能移动，将原位置更新为空
                        setMatrix(board, nextRow, nextCol, 1);//更新新位置
                        moved = true;
                    }
                }
            }

            if (checkInWidthSize(col + 1) && checkInHeightSize(nextRow) && checkInWidthSize(nextCol) && checkInWidthSize(nextCol + 1) && getId(board, row, col) == 2 && getId(board, row, col + 1) == 2) {//关羽1*2
                if ((direction == Direction.LEFT && getId(board, nextRow, nextCol) == 0) || (direction == Direction.RIGHT && getId(board, nextRow, nextCol + 1) == 0) || ((direction == Direction.UP || direction == Direction.DOWN) && getId(board, nextRow, nextCol) == 0 && getId(board, nextRow, nextCol + 1) == 0)) {
                    setMatrix(board, row, col, 0);
                    setMatrix(board, row, col + 1, 0);
                    setMatrix(board, nextRow, nextCol, 2);
                    setMatrix(board, nextRow, nextCol + 1, 2);
                    moved = true;
                }
            }

            if (checkInHeightSize(row + 1) && checkInWidthSize(nextCol) && checkInHeightSize(nextRow + 1) && checkInHeightSize(nextRow) && getId(board, row, col) == 3 && getId(board, row + 1, col) == 3) {//其他角色2*1
                if ((direction == Direction.UP && getId(board, nextRow, nextCol) == 0) || (direction == Direction.DOWN && getId(board, nextRow + 1, nextCol) == 0) || ((direction == Direction.LEFT || direction == Direction.RIGHT) && getId(board, nextRow, nextCol) == 0 && getId(board, nextRow + 1, nextCol) == 0)) {
                    setMatrix(board, row, col, 0);
                    setMatrix(board, row + 1, col, 0);
                    setMatrix(board, nextRow, nextCol, 3);
                    setMatrix(board, nextRow + 1, nextCol, 3);
                    moved = true;
                }
            }

            if (checkInHeightSize(row + 1) && checkInWidthSize(col + 1) && checkInHeightSize(nextRow) && checkInWidthSize(nextCol) && checkInHeightSize(nextRow + 1) && checkInWidthSize(nextCol + 1) && getId(board, row, col) == 4 && getId(board, row, col + 1) == 4 && getId(board, row + 1, col) == 4 && getId(board, row + 1, col + 1) == 4) {//曹操
                if ((direction == Direction.UP && getId(board, nextRow, nextCol) == 0 && getId(board, nextRow, nextCol + 1) == 0) || (direction == Direction.DOWN && getId(board, nextRow + 1, nextCol) == 0 && getId(board, nextRow + 1, nextCol + 1) == 0) || (direction == Direction.LEFT && getId(board, nextRow, nextCol) == 0 && getId(board, nextRow + 1, nextCol) == 0) || (direction == Direction.RIGHT && getId(board, nextRow, nextCol + 1) == 0 && getId(board, nextRow + 1, nextCol + 1) == 0)) {
                    setMatrix(board, row, col, 0);
                    setMatrix(board, row + 1, col, 0);
                    setMatrix(board, row, col + 1, 0);
                    setMatrix(board, row + 1, col + 1, 0);
                    setMatrix(board, nextRow, nextCol, 4);
                    setMatrix(board, nextRow + 1, nextCol, 4);
                    setMatrix(board, nextRow, nextCol + 1, 4);
                    setMatrix(board, nextRow + 1, nextCol + 1, 4);
                    moved = true;
                }
            }
        }
        return moved ? board : null;
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

    private String solutionPath;
    private Timer timer;
    private String[] steps;
    private int currentStep;
    private int animationSpeed = 300;
    private boolean isPaused = false;

    public void startAnimation() {
        if (solutionPath != null) {
            this.steps = solutionPath.split(";");
            this.currentStep = 0;
            this.isPaused = false;

            timer = new Timer(animationSpeed, e -> {
                if (currentStep >= steps.length) {
                    stopAnimation();
                    return;
                }

                if (!isPaused) {
                    executeStep(steps[currentStep]);
                    currentStep++;
                }
            });
            timer.start();
        }
    }

    private void executeStep(String step) {
        step = step.trim();
        if (step.startsWith("(")) {
            String[] parts = step.split("-");
            String coord = parts[0].replace("(", "").replace(")", "");
            String[] xy = coord.split(",");
            int row = Integer.parseInt(xy[0]);
            int col = Integer.parseInt(xy[1]);
            Direction dir = Direction.valueOf(parts[1]);

            GameController.view.findBox(row, col);
            if (GameController.doMove(row, col, dir)) {
                GameController.view.afterMove();
            }
        }
    }

    // 暂停动画
    public void pauseAnimation() {
        isPaused = true;
    }

    // 恢复动画
    public void resumeAnimation() {
        isPaused = false;
    }

    // 停止动画（自动调用）
    private void stopAnimation() {
        if (timer != null) {
            timer.stop();
        }
    }

    // 重置动画（回到第一步）
    public void resetAnimation() {
        currentStep = 0;
    }

    // 调整播放速度（ms/步）
    public void setAnimationSpeed(int speedMs) {
        this.animationSpeed = speedMs;
        if (timer != null) {
            timer.setDelay(speedMs);
        }
    }
}
