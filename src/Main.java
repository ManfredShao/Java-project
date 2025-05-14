import view.login.IdentitySelectFrame;
import view.login.LoginFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AudioPlayer bgmPlayer = AudioPlayer.playBgm("文明.wav");
            IdentitySelectFrame identitySelectFrame = new IdentitySelectFrame();
            identitySelectFrame.setVisible(true);
        });
    }
}
