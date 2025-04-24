package controller;

import model.Direction;
import model.Map;
import model.MapModel;
import view.game.BoxComponent;
import view.game.GamePanel;

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
