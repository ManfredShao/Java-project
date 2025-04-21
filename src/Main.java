import model.MapModel;
import view.game.GameFrame;
import view.login.LoginFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame(280, 280);
            loginFrame.setVisible(true);
            MapModel mapModel = new MapModel(new int[][]{
                    {3,4,4,3},
                    {3,4,4,3},
                    {1,2,2,1},
                    {3,1,1,3},
                    {3,0,0,3}
            });
            GameFrame gameFrame = new GameFrame(600, 450, mapModel);
            gameFrame.setVisible(false);
            loginFrame.setGameFrame(gameFrame);
        });
    }
}
