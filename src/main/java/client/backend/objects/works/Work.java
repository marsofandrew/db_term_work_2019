package client.backend.objects.works;

import client.backend.objects.DbObject;
import client.backend.objects.Helpers;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.HashMap;
import java.util.Map;

import static client.backend.Connections.SERVER;

public class Work extends DbObject {

  private Map<String, String> fKeys = new HashMap<>();

  public Work(JsonObject object) {
    super(object);
    fKeys.put("CAR_ID", "CARS");
    fKeys.put("MASTER_ID", "MASTERS");
    fKeys.put("SERVICE_ID", "SERVICES");
  }

  @Override
  protected JsonObject modifyElement(JsonObject data) {
    return SERVER.patch("/work", data);
  }

  @Override
  protected JsonObject deleteElement() {
    return SERVER.delete(String.format("/work/%d", id));
  }

  @Override
  protected JsonArray getFkData(String column, Object value) {
    String key = column.toUpperCase();
    if (!fKeys.containsKey(key) || value == null || value.equals("null") ||value.equals("NULL")) {
      return null;
    }
    JsonArray response = SERVER.get(String.format("/data?select=*&from=%s&other=where ID=%s", fKeys.get(key), value));
    return response;
  }
}
