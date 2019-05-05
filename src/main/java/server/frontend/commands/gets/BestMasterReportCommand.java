package server.frontend.commands.gets;

import io.vertx.core.json.JsonObject;
import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;
import oracle.jdbc.pooling.Tuple;
import server.backend.DBConnectorInterface;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static server.frontend.commands.gets.Helpers.divideQueryPart;
import static server.frontend.commands.gets.Helpers.getQueryParts;
import static server.frontend.commands.gets.Helpers.getResultFromCursor;

public class BestMasterReportCommand extends GetCommand {
  public BestMasterReportCommand(DBConnectorInterface dbConnectorInterface) {
    super(dbConnectorInterface);
  }

  @Override
  protected List<JsonObject> getDbResponse(String request, JsonObject data) throws SQLException {
    String[] parts = getQueryParts(request);
    Instant instant = null;
    for (String part : parts) {
      Tuple<String, String> element = divideQueryPart(part);
      if (element.get1().equals("start_date")) {
        instant = Instant.parse(element.get2());
      }
    }
    if (instant == null) {
      throw new SQLException("You must provide start_date as a query parameter", "start_date is not provided", 400);
    }

    Connection connection = dbConnectorInterface.getConnection();
    List<JsonObject> serverResponse = null;
    try (CallableStatement command = connection.prepareCall("BEGIN GET_BEST_MASTERS(?, ?); END;")) {
      command.setTimestamp(1, Timestamp.from(instant));
      command.registerOutParameter(2, OracleTypes.CURSOR);
      command.execute();
      ResultSet set = ((OracleCallableStatement) command).getCursor(2);
      serverResponse = getResultFromCursor(set, Arrays.asList("MASTER_ID", "NAME", "JOBS"));
    }
    return serverResponse;
  }


}
