package server.frontend.commands.masters;

import io.vertx.core.json.JsonObject;
import server.backend.DBConnectorInterface;
import server.frontend.commands.CreateCommand;

import java.sql.SQLException;

import static server.frontend.DbHelpers.createAndCheckStringVar;
import static server.frontend.commands.masters.Masters.TABLE_NAME;

public class CreateMasterCommand extends CreateCommand {
  private static final String NAME_KEY = "NAME";

  public CreateMasterCommand(DBConnectorInterface dbConnectorInterface) {
    super(dbConnectorInterface);
  }

  protected void create(String request, JsonObject data, DBConnectorInterface dbConnectorInterface) throws SQLException {
    String name = data.getString(NAME_KEY);
    dbConnectorInterface.insert(TABLE_NAME, NAME_KEY, createAndCheckStringVar(name));
  }

}
