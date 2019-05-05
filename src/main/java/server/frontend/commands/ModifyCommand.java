package server.frontend.commands;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import oracle.jdbc.pooling.Tuple;
import server.backend.DBConnectorInterface;
import server.frontend.commands.services.Services;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static server.frontend.DbHelpers.createAndCheckStringVar;
import static server.frontend.commands.Commands.ERROR_CODE_MESSAGE;
import static server.frontend.commands.Commands.ERROR_MESSAGE;
import static server.frontend.commands.Commands.STATUS_CODE;

public abstract class ModifyCommand extends DBCommand {

  public static final String CONDITION_KEY = "condition";
  public static final String UPDATE_KEY = "update";

  public ModifyCommand(DBConnectorInterface dbConnectorInterface) {
    super(dbConnectorInterface);
  }

  public JsonArray execute(String request, JsonObject data) {
    JsonObject response = new JsonObject();

    super.dbConnectorInterface.connect();
    try {
      modify(request, data, super.dbConnectorInterface);
      response.put(STATUS_CODE, Commands.STATUS_CODE_SUCCESS);
    } catch (SQLException e) {
      response.put(STATUS_CODE, Commands.STATUS_CODE_FAIL);
      response.put(ERROR_CODE_MESSAGE, e.getErrorCode());
      response.put(ERROR_MESSAGE, e.getMessage());
    } catch (Exception e) {
      response.put(STATUS_CODE, Commands.STATUS_CODE_ERROR);
      response.put(ERROR_MESSAGE, e.getMessage());
    } finally {
      super.dbConnectorInterface.disconnect();
    }

    return new JsonArray().add(response);
  }

  protected abstract void modify(String request, JsonObject data, DBConnectorInterface dbConnector) throws SQLException;

  protected void sendRequest(DBConnectorInterface dbConnector, String table, JsonObject data) throws SQLException {
    List<String> values = new ArrayList<>();
    JsonObject updateValues = data.getJsonObject(UPDATE_KEY);
    for (String key : updateValues.fieldNames()) {
      Object value = updateValues.getValue(key);
      if (value instanceof String) {
        values.add(createAndCheckStringVar((String) value));
      } else {
        values.add(value.toString());
      }
    }
    dbConnector.update(table,
        updateValues.fieldNames().toArray(new String[0]),
        values.toArray(new String[data.fieldNames().size()]),
        data.getString(CONDITION_KEY));
  }
}
