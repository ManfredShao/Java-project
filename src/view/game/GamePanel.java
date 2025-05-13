package view.game;

import controller.GameController;
import model.Direction;
import model.Map;
import model.MapModel;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * It is the subclass of ListenerPanel, so that it should implement those four methods: do move left, up, down ,right.
 * The class contains a grids, which is the corresponding GUI view of the matrix variable in MapMatrix.
 */
public class GamePanel extends ListenerPanel {
    private List<BoxComponent> boxes;
    private MapModel model;
    private GameController controller;
    private JLabel stepLabel;
    private int steps;
    private int GRID_SIZE;
    private BoxComponent selectedBox;

    public GamePanel() {
        // 获取实际可用显示区域
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        DisplayMode dm = gd.getDisplayMode();

        // 考虑多显示器场景
        Rectangle effectiveBounds = gd.getDefaultConfiguration().getBounds();
        Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(gd.getDefaultConfiguration());

        int usableWidth = effectiveBounds.width - screenInsets.left - screenInsets.right;
        int usableHeight = effectiveBounds.height - screenInsets.top - screenInsets.bottom;

        // 计算基准尺寸（考虑横竖屏切换）
        int baseSize = (int) Math.min(usableWidth / 4, usableHeight / 5);

        // 应用系统缩放（Java 9+）
        float scale = (float) gd.getDefaultConfiguration().getDefaultTransform().getScaleX();
        GRID_SIZE = (int) (baseSize / scale);

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
        initialGame(Map.LEVEL_1);
    }

    public void initialGame(Map level) {
        this.steps = 0;
        this.model = new MapModel(level);

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
                    box = new BoxComponent(new Color(69, 67, 82), i, j);
                    box.setSize(GRID_SIZE, GRID_SIZE);
                    box.setText("铁卫");
                    map[i][j] = 0;
                } else if (map[i][j] == 2) {//关羽
                    box = new BoxComponent(new Color(46, 139, 87), i, j);
                    box.setSize(GRID_SIZE * 2, GRID_SIZE);
                    box.setText("武圣");
                    map[i][j] = 0;
                    map[i][j + 1] = 0;
                } else if (map[i][j] == 3) {//其他角色
                    box = new BoxComponent(new Color(65, 105, 225), i, j);
                    box.setSize(GRID_SIZE, GRID_SIZE * 2);
                    box.setText("猛将");
                    map[i][j] = 0;
                    map[i + 1][j] = 0;
                } else if (map[i][j] == 4) {//曹操
                    box = new BoxComponent(new Color(178, 34, 34), i, j);
                    box.setSize(GRID_SIZE * 2, GRID_SIZE * 2);
                    box.setText("枭雄");
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
        this.repaint();
    }

    public void initialGame(int[][] inputMap, int steps) {
        this.steps = steps;
        int[][] map = new int[inputMap.length][inputMap[0].length];
        for (int i = 0; i < inputMap.length; i++) {
            for (int j = 0; j < inputMap[0].length; j++) {
                map[i][j] = inputMap[i][j];
            }
        }
        //build Component
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                BoxComponent box = null;
                if (map[i][j] == 1) {//卒
                    box = new BoxComponent(Color.ORANGE, i, j);
                    box.setSize(GRID_SIZE, GRID_SIZE);
                    map[i][j] = 0;
                } else if (map[i][j] == 2) {//关羽
                    box = new BoxComponent(Color.PINK, i, j);
                    box.setSize(GRID_SIZE * 2, GRID_SIZE);
                    map[i][j] = 0;
                    map[i][j + 1] = 0;
                } else if (map[i][j] == 3) {//其他角色
                    box = new BoxComponent(Color.BLUE, i, j);
                    box.setSize(GRID_SIZE, GRID_SIZE * 2);
                    map[i][j] = 0;
                    map[i + 1][j] = 0;
                } else if (map[i][j] == 4) {//曹操
                    box = new BoxComponent(Color.GREEN, i, j);
                    box.setSize(GRID_SIZE * 2, GRID_SIZE * 2);
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
        this.repaint();
    }

    public void resetGame() {
        this.removeAll();
        boxes.clear();

        selectedBox = null;
        stepLabel.setText("移步: 0");

        initialGame(Map.LEVEL_1);
        revalidate();
        this.repaint();
    }

    public void clear() {
        this.removeAll();
        boxes.clear();
        selectedBox = null;
        stepLabel.setText("移步: 0");
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

    @Override
    public void doMoveRight() {
        System.out.println("Click VK_RIGHT");
        if (selectedBox != null) {
            if (controller.doMove(selectedBox.getRow(), selectedBox.getCol(), Direction.RIGHT)) {
                afterMove();
            }
        }
    }

    @Override
    public void doMoveLeft() {
        System.out.println("Click VK_LEFT");
        if (selectedBox != null) {
            if (controller.doMove(selectedBox.getRow(), selectedBox.getCol(), Direction.LEFT)) {
                afterMove();
            }
        }
    }

    @Override
    public void doMoveUp() {
        System.out.println("Click VK_Up");
        if (selectedBox != null) {
            if (controller.doMove(selectedBox.getRow(), selectedBox.getCol(), Direction.UP)) {
                afterMove();
            }
        }
    }

    @Override
    public void doMoveDown() {
        System.out.println("Click VK_DOWN");
        if (selectedBox != null) {
            if (controller.doMove(selectedBox.getRow(), selectedBox.getCol(), Direction.DOWN)) {
                afterMove();
            }
        }
    }

    public void afterMove() {
        this.steps++;
        this.stepLabel.setText(String.format("移步: %d", this.steps));
        if (GameController.model_changed.getId(4, 1) == 4 && GameController.model_changed.getId(4, 2) == 4) {
            JLabel label = new JLabel(String.format("<html><div style='" + "font-family: \"STXingkai\", \"LiSu\", \"KaiTi\", cursive; " + "color: #2E1D1A; " + "font-size: 24pt; " + "text-align: center;" + "'>" + "华容道尽<br>云开见龙<br><br>巧行%d步，智破千重" + "</div></html>", steps));
            label.setHorizontalAlignment(SwingConstants.CENTER);
            // 2. 创建透明图标（替换咖啡图标）
            Image emptyIcon = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
            JDialog dialog = new JDialog();
            dialog.setTitle("一破：卧龙出山");
            dialog.setIconImage(emptyIcon); // 移除Java图标
            dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
            dialog.setLayout(new BorderLayout());
            dialog.add(label, BorderLayout.CENTER);

            JButton confirmBtn = new JButton("已知晓");
            confirmBtn.setFont(new Font("楷体", Font.PLAIN, 16));
            confirmBtn.addActionListener(e -> {
                controller.restartGame();
//                this.gamePanel.requestFocusInWindow();//enable key listener
            });

            JPanel btnPanel = new JPanel();
            btnPanel.add(confirmBtn);
            dialog.add(btnPanel, BorderLayout.SOUTH);

            // 自适应大小并居中显示
            dialog.pack();
            dialog.setSize(400, 300);
            dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);
            dialog.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        }
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

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public void refreshStepLabel() {
        this.stepLabel.setText(String.format("移步: %d", this.steps));
    }

    public void moveBoxSmoothly(BoxComponent box, int targetRow, int targetCol) {
        final int startX = box.getX();
        final int startY = box.getY();
        final int endX = targetCol * GRID_SIZE + 2;
        final int endY = targetRow * GRID_SIZE + 2;

        final int duration = 80; // 动画总时长，毫秒
        final int steps = 75;     // 动画步数，越多越平滑
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

}
