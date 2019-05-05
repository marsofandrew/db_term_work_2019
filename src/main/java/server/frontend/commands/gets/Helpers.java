package server.frontend.commands.gets;

import io.vertx.core.json.JsonObject;
import oracle.jdbc.pooling.Tuple;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Helpers {
  public static String[] getQueryParts(String request) {
    String[] paramString = request.split("\\?");
    if (paramString.length < 2) {
      return new String[0];
    }
    return paramString[1].split("&");
  }

  public static Tuple<String, String> divideQueryPart(String queryPart) {
    int index = queryPart.indexOf('=');
    String[] elements = new String[]{queryPart.substring(0, index), queryPart.substring(index + 1)};
    return new Tuple<>(elements[0], elements[1]);
  }

  public static List<JsonObject> getResultFromCursor(ResultSet response, List<String> keys) throws SQLException {
    List<JsonObject> list = new ArrayList<>();
    while (response.next()) {
      JsonObject object = new JsonObject();
      for (String key : keys) {
        object.put(key, response.getString(key));
      }
      list.add(object);
    }
    return list;
  }

  private Helpers() {
  }
}
