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
    private final int GRID_SIZE = 50;
    private BoxComponent selectedBox;


    public GamePanel() {
        boxes = new ArrayList<>();
        this.setVisible(true);
        this.setFocusable(true);
        this.setLayout(null);
        this.setSize(4 * GRID_SIZE + 4, 5 * GRID_SIZE + 4);
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
                if (map[i][j] == 1) {
                    box = new BoxComponent(Color.ORANGE, i, j);
                    box.setSize(GRID_SIZE, GRID_SIZE);
                    map[i][j] = 0;
                } else if (map[i][j] == 2) {
                    box = new BoxComponent(Color.PINK, i, j);
                    box.setSize(GRID_SIZE * 2, GRID_SIZE);
                    map[i][j] = 0;
                    map[i][j + 1] = 0;
                } else if (map[i][j] == 3) {
                    box = new BoxComponent(Color.BLUE, i, j);
                    box.setSize(GRID_SIZE, GRID_SIZE * 2);
                    map[i][j] = 0;
                    map[i + 1][j] = 0;
                } else if (map[i][j] == 4) {
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
        // 清空现有组件
        this.removeAll();
        boxes.clear();
        // 重置状态
        selectedBox = null;
        stepLabel.setText("Step: 0");
        // 重新初始化
        initialGame(Map.LEVEL_1);
        revalidate();
        this.repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        Border border = BorderFactory.createLineBorder(Color.DARK_GRAY, 2);
        this.setBorder(border);
    }

    @Override
    public void doMouseClick(Point point) {
        Component component = this.getComponentAt(point);
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
        this.stepLabel.setText(String.format("Step: %d", this.steps));
        if (model.getId(4, 1) == 4 && model.getId(4, 2) == 4) {
            JLabel label = new JLabel(String.format("<html><div style='" + "font-family: \"STXingkai\", \"LiSu\", \"KaiTi\", cursive; " + "color: #2E1D1A; " + "font-size: 24pt; " + "text-align: center;" + "'>" + "华容道尽<br>云开见龙<br><br>巧行%d步，智破千重" + "</div></html>", steps));
            label.setHorizontalAlignment(SwingConstants.CENTER);

            // 2. 创建透明图标（替换咖啡图标）
            Image emptyIcon = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);

            // 3. 创建自定义对话框
            JDialog dialog = new JDialog();
            dialog.setTitle("一破——卧龙出山");
            dialog.setIconImage(emptyIcon); // 移除Java图标
            dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
            dialog.setLayout(new BorderLayout());

            // 4. 设置对话框内容
            dialog.add(label, BorderLayout.CENTER);

            // 5. 添加确认按钮
            JButton confirmBtn = new JButton("已知晓");
            confirmBtn.setFont(new Font("楷体", Font.PLAIN, 16));
            confirmBtn.addActionListener(e -> dialog.dispose());

            JPanel btnPanel = new JPanel();
            btnPanel.add(confirmBtn);
            dialog.add(btnPanel, BorderLayout.SOUTH);

            // 6. 自适应大小并居中显示
            dialog.pack();
            dialog.setSize(400, 300); // 固定对话框大小
            dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);
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
}
