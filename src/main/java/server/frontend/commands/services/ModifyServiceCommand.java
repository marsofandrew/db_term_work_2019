package server.frontend.commands.services;

import io.vertx.core.json.JsonObject;
import server.backend.DBConnectorInterface;
import server.frontend.commands.ModifyCommand;

import java.sql.SQLException;

import static server.frontend.DbHelpers.createAndCheckStringVar;

public class ModifyServiceCommand extends ModifyCommand {

  public ModifyServiceCommand(DBConnectorInterface dbConnectorInterface) {
    super(dbConnectorInterface);
  }

  @Override
  protected void modify(String request, JsonObject data, DBConnectorInterface dbConnector) throws SQLException {
    super.sendRequest(dbConnector, Services.TABLE_NAME, data);
  }
}
