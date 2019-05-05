package server.frontend.commands.gets;

import io.vertx.core.json.JsonObject;
import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;
import oracle.jdbc.pooling.Tuple;
import server.backend.DBConnector;
import server.backend.DBConnectorInterface;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static server.frontend.commands.gets.Helpers.divideQueryPart;
import static server.frontend.commands.gets.Helpers.getQueryParts;
import static server.frontend.commands.gets.Helpers.getResultFromCursor;

public class TotalRevenueReportCommand extends GetCommand {
  public TotalRevenueReportCommand(DBConnectorInterface dbConnectorInterface) {
    super(dbConnectorInterface);
  }

  @Override
  protected List<JsonObject> getDbResponse(String request, JsonObject data) throws SQLException {
    String[] queryParts = getQueryParts(request);
    Instant startDate = null;
    Instant endDate = null;
    for (String part : queryParts) {
      Tuple<String, String> element = divideQueryPart(part);
      switch (element.get1()) {
        case "start_date":
          startDate = Instant.parse(element.get2());
          break;
        case "end_date":
          endDate = Instant.parse(element.get2());
          break;
      }
    }
    Connection connection = dbConnectorInterface.getConnection();
    List<JsonObject> serverResponse = null;
    try (CallableStatement command = connection.prepareCall("BEGIN TOTAL_REVENUE_REPORT(?, ?, ?); END;")) {
      if (startDate != null){
        command.setTimestamp(1, Timestamp.from(startDate));
      } else {
        command.setNull(1, OracleTypes.DATE);
      }

      if (endDate != null){
        command.setTimestamp(2, Timestamp.from(endDate));
      } else {
        command.setNull(2, OracleTypes.DATE);
      }
      command.registerOutParameter(3, OracleTypes.CURSOR);
      command.execute();
      ResultSet set = ((OracleCallableStatement) command).getCursor(3);
      serverResponse = getResultFromCursor(set, Arrays.asList("ORDERS", "OUR_COST", "FOREIGN_COST"));
    }
    return serverResponse;
  }
}
