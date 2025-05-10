package view.login;

import model.Map;
import model.MapModel;
import user.User;
import view.FrameUtil;
import view.game.GameFrame;

import javax.swing.*;

public class IdentitySelectFrame extends JFrame {

    public IdentitySelectFrame () {
        this.setSize(280, 280);
        this.setTitle("身份选择");
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setLayout(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton guestBtn = FrameUtil.createButton(this, "小试牛刀", 100, 40);
        JButton playerBtn = FrameUtil.createButton(this, "挂帅亲征", 100, 40);

        guestBtn.setBounds(80, 50, 100, 40);
        playerBtn.setBounds(80, 120, 100, 40);

        add(guestBtn);
        add(playerBtn);

        guestBtn.addActionListener(e -> {
            User user = new User(null, null);
            this.setVisible(false);
            MapModel mapModel = new MapModel(Map.LEVEL_1);
            GameFrame gameFrame = new GameFrame(mapModel, user);
            gameFrame.setVisible(true);
            //直接隐藏save和load按钮，游客身份不能进行这两项操作
            gameFrame.getSaveBtn().setVisible(false);
            gameFrame.getLoadBtn().setVisible(false);
        });

        playerBtn.addActionListener(e -> {
            this.setVisible(false);
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });

    }

}
