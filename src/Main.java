import view.login.IdentitySelectFrame;
import view.login.LoginFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            IdentitySelectFrame identitySelectFrame = new IdentitySelectFrame();
            identitySelectFrame.setVisible(true);
        });
    }
}
