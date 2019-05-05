package server.frontend.commands.works;

import io.vertx.core.json.JsonObject;
import server.backend.DBConnectorInterface;
import server.frontend.commands.CreateCommand;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLType;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDate;

import static server.frontend.commands.works.Works.CAR_ID;
import static server.frontend.commands.works.Works.DATE_OF_WORK;
import static server.frontend.commands.works.Works.MASTER_ID;
import static server.frontend.commands.works.Works.SERVICE_ID;
import static server.frontend.commands.works.Works.TABLE_NAME;


public class CreateWorkCommand extends CreateCommand {


  public CreateWorkCommand(DBConnectorInterface dbConnectorInterface) {
    super(dbConnectorInterface);
  }

  protected void create(String request, JsonObject data, DBConnectorInterface dbConnectorInterface) throws SQLException {
    Connection connection = dbConnectorInterface.getConnection();
    PreparedStatement statement = connection.prepareStatement(String.format("INSERT INTO %s (%s, %s, %s, %s) VALUES (?, ?, ?, ?)", TABLE_NAME,
        CAR_ID, DATE_OF_WORK, MASTER_ID, SERVICE_ID));
    if (data.getInteger(CAR_ID)!=null){
      statement.setInt(1, data.getInteger(CAR_ID));
    } else {
      statement.setNull(1, Types.INTEGER);
    }
    if (data.getInstant(DATE_OF_WORK) != null){
      statement.setTimestamp(2,Timestamp.from(data.getInstant(DATE_OF_WORK)));
    }else {
      statement.setNull(2, Types.DATE);
    }

    if (data.getInteger(MASTER_ID)!=null){
      statement.setInt(3, data.getInteger(MASTER_ID));
    } else {
      statement.setNull(3, Types.INTEGER);
    }

    if (data.getInteger(SERVICE_ID)!=null){
      statement.setInt(4, data.getInteger(SERVICE_ID));
    } else {
      statement.setNull(4, Types.INTEGER);
    }

    statement.executeUpdate();
  }
}
