package server.frontend.commands;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import server.backend.DBConnectorInterface;

import java.sql.SQLException;

import static server.frontend.commands.Commands.ERROR_CODE_MESSAGE;
import static server.frontend.commands.Commands.ERROR_MESSAGE;
import static server.frontend.commands.Commands.STATUS_CODE;

public abstract class CreateCommand extends DBCommand {


  public CreateCommand(DBConnectorInterface dbConnectorInterface) {
    super(dbConnectorInterface);
  }

  public JsonArray execute(String request, JsonObject data) {
    JsonObject response = new JsonObject();

    super.dbConnectorInterface.connect();
    try {
      create(request, data, super.dbConnectorInterface);
      response.put(STATUS_CODE, Commands.STATUS_CODE_CREATED);
    } catch (SQLException e) {
      response.put(STATUS_CODE, Commands.STATUS_CODE_FAIL);
      response.put(ERROR_CODE_MESSAGE, e.getErrorCode());
      response.put(ERROR_MESSAGE, e.getMessage());
    } catch (Exception e) {
      e.printStackTrace();
      response.put(STATUS_CODE, Commands.STATUS_CODE_ERROR);
      response.put(ERROR_MESSAGE, e.getMessage());
    } finally {
      super.dbConnectorInterface.disconnect();
    }

    return new JsonArray().add(response);
  }

  protected abstract void create(String request, JsonObject data, DBConnectorInterface dbConnectorInterface) throws SQLException;
}
