package server.frontend.commands;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import server.backend.DBConnectorInterface;

import java.sql.SQLException;
import java.util.Arrays;

import static server.frontend.commands.Commands.ERROR_CODE_MESSAGE;
import static server.frontend.commands.Commands.ERROR_MESSAGE;
import static server.frontend.commands.Commands.STATUS_CODE;

public abstract class DeleteCommand extends DBCommand {

  public DeleteCommand(DBConnectorInterface dbConnectorInterface) {
    super(dbConnectorInterface);
  }

  @Override
  public JsonArray execute(String request, JsonObject data) {
    JsonObject response = new JsonObject();

    super.dbConnectorInterface.connect();
    try {
      delete(request, super.dbConnectorInterface);
      response.put(STATUS_CODE, Commands.STATUS_CODE_NO_CONTENT);
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

  protected void delete(String request, DBConnectorInterface dbConnector) throws SQLException {
    if (!canDelete(request, dbConnector)) {
      throw new IllegalStateException("You can't delete this object");
    }

    String[] ids = getIDs(request);
    StringBuilder conditionBuilder = new StringBuilder();
    for (String id : ids) {
      conditionBuilder.append(id + ", ");
    }
    conditionBuilder.deleteCharAt(conditionBuilder.lastIndexOf(","));
    dbConnector.delete(getTable(request), String.format("ID in (%s)", conditionBuilder.toString()));
  }

  protected abstract boolean canDelete(String request, DBConnectorInterface dbConnector);

  protected abstract String getTable(String request);

  protected abstract String[] getIDs(String request);

  protected String[] getIdsFromRequest(String request) {
    String[] params = request.split("/");
    return Arrays.copyOfRange(params, 2, params.length);
  }
}
