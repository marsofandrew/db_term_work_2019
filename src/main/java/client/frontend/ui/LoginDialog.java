package client.frontend.ui;

import com.privatejgoodies.common.base.Strings;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import oracle.jdbc.pooling.Tuple;

import javax.swing.*;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static client.backend.Connections.SERVER;
import static client.backend.objects.Helpers.handleResponse;
import static client.frontend.helpers.Helpers.reactOnResponse;

public class LoginDialog extends JDialog{
  private JTextField tfUser;
  private JTextField tfPassword;
  private JButton btnLogin;
  private JButton btnRegister;
  private JPanel panel;
  private boolean authorised;

  public LoginDialog() {
    setContentPane(panel);
    setModal(true);
    setResizable(false);
    setSize(400, 200);
    setTitle("Login");
    btnRegister.addActionListener(e -> register());
    btnLogin.addActionListener(e -> login());
    setOnCloseActions();
  }


  public boolean isAuthorised(){
    return authorised;
  }
  private void register() {
    Tuple<String, String> data = getUsernameAndPassword();
    if (data == null){
      return;
    }
    String username = data.get1();
    String password = data.get2();
    if (!checkArguments(username, "Username can't be empty or null or consists only whitespaces")) {
      return;
    }
    if (!checkArguments(password, "Password can't be null or empty")) {
      return;
    }
    String passCode = null;
    do {
      passCode = JOptionPane.showInputDialog(this, "Input pass code", "Authentication", JOptionPane.QUESTION_MESSAGE);
    } while (!checkArguments(passCode, "Pass code can't be empty or null") && passCode != null);

    if (passCode == null) {
      return;
    }
    JsonObject request = new JsonObject();
    request.put("username", username);
    request.put("password", password);
    request.put("pass_code", passCode);
    JsonObject response = SERVER.post("/user", request);
    boolean statusOk = handleResponse(response);
    if (statusOk) {
      JOptionPane.showMessageDialog(this, "You have successfully registered", "Success", JOptionPane.INFORMATION_MESSAGE);
    } else {
      reactOnResponse(statusOk, response, this);
    }
  }

  private void login() {
    Tuple<String, String> data = getUsernameAndPassword();
    if (data == null){
      return;
    }
    JsonArray response = SERVER.get(String.format("/login?username=%s&password=%s", data.get1(), data.get2()));
    authorised = handleResponse(response.getJsonObject(0));
    reactOnResponse(authorised, response.getJsonObject(0), this);
    if (authorised) {
      dispose();
    }
  }

  private boolean checkArguments(String argument, String errorMessage) {
    if (Strings.isEmpty(argument)) {
      JOptionPane.showMessageDialog(null, errorMessage, "ERROR", JOptionPane.ERROR_MESSAGE);
      return false;
    }
    return true;
  }

  private Tuple<String, String> getUsernameAndPassword() {
    String username = tfUser.getText() == null ? null : tfUser.getText().trim();
    String password = tfPassword.getText();
    if (!checkArguments(username, "Username can't be empty or null or consists only whitespaces")) {
      return null;
    }
    if (!checkArguments(password, "Password can't be null or empty")) {
      return null;
    }
    return new Tuple<>(username, password);
  }

  private void setOnCloseActions(){
    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        dispose();
      }
    });
  }
}
