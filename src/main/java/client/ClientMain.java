package client;

import client.frontend.ui.LoginDialog;
import client.frontend.ui.MainWindow;

import javax.swing.*;
import java.awt.*;

public class ClientMain {
  public static void main(String[] args) {
    LoginDialog loginDialog = new LoginDialog();
    loginDialog.setVisible(true);
    loginDialog.dispose();
    if (!loginDialog.isAuthorised()) {
      return;
    }
    MainWindow window = new MainWindow();
    JFrame frame = new JFrame();
    frame.setContentPane(window.getMainPanel());
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.pack();
    frame.setSize(800, 600);
    frame.setMinimumSize(new Dimension(800, 600));
    frame.setTitle("Application");
    frame.setResizable(false);
    frame.setVisible(true);

  }
}
