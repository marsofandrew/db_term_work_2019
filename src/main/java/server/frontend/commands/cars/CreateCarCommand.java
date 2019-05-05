package server.frontend.commands.cars;

import io.vertx.core.json.JsonObject;
import server.backend.DBConnectorInterface;
import server.frontend.commands.CreateCommand;

import java.sql.SQLException;

import static server.frontend.DbHelpers.createAndCheckStringVar;

public class CreateCarCommand extends CreateCommand {
  private static final String NUMBER_KEY = "NUM";
  private static final String COLOR = "COLOR";
  private static final String MARK = "MARK";
  private static final String IS_FOREIGN = "IS_FOREIGN";

  public CreateCarCommand(DBConnectorInterface dbConnectorInterface) {
    super(dbConnectorInterface);
  }

  protected void create(String request, JsonObject data, DBConnectorInterface dbConnectorInterface) throws SQLException {
      dbConnectorInterface.insert(Cars.TABLE_NAME, String.format("%s, %s, %s, %s", NUMBER_KEY, COLOR, MARK, IS_FOREIGN),
          String.format("%s, %s, %s, %d", createAndCheckStringVar(data.getString(NUMBER_KEY)),
              createAndCheckStringVar(data.getString(COLOR)),
              createAndCheckStringVar(data.getString(MARK)),
              data.getInteger(IS_FOREIGN)));

  }
}
