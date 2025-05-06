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
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * It is a bridge to combine GamePanel(view) and MapMatrix(model) in one game.
 * You can design several methods about the game logic in this class.
 */
public class GameController {
    private final GamePanel view;
    public static MapModel model_changed;
    private GamePanel gamePanel;
    private int step;

    public GameController(GamePanel view, MapModel model) {
        this.view = view;
        this.model_changed = model;
        view.setController(this);

        SwingUtilities.invokeLater(() -> {
            // 1. 创建自定义对话框内容
            JLabel content = new JLabel("<html><div style='" + "font-family: \"楷体\",\"华文楷体\",serif;" + "text-align: center;" + "color: #5C3317;" + "font-size: 14pt;" + "'>" + "<p>建安十三年冬，曹公兵败赤壁</p>" + "<p>率残部经华容道遁走</p>" + "<p>今关云长镇守要隘，持青龙偃月刀立雪相候</p>" + "<p>■ 红袍为云长，当引其让路</p>" + "<p>■ 绿甲乃孟德，需助其脱困</p>" + "<p>■ 黄巾乃士卒，可纵横驱驰</p>" + "<p>■ 蓝衣乃将领，如子龙守关</p>" + "</div></html>");

            // 2. 创建自定义标题组件
            JLabel titleLabel = new JLabel("<html><div style='color:#8B0000; font-size:18pt;'>漢末華容道</div></html>", SwingConstants.CENTER);

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
        model_changed.resetMatrix(Map.LEVEL_1);
        view.resetGame();
        view.requestFocus();
        System.out.println("restartGame");
    }

    public boolean doMove(int row, int col, Direction direction) {
        int nextRow = row + direction.getRow();
        int nextCol = col + direction.getCol();
        if (model_changed.getId(row, col) == 1) {//卒1*1
            if (model_changed.checkInHeightSize(nextRow) && model_changed.checkInWidthSize(nextCol)) {//不越界
                if (model_changed.getId(nextRow, nextCol) == 0) {//不碰撞
                    model_changed.setMatrix(row, col, 0);//能移动，将原位置更新为空
                    model_changed.setMatrix(nextRow, nextCol, 1);//更新新位置
                    refreshCoordinate(nextRow, nextCol, 1, 1);
                    return true;
                }
            }
        }

        if (model_changed.getId(row, col) == 2) {//关羽1*2
            if (model_changed.checkInHeightSize(nextRow) && model_changed.checkInWidthSize(nextCol) && model_changed.checkInWidthSize(nextCol + 1)) {
                if ((direction == Direction.LEFT && model_changed.getId(nextRow, nextCol) == 0) || (direction == Direction.RIGHT && model_changed.getId(nextRow, nextCol + 1) == 0) || ((direction == Direction.UP || direction == Direction.DOWN) && model_changed.getId(nextRow, nextCol) == 0) && model_changed.getId(nextRow, nextCol + 1) == 0) {
                    model_changed.setMatrix(row, col, 0);
                    model_changed.setMatrix(row, col + 1, 0);
                    model_changed.setMatrix(nextRow, nextCol, 2);
                    model_changed.setMatrix(nextRow, nextCol + 1, 2);
                    refreshCoordinate(nextRow, nextCol, 2, 1);
                    return true;
                }
            }
        }
        if (model_changed.getId(row, col) == 3) {//其他角色2*1
            if (model_changed.checkInWidthSize(nextCol) && model_changed.checkInHeightSize(nextRow + 1) && model_changed.checkInHeightSize(nextRow)) {
                if ((direction == Direction.UP && model_changed.getId(nextRow, nextCol) == 0) || (direction == Direction.DOWN && model_changed.getId(nextRow + 1, nextCol) == 0) || ((direction == Direction.LEFT || direction == Direction.RIGHT) && model_changed.getId(nextRow, nextCol) == 0) && model_changed.getId(nextRow + 1, nextCol) == 0) {
                    model_changed.setMatrix(row, col, 0);
                    model_changed.setMatrix(row + 1, col, 0);
                    model_changed.setMatrix(nextRow, nextCol, 3);
                    model_changed.setMatrix(nextRow+1, nextCol, 3);
                    refreshCoordinate(nextRow, nextCol, 1, 2);
                    return true;
                }
            }
        }
        if (model_changed.getId(row, col) == 4) {//曹操
            if (model_changed.checkInHeightSize(nextRow) && model_changed.checkInWidthSize(nextCol) && model_changed.checkInHeightSize(nextRow + 1) && model_changed.checkInWidthSize(nextCol + 1)) {
                if ((direction == Direction.UP && model_changed.getId(nextRow, nextCol) == 0 && model_changed.getId(nextRow, nextCol + 1) == 0) || (direction == Direction.DOWN && model_changed.getId(nextRow + 1, nextCol) == 0 && model_changed.getId(nextRow + 1, nextCol + 1) == 0) || (direction == Direction.LEFT && model_changed.getId(nextRow, nextCol) == 0 && model_changed.getId(nextRow + 1, nextCol) == 0) || (direction == Direction.RIGHT && model_changed.getId(nextRow, nextCol + 1) == 0 && model_changed.getId(nextRow + 1, nextCol + 1) == 0)) {
                    model_changed.setMatrix(row, col, 0);
                    model_changed.setMatrix(row + 1, col, 0);
                    model_changed.setMatrix(row, col + 1, 0);
                    model_changed.setMatrix(row + 1, col + 1, 0);
                    model_changed.setMatrix(nextRow, nextCol, 4);
                    model_changed.setMatrix(nextRow + 1, nextCol, 4);
                    model_changed.setMatrix(nextRow, nextCol + 1, 4);
                    model_changed.setMatrix(nextRow + 1, nextCol + 1, 4);
                    refreshCoordinate(nextRow, nextCol, 2, 2);
                    return true;
                }
            }
        }
        return false;
    }

    private void refreshCoordinate(int nextRow, int nextCol, int width, int height) {
        BoxComponent box = view.getSelectedBox();
        box.setRow(nextRow);//更新逻辑坐标
        box.setCol(nextCol);
        //更新像素坐标
        box.setBounds(box.getCol() * view.getGRID_SIZE() + 2, box.getRow() * view.getGRID_SIZE() + 2, width * view.getGRID_SIZE(), height * view.getGRID_SIZE());
        box.repaint();
    }

    public void saveGame(User user) {
            step=view.getSteps();
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
            gameData.add(String.valueOf(view.getSteps()));
            String path = String.format("Save/%s", user.getUsername());
            File dir = new File(path);
            dir.mkdir();
            try {
                Files.write(Path.of(path + "/data.txt"), gameData);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


    }

    public void loadGame(User user) {
        int[][] map = new int[5][4];
        try {
            List<String> lines = Files.readAllLines(Path.of("Save/" + user.getUsername() + "/data.txt"));
            for (int j = 0; j < 5; j++) {
                String s = lines.get(j).replace(" ", "");
                for (int i = 0; i < 4; i++) {
                    map[j][i] = Integer.parseInt(s.substring(i, i + 1));
                }
            }
            view.clear();
            view.setSteps(Integer.parseInt(lines.get(lines.size() - 1)));
            view.refreshStepLabel();
            view.initialGame(map, view.getSteps());
            model_changed.resetMatrix(map);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
