package view.game;

import model.MapModel;
//目的：将MapModel类型转换为数组类型，方便存储
public interface CloneMatrix {
    default int[][] cloneMatrix (MapModel model) {
        int[][] cloneMatrix = new int[model.getHeight()][model.getWidth()];
        for (int i = 0; i < model.getHeight(); i++) {
            for (int j = 0; j < model.getWidth(); j++) {
                cloneMatrix[i][j] = model.getId(i,j);
            }
        }
        return cloneMatrix;
    }
}
