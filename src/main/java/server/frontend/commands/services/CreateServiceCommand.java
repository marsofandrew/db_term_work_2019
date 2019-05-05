package server.frontend.commands.services;

import io.vertx.core.json.JsonObject;
import oracle.jdbc.pooling.Tuple;
import server.backend.DBConnectorInterface;
import server.frontend.commands.CreateCommand;

import java.sql.SQLException;

import static server.frontend.DbHelpers.createAndCheckStringVar;
import static server.frontend.commands.services.Services.COST_FOREIGN;
import static server.frontend.commands.services.Services.COST_OUR;
import static server.frontend.commands.services.Services.NAME_KEY;
import static server.frontend.commands.services.Services.TABLE_NAME;

public class CreateServiceCommand extends CreateCommand {

  public CreateServiceCommand(DBConnectorInterface dbConnectorInterface) {
    super(dbConnectorInterface);
  }

  protected void create(String request, JsonObject data, DBConnectorInterface dbConnectorInterface) throws SQLException {
    dbConnectorInterface.insert(TABLE_NAME, String.format("%s, %s, %s", NAME_KEY, COST_OUR, COST_FOREIGN),
        String.format("%s, %.2f, %.2f", createAndCheckStringVar(data.getString(NAME_KEY)), data.getDouble(COST_OUR), data.getDouble(COST_FOREIGN)));

  }
}
