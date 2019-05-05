package server.frontend.commands.cars;

import io.vertx.core.json.JsonObject;
import server.backend.DBConnectorInterface;
import server.frontend.commands.ModifyCommand;

import java.sql.SQLException;

public class ModifyCarCommand extends ModifyCommand {
  public ModifyCarCommand(DBConnectorInterface dbConnectorInterface) {
    super(dbConnectorInterface);
  }

  @Override
  protected void modify(String request, JsonObject data, DBConnectorInterface dbConnector) throws SQLException {
    super.sendRequest(dbConnector, Cars.TABLE_NAME, data);
  }
}
