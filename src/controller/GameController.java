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
        int boxId = model.getId(row, col);
        int boxHeight = 0;
        int boxWidth = 0;
        switch (boxId) {
            case 1:
                boxHeight = 1;
                boxWidth = 1;
                break;
            case 2:
                boxHeight = 1;
                boxWidth = 2;
                break;
            case 3:
                boxHeight = 2;
                boxWidth = 1;
                break;
            case 4:
                boxHeight = 2;
                boxWidth = 2;
                break;
            default:
                break;
        }
        //下面检查是否出现碰撞或者越界
        int nextRow = row + direction.getRow();
        int nextCol = col + direction.getCol();
        for (int i = 0; i < boxHeight; i++){
            for (int j = 0; j < boxWidth; j++){
                int idOfRowToBeChecked = i + nextRow;
                int idOfColToBeChecked = j + nextCol;
                //检查越界
                if (!model.checkInHeightSize(idOfRowToBeChecked) || !model.checkInWidthSize(idOfColToBeChecked)){
                    return false;
                }
                //检查碰撞
                if (model.getId(idOfRowToBeChecked,idOfColToBeChecked) != 0){
                    return false;
                }
            }
        }
        //剩下的情况返回true
        BoxComponent box = view.getSelectedBox();
        box.setRow(nextRow);
        box.setCol(nextCol);
        box.setLocation(box.getCol() * view.getGRID_SIZE() + 2, box.getRow() * view.getGRID_SIZE() + 2);
        box.repaint();
        //重置参数，将原来的位置ID设为0，移动后的位置设置对应的iD。
        for (int i = 0; i < boxHeight; i++) {
            for (int j = 0; j < boxWidth; j++) {
                model.getMatrix()[row + i][col + j] = 0;
                model.getMatrix()[nextRow + i][nextCol + j] = boxId;

            }
        }
        return true;
    }

    //todo: add other methods such as loadGame, saveGame...

}
