package controller;

import model.Direction;
import model.Map;
import model.MapModel;
import view.game.BoxComponent;
import view.game.GamePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * It is a bridge to combine GamePanel(view) and MapMatrix(model) in one game.
 * You can design several methods about the game logic in this class.
 */
public class GameController {
    private final GamePanel view;
    private final MapModel model;

    public GameController(GamePanel view, MapModel model) {
        this.view = view;
        this.model = model;
        view.setController(this);

        SwingUtilities.invokeLater(() -> {
            // 1. 创建自定义对话框内容
            JLabel content = new JLabel(
                    "<html><div style='"
                            + "font-family: \"楷体\",\"华文楷体\",serif;"
                            + "text-align: center;"
                            + "color: #5C3317;"
                            + "font-size: 14pt;"
                            + "'>"
                            + "<p>建安十三年冬，曹公兵败赤壁</p>"
                            + "<p>率残部经华容道遁走</p>"
                            + "<p>今关云长镇守要隘，持青龙偃月刀立雪相候</p>"
                            + "<p>■ 红袍为云长，当引其让路</p>"
                            + "<p>■ 绿甲乃孟德，需助其脱困</p>"
                            + "<p>■ 黄巾乃士卒，可纵横驱驰</p>"
                            + "<p>■ 蓝衣乃将领，如子龙守关</p>"
                            + "</div></html>"
            );

            // 2. 创建自定义标题组件
            JLabel titleLabel = new JLabel(
                    "<html><div style='color:#8B0000; font-size:18pt;'>漢末華容道</div></html>",
                    SwingConstants.CENTER
            );

            // 3. 创建完全自定义的对话框
            JDialog dialog = new JDialog();
            dialog.setTitle(""); // 清空默认标题

            // 设置对话框内容
            dialog.getContentPane().setLayout(new BorderLayout(10, 10));
            dialog.getContentPane().add(titleLabel, BorderLayout.NORTH);
            dialog.getContentPane().add(content, BorderLayout.CENTER);

            // 添加确认按钮
            JButton confirmBtn = new JButton("领命出征");
            confirmBtn.addActionListener(e -> dialog.dispose());
            JPanel btnPanel = new JPanel();
            btnPanel.add(confirmBtn);
            dialog.getContentPane().add(btnPanel, BorderLayout.SOUTH);

            // 4. 移除图标并设置其他属性
            dialog.setIconImage(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB));
            dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
            dialog.pack();
            dialog.setLocationRelativeTo(view);
            dialog.setVisible(true);
        });
    }

    public void restartGame() {
        model.resetMatrix(Map.LEVEL_1);  // 重置模型
        view.resetGame();        // 重置视图
        view.requestFocus();     // 保持焦点在游戏面板
        System.out.println("restartGame");
    }

    public boolean doMove(int row, int col, Direction direction) {
        int nextRow = row + direction.getRow();
        int nextCol = col + direction.getCol();
        if (model.getId(row, col) == 1) {//卒1*1
            if (model.checkInHeightSize(nextRow) && model.checkInWidthSize(nextCol)) {//不越界
                if (model.getId(nextRow, nextCol) == 0) {//不碰撞
                    model.getMatrix()[row][col] = 0;//能移动，将原位置更新为空
                    model.getMatrix()[nextRow][nextCol] = 1;//更新新位置
                    refreshCoordinate(nextRow,nextCol,1,1);
                    return true;
                }
            }
        }

        //非1*1板块注意自身重叠

        if (model.getId(row, col) == 2) {//关羽1*2
            if (model.checkInHeightSize(nextRow) && model.checkInWidthSize(nextCol) && model.checkInWidthSize(nextCol + 1)) {
                if ((direction == Direction.LEFT && model.getId(nextRow, nextCol) == 0) || (direction == Direction.RIGHT && model.getId(nextRow, nextCol + 1) == 0) || ((direction == Direction.UP || direction == Direction.DOWN) && model.getId(nextRow, nextCol) == 0) && model.getId(nextRow, nextCol + 1) == 0) {
                    model.getMatrix()[row][col] = 0;
                    model.getMatrix()[row][col + 1] = 0;
                    model.getMatrix()[nextRow][nextCol] = 2;
                    model.getMatrix()[nextRow][nextCol + 1] = 2;
                    refreshCoordinate(nextRow,nextCol,2,1);
                    return true;
                }
            }
        }
        if (model.getId(row, col) == 3) {//其他角色2*1
            if (model.checkInWidthSize(nextRow) && model.checkInHeightSize(nextRow + 1) && model.checkInHeightSize(nextRow + 1)) {
                if ((direction == Direction.UP && model.getId(nextRow, nextCol) == 0) || (direction == Direction.DOWN && model.getId(nextRow + 1, nextCol) == 0) || ((direction == Direction.LEFT || direction == Direction.RIGHT) && model.getId(nextRow, nextCol) == 0) && model.getId(nextRow + 1, nextCol) == 0) {
                    model.getMatrix()[row][col] = 0;
                    model.getMatrix()[row + 1][col] = 0;
                    model.getMatrix()[nextRow][nextCol] = 3;
                    model.getMatrix()[nextRow + 1][nextCol] = 3;
                    refreshCoordinate(nextRow,nextCol,1,2);
                    return true;
                }
            }
        }
        if (model.getId(row, col) == 4) {//曹操
            if (model.checkInHeightSize(nextRow) && model.checkInWidthSize(nextCol) && model.checkInHeightSize(nextRow + 1) && model.checkInWidthSize(nextCol + 1)) {
                if ((direction == Direction.UP && model.getId(nextRow, nextCol) == 0 && model.getId(nextRow, nextCol + 1) == 0) || (direction == Direction.DOWN && model.getId(nextRow + 1, nextCol) == 0 && model.getId(nextRow + 1, nextCol + 1) == 0) || (direction == Direction.LEFT && model.getId(nextRow, nextCol) == 0 && model.getId(nextRow + 1, nextCol) == 0) || (direction == Direction.RIGHT && model.getId(nextRow, nextCol + 1) == 0 && model.getId(nextRow + 1, nextCol + 1) == 0)) {
                    model.getMatrix()[row][col] = 0;
                    model.getMatrix()[row + 1][col] = 0;
                    model.getMatrix()[row][col + 1] = 0;
                    model.getMatrix()[row + 1][col + 1] = 0;
                    model.getMatrix()[nextRow][nextCol] = 4;
                    model.getMatrix()[nextRow + 1][nextCol] = 4;
                    model.getMatrix()[nextRow][nextCol + 1] = 4;
                    model.getMatrix()[nextRow + 1][nextCol + 1] = 4;
                    refreshCoordinate(nextRow,nextCol,2,2);
                    return true;
                }
            }
        }
        return false;
    }

    private void refreshCoordinate (int nextRow, int nextCol,int width, int height) {
        BoxComponent box = view.getSelectedBox();
        box.setRow(nextRow);//更新逻辑坐标
        box.setCol(nextCol);
        //更新像素坐标
        box.setBounds(box.getCol() * view.getGRID_SIZE() + 2, box.getRow() * view.getGRID_SIZE() + 2, width * view.getGRID_SIZE(), height * view.getGRID_SIZE());
        box.repaint();
    }

    //todo: add other methods such as loadGame, saveGame...

}
