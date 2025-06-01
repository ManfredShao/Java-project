package view.game;

import controller.GameController;
import controller.GameFrame;
import model.Direction;
import model.Map;
import model.MapModel;
import user.LeaderboardManager;
import user.Score;
import user.User;
import view.login.IdentitySelectFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * It is the subclass of ListenerPanel, so that it should implement those four methods: do move left, up, down ,right.
 * The class contains a grids, which is the corresponding GUI view of the matrix variable in MapMatrix.
 */
public class GamePanel extends ListenerPanel {
    private final List<BoxComponent> boxes;
    private MapModel model;
    private GameController controller;
    private JLabel stepLabel;
    private final int GRID_SIZE;
    private final float scaleFactor;
    private BoxComponent selectedBox;
    private ArrayList<int[][]> allSteps;
    private int steps;
    private Map level;

    public GamePanel(Map mapLevel,float scaleFactor) {
        this.level = mapLevel;
        this.scaleFactor = scaleFactor;

        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        Rectangle effectiveBounds = gd.getDefaultConfiguration().getBounds();
        Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(gd.getDefaultConfiguration());

        int usableWidth = effectiveBounds.width - screenInsets.left - screenInsets.right;
        int usableHeight = effectiveBounds.height - screenInsets.top - screenInsets.bottom;

        // 计算基准尺寸（考虑横竖屏切换）
        int baseSize = Math.min(usableWidth / 4, usableHeight / 5);

        // 应用系统缩放
        float screenScale = (float) gd.getDefaultConfiguration().getDefaultTransform().getScaleX();
        GRID_SIZE = (int) (baseSize / screenScale * scaleFactor);

        // 设置组件尺寸（保留边框）
        int panelWidth = 4 * GRID_SIZE + 4;
        int panelHeight = 5 * GRID_SIZE + 4;
        this.setSize(panelWidth, panelHeight);
        this.setLocation((usableWidth - panelWidth) / 2, (usableHeight - panelHeight) / 2);
        boxes = new ArrayList<>();
        this.setVisible(true);
        this.setFocusable(true);
        this.setLayout(null);
        this.selectedBox = null;
        initialGamePanel(level);
    }

    public void initialGamePanel(Map Level) {
        this.model = new MapModel(level);
        this.steps = 0;
        this.allSteps = new ArrayList<>();
        addToAllSteps(this.cloneMatrix(this.model));
        this.BuildComponent();
        this.repaint();
    }

    public void loadGamePanel(int[][] inputMap, int steps, String time, String level) {
        this.level = Map.valueOf(level);
        this.model = new MapModel(inputMap);
        this.steps = steps;
        this.setLeftTime(time);
        this.allSteps = new ArrayList<>();
        for (int i = 0; i < steps; i++) {
            addToAllSteps(this.cloneMatrix(this.model));
        }
        addToAllSteps(inputMap);
        this.BuildComponent();
        this.repaint();
    }

    public void setGamePanel(int[][] inputMap) {
        this.model = new MapModel(inputMap);
        this.BuildComponent();
        this.repaint();
    }

    public void BuildComponent() {
        //copy a map
        int[][] map = new int[model.getHeight()][model.getWidth()];
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                map[i][j] = model.getId(i, j);
            }
        }
        //build Component
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                BoxComponent box = null;
                if (map[i][j] == 1) {//卒
                    box = new BoxComponent(new Color(27, 27, 27), i, j);
                    box.setSize(GRID_SIZE, GRID_SIZE);
                    box.setImage("卒.jpg");
                    map[i][j] = 0;
                } else if (map[i][j] == 2) {//关羽
                    box = new BoxComponent(new Color(27, 27, 27), i, j);
                    box.setSize(GRID_SIZE * 2, GRID_SIZE);
                    box.setImage("关羽.jpg");
                    map[i][j] = 0;
                    map[i][j + 1] = 0;
                } else if (map[i][j] == 3) {//其他角色
                    box = new BoxComponent(new Color(27, 27, 27), i, j);
                    box.setSize(GRID_SIZE, GRID_SIZE * 2);
                    box.setImage("将军.jpg");
                    map[i][j] = 0;
                    map[i + 1][j] = 0;
                } else if (map[i][j] == 4) {//曹操
                    box = new BoxComponent(new Color(27, 27, 27), i, j);
                    box.setSize(GRID_SIZE * 2, GRID_SIZE * 2);
                    box.setImage("曹操.jpg");
                    map[i][j] = 0;
                    map[i + 1][j] = 0;
                    map[i][j + 1] = 0;
                    map[i + 1][j + 1] = 0;
                }
                if (box != null) {
                    box.setLocation(j * GRID_SIZE + 2, i * GRID_SIZE + 2);
                    boxes.add(box);
                    this.add(box);
                }
            }
        }
    }


    public void resetGame() {
        this.removeAll();
        boxes.clear();
        selectedBox = null;
        stepLabel.setText("移步: 0");
        initialGamePanel(level);
        this.setLeftTime("0");
        revalidate();
        this.repaint();
    }

    public void clear() {
        this.removeAll();
        boxes.clear();
        selectedBox = null;
    }


    @Override
    public void doMouseClick(Point point) {
        Point adjustedPoint = SwingUtilities.convertPoint(this, point, this);
        Component component = this.getComponentAt(adjustedPoint);
        if (component instanceof BoxComponent clickedComponent) {
            if (selectedBox == null) {
                selectedBox = clickedComponent;
                selectedBox.setSelected(true);
            } else if (selectedBox != clickedComponent) {
                selectedBox.setSelected(false);
                clickedComponent.setSelected(true);
                selectedBox = clickedComponent;
            } else {
                clickedComponent.setSelected(false);
                selectedBox = null;
            }
        }
    }

    public void findBox(int row, int col) {
        for (BoxComponent box : boxes) {
            if (box.getRow() == row && box.getCol() == col) {
                box.setSelected(true);
                selectedBox = box;
            }
        }
    }

    @Override
    public void doMoveRight() {
        System.out.println("Click VK_RIGHT");
        if (selectedBox != null) {
            if (GameController.doMove(selectedBox.getRow(), selectedBox.getCol(), Direction.RIGHT)) {
                this.addToAllSteps(this.cloneMatrix(GameController.model_changed));
                afterMove();
            }
        }
    }

    @Override
    public void doMoveLeft() {
        System.out.println("Click VK_LEFT");
        if (selectedBox != null) {
            if (GameController.doMove(selectedBox.getRow(), selectedBox.getCol(), Direction.LEFT)) {
                this.addToAllSteps(this.cloneMatrix(GameController.model_changed));
                afterMove();
            }
        }
    }

    @Override
    public void doMoveUp() {
        System.out.println("Click VK_Up");
        if (selectedBox != null) {
            if (GameController.doMove(selectedBox.getRow(), selectedBox.getCol(), Direction.UP)) {
                this.addToAllSteps(this.cloneMatrix(GameController.model_changed));
                afterMove();
            }
        }
    }

    @Override
    public void doMoveDown() {
        System.out.println("Click VK_DOWN");
        if (selectedBox != null) {
            if (GameController.doMove(selectedBox.getRow(), selectedBox.getCol(), Direction.DOWN)) {
                this.addToAllSteps(this.cloneMatrix(GameController.model_changed));
                afterMove();
            }
        }
    }

    public void printLastStep() {
        int[][] matrix = this.allSteps.get(this.getSteps() - 1);
        for (int[] ints : matrix) {
            for (int j = 0; j < matrix[0].length; j++) {
                System.out.print(ints[j]);
                System.out.print(" ");
            }
            System.out.println();
        }
    }

    public void printAllSteps() {
        for (int[][] allStep : this.allSteps) {
            for (int[] ints : allStep) {
                for (int k = 0; k < ints.length; k++) {
                    System.out.print(ints[k]);
                    System.out.print(" ");
                }
                System.out.println();
            }
            System.out.println();
        }
    }

    public boolean afterMove() {
        this.steps++;
        GameFrame frame = (GameFrame) SwingUtilities.getWindowAncestor(this);
        User user = frame.getUser();
        if (user.getUsername() != null && ((GameFrame) SwingUtilities.getWindowAncestor(this)).isServer()) {
            controller.saveGame(user);
            frame.upDateGame();
        }

        //排行榜
        if (user.getUsername() != null && GameController.model_changed.getId(4, 1) == 4 && GameController.model_changed.getId(4, 2) == 4) {
            LeaderboardManager.addScore(new Score(user.getUsername(), this.getSteps(), this.getLevel()));
        }

        this.stepLabel.setText(String.format("移步: %d", this.getSteps()));
        if (GameController.model_changed.getId(4, 1) == 4 && GameController.model_changed.getId(4, 2) == 4) {
            ((GameFrame) SwingUtilities.getWindowAncestor(this)).getTime().pause();

            JLabel label = new JLabel(String.format(
                    "<html>"
                            + "<div style='"
                            +   "font-family: \"KaiTi\", \"LiSu\", \"KaiTi\", cursive; "
                            +   "color: #2E1D1A; "
                            +   "font-size: 24pt; "
                            +   "text-align: center; "
                            +   "line-height: 1.6; "
                            + "'>"
                            +   "<span style='text-shadow: 1px 1px 2px #D3B17D;'>华容道尽，云开见龙</span><br>"
                            +   "巧行%d步，智破千重<br><br>"
                            +   "<span style='font-size: 16pt; color: #5C3317; letter-spacing: 1px;'>"
                            +     "⌛ 用时：%s"
                            +   "</span>"
                            + "</div>"
                            + "</html>",
                    this.getSteps(),
                    ((GameFrame) SwingUtilities.getWindowAncestor(this)).getTime().getLeftTime()
            ));

            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setBackground(new Color(250, 245, 235));  // 设置label背景颜色
            label.setOpaque(true);  // 使背景颜色生效

            JDialog dialog = new JDialog();
            dialog.setTitle("云长义释，将军可速行！");
            dialog.setBackground(new Color(250, 245, 235)); // 设置dialog背景颜色

// 创建透明图标（替换咖啡图标）
            Image emptyIcon = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
            dialog.setIconImage(emptyIcon); // 移除Java图标
            dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
            dialog.setLayout(new BorderLayout());
            dialog.add(label, BorderLayout.CENTER);

            AncientButton confirmBtn = new AncientButton("已知晓");
            confirmBtn.setFont(new Font("楷体", Font.BOLD, 16));
            confirmBtn.setBackground(new Color(250, 245, 235)); // 设置按钮背景颜色
            confirmBtn.setOpaque(true);  // 使按钮背景颜色生效
            confirmBtn.addActionListener(e -> {
                SwingUtilities.getWindowAncestor(this).dispose();
                new IdentitySelectFrame();
                dialog.dispose();
            });

            JPanel btnPanel = new JPanel();
            btnPanel.setBackground(new Color(250, 245, 235)); // 设置按钮面板的背景颜色
            btnPanel.add(confirmBtn);
            dialog.add(btnPanel, BorderLayout.SOUTH);

// 自适应大小并居中显示
            dialog.pack();
            dialog.setSize(400, 300);
            dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);
            dialog.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        }
        return true;
    }

    public void setStepLabel(JLabel stepLabel) {
        this.stepLabel = stepLabel;
    }


    public void setController(GameController controller) {
        this.controller = controller;
    }

    public BoxComponent getSelectedBox() {
        return selectedBox;
    }

    public int getGRID_SIZE() {
        return GRID_SIZE;
    }

    public ArrayList<int[][]> getAllSteps() {
        return allSteps;
    }

    public void setAllSteps(ArrayList<int[][]> allSteps) {
        this.allSteps = allSteps;
    }

    public void addToAllSteps(int[][] steps) {
        this.allSteps.add(steps);
    }

    public void removeLastSteps() {
        this.allSteps.remove(allSteps.size() - 1);
    }

    public Map getLevel() {
        return level;
    }

    public int getSteps() {
        return this.steps;
    }

    public void setLeftTime(String leftTime) {
        ((GameFrame) SwingUtilities.getWindowAncestor(this)).getTime().setLeftTime(leftTime);
    }

    public String getLeftTime() {
        return ((GameFrame) SwingUtilities.getWindowAncestor(this)).getTime().getUsedTime();
    }

    public void setLeftTime() {
        ((GameFrame) SwingUtilities.getWindowAncestor(this)).getTime().addTime();
    }

    public void refreshStepLabel() {//用于撤回
        this.steps--;
        this.stepLabel.setText(String.format("移步: %d", this.getSteps()));
    }

    public void refreshStepLabel(int steps) {//用于loadGame
        this.stepLabel.setText(String.format("移步: %d", steps));
    }

    public void moveBoxSmoothly(BoxComponent box, int targetRow, int targetCol) {
        final int startX = box.getX();
        final int startY = box.getY();
        final int endX = targetCol * GRID_SIZE + 2;
        final int endY = targetRow * GRID_SIZE + 2;

        final int duration = 70; // 动画总时长，毫秒
        final int steps = 85;     // 动画步数，越多越平滑
        final int delay = duration / steps;

        final int dx = (endX - startX) / steps;
        final int dy = (endY - startY) / steps;

        Timer timer = new Timer(delay, null);
        final int[] count = {0};

        timer.addActionListener(e -> {
            if (count[0] < steps) {
                box.setLocation(box.getX() + dx, box.getY() + dy);
                count[0]++;
            } else {
                box.setLocation(endX, endY); // 确保最后准确落点
                box.setRow(targetRow);
                box.setCol(targetCol);
                ((Timer) e.getSource()).stop();
            }
        });

        timer.start();
    }

    public int[][] cloneMatrix(MapModel model) {
        int[][] cloneMatrix = new int[model.getHeight()][model.getWidth()];
        for (int i = 0; i < model.getHeight(); i++) {
            for (int j = 0; j < model.getWidth(); j++) {
                cloneMatrix[i][j] = model.getId(i, j);
            }
        }
        return cloneMatrix;
    }

    public int[][] cloneMatrix(int[][] matrix) {
        int[][] cloneMatrix = new int[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                cloneMatrix[i][j] = matrix[i][j];
            }
        }
        return cloneMatrix;
    }
}