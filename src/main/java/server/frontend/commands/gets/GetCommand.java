package server.frontend.commands.gets;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import oracle.jdbc.pooling.Tuple;
import server.backend.DBConnectorInterface;
import server.frontend.commands.Commands;
import server.frontend.commands.DBCommand;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static server.frontend.commands.Commands.ERROR_CODE_MESSAGE;
import static server.frontend.commands.Commands.ERROR_MESSAGE;
import static server.frontend.commands.Commands.STATUS_CODE;
import static server.frontend.commands.gets.Helpers.divideQueryPart;
import static server.frontend.commands.gets.Helpers.getQueryParts;

public class GetCommand extends DBCommand {
  public static final String COLUMNS = "columns";
  public static final String TABLE = "table";
  public static final String OTHER = "other";

  public GetCommand(DBConnectorInterface dbConnectorInterface) {
    super(dbConnectorInterface);
  }

  @Override
  public JsonArray execute(String request, JsonObject data) {
    JsonArray response = new JsonArray();


    dbConnectorInterface.connect();
    try {
      List<JsonObject> dbResponse = getDbResponse(request, data);
      response.add(new JsonObject().put(STATUS_CODE, Commands.STATUS_CODE_SUCCESS));
      for (JsonObject object : dbResponse) {
        response.add(object);
      }
    } catch (SQLException e) {
      JsonObject error = new JsonObject();
      error.put(STATUS_CODE, Commands.STATUS_CODE_FAIL);
      error.put(ERROR_CODE_MESSAGE, e.getErrorCode());
      error.put(ERROR_MESSAGE, e.getMessage());
      response.add(error);
    } catch (Exception e) {
      JsonObject error = new JsonObject();
      error.put(STATUS_CODE, Commands.STATUS_CODE_ERROR);
      error.put(ERROR_MESSAGE, e.getMessage());
      response.add(error);
    } finally {
      dbConnectorInterface.disconnect();
    }


    return response;
  }

  protected List<JsonObject> getDbResponse(String request, JsonObject data) throws SQLException {
    Map<String, String> params = getParams(request);
    return dbConnectorInterface.select(params.get(COLUMNS), params.get(TABLE), params.get(OTHER));
  }

  private Map<String, String> getParams(String request) {
    Map<String, String> params = new HashMap<>();
    String[] parts = getQueryParts(request);
    for (String part : parts) {
      Tuple<String, String> element = divideQueryPart(part);
      switch (element.get1().toLowerCase()) {
        case "select": {
          params.put(COLUMNS, element.get2());
          break;
        }
        case "from": {
          params.put(TABLE, element.get2());
          break;
        }
        case OTHER: {
          params.put(OTHER, element.get2());
          break;
        }
      }
    }
    return params;
  }
}
