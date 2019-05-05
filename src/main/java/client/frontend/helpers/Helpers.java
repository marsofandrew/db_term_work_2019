package client.frontend.helpers;

import io.vertx.core.json.JsonObject;
import server.frontend.commands.Commands;

import javax.swing.*;
import java.awt.*;

public class Helpers {

  private Helpers() {
  }

  public static void reactOnResponse(boolean isSuccess, JsonObject response, Component component) {
    if (!isSuccess) {
      JOptionPane.showMessageDialog(component,
          String.format("CODE: %d\nERROR: %s", response.getInteger(Commands.STATUS_CODE), response.getString(Commands.ERROR_MESSAGE)), "ERROR",
          JOptionPane.ERROR_MESSAGE);
    }
  }


}
