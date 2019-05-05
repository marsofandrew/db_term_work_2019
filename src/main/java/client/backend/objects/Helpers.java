package client.backend.objects;

import io.vertx.core.json.JsonObject;
import server.frontend.commands.Commands;

public class Helpers {

  public static boolean handleResponse(JsonObject response) {
    if (response == null || !response.fieldNames().contains(Commands.STATUS_CODE)) {
      return false;
    }
    return response.getInteger(Commands.STATUS_CODE) / 100 == 2;
  }

  public static int getIdFromResponse(JsonObject jsonObject) {
    return Integer.valueOf(jsonObject.getString("ID"));
  }

  private Helpers() {
  }
}
