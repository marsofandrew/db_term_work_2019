package server.frontend.commands.access;

import io.vertx.core.json.JsonObject;
import server.backend.DBConnectorInterface;
import server.frontend.commands.CreateCommand;


import java.sql.SQLException;

import static server.frontend.DbHelpers.createAndCheckStringVar;
import static server.frontend.commands.access.Helpers.PASS_CODE;
import static server.frontend.commands.access.Helpers.TABLE_NAME;
import static server.frontend.commands.access.Helpers.countHash;

public class RegisterCommand extends CreateCommand {
  public RegisterCommand(DBConnectorInterface dbConnectorInterface) {
    super(dbConnectorInterface);
  }

  @Override
  protected void create(String request, JsonObject data, DBConnectorInterface dbConnectorInterface) throws SQLException {
    String username = data.getString("username");
    String password = data.getString("password");
    String passCode = data.getString("pass_code");
    if (!PASS_CODE.equals(passCode)) {
      throw new RuntimeException("Invalid pass_code");
    }

    dbConnectorInterface.insert(TABLE_NAME, "username, password", String.format("%s, %s", createAndCheckStringVar(username), createAndCheckStringVar(countHash(password))));
  }
}
