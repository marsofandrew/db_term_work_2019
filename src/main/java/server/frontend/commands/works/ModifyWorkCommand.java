package server.frontend.commands.works;

import io.vertx.core.json.JsonObject;
import server.backend.DBConnectorInterface;
import server.frontend.commands.ModifyCommand;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;

import static server.frontend.commands.works.Works.CAR_ID;
import static server.frontend.commands.works.Works.DATE_OF_WORK;
import static server.frontend.commands.works.Works.MASTER_ID;
import static server.frontend.commands.works.Works.SERVICE_ID;
import static server.frontend.commands.works.Works.TABLE_NAME;

public class ModifyWorkCommand extends ModifyCommand {
  public ModifyWorkCommand(DBConnectorInterface dbConnectorInterface) {
    super(dbConnectorInterface);
  }

  @Override
  protected void modify(String request, JsonObject data, DBConnectorInterface dbConnector) throws SQLException {
    Connection connection = dbConnectorInterface.getConnection();
    PreparedStatement statement = connection.prepareStatement(String.format("UPDATE %s SET %s = ?, %s = ?, %s = ?, %s = ? WHERE %s", TABLE_NAME,
        CAR_ID, DATE_OF_WORK, MASTER_ID, SERVICE_ID, data.getString(CONDITION_KEY)));
    JsonObject values = data.getJsonObject(UPDATE_KEY);
    if (values.getInteger(CAR_ID) != null) {
      statement.setInt(1, values.getInteger(CAR_ID));
    } else {
      statement.setNull(1, Types.INTEGER);
    }
    if (values.getInstant(DATE_OF_WORK) != null) {
      statement.setTimestamp(2, Timestamp.from(values.getInstant(DATE_OF_WORK)));
    } else {
      statement.setNull(2, Types.DATE);
    }

    if (values.getInteger(MASTER_ID) != null) {
      statement.setInt(3, values.getInteger(MASTER_ID));
    } else {
      statement.setNull(3, Types.INTEGER);
    }

    if (values.getInteger(SERVICE_ID) != null) {
      statement.setInt(4, values.getInteger(SERVICE_ID));
    } else {
      statement.setNull(4, Types.INTEGER);
    }

    statement.executeUpdate();
  }
}
