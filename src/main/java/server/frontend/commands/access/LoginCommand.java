package server.frontend.commands.access;

import io.vertx.core.json.JsonObject;
import oracle.jdbc.pooling.Tuple;
import server.backend.DBConnectorInterface;
import server.frontend.commands.gets.GetCommand;

import java.sql.SQLException;
import java.util.List;

import static server.frontend.DbHelpers.createAndCheckStringVar;
import static server.frontend.commands.access.Helpers.TABLE_NAME;
import static server.frontend.commands.access.Helpers.countHash;
import static server.frontend.commands.gets.Helpers.divideQueryPart;
import static server.frontend.commands.gets.Helpers.getQueryParts;

public class LoginCommand extends GetCommand {

  private static final String PASSWORD = "PASSWORD";

  public LoginCommand(DBConnectorInterface dbConnectorInterface) {
    super(dbConnectorInterface);
  }

  @Override
  protected List<JsonObject> getDbResponse(String request, JsonObject data) throws SQLException {
    String[] queryParts = getQueryParts(request);
    String username = null;
    String password = null;
    for (String part : queryParts) {
      Tuple<String, String> element = divideQueryPart(part);

      switch (element.get1()) {
        case "username":
          username = element.get2();
          break;
        case "password":
          password = element.get2();
          break;
      }
    }

    if (password == null) {
      throw new RuntimeException("You should provide password. It cant be empty or null");
    }

    List<JsonObject> dbResponse = dbConnectorInterface.select(PASSWORD, TABLE_NAME, "WHERE username = " + createAndCheckStringVar(username));
    String dbPassword = dbResponse.get(0).getString(PASSWORD);
    if (!countHash(password).equals(dbPassword)) {
      throw new SQLException("Password or user name is incorrect", "Invalid password or username", 401);
    }
    return null;
  }
}
