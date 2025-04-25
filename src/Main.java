import model.Map;
import model.MapModel;
import view.game.GameFrame;
import view.login.LoginFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame(280, 280);
//            loginFrame.setVisible(true);
            MapModel mapModel = new MapModel(Map.LEVEL_1);
            GameFrame gameFrame = new GameFrame(mapModel);
            gameFrame.setVisible(true);
            loginFrame.setGameFrame(gameFrame);
        });
    }
}
