import Music.AudioPlayer;
import view.login.IdentitySelectFrame;

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
