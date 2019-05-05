package client.backend.objects.masters;

import client.backend.objects.DbObject;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import static client.backend.Connections.SERVER;

public class Master extends DbObject {
  public Master(JsonObject object) {
    super(object);
  }

  @Override
  protected JsonObject modifyElement(JsonObject data) {
    return null;
  }

  @Override
  protected JsonObject deleteElement() {
    return SERVER.delete(String.format("/master/%d", id));
  }

  @Override
  protected JsonArray getFkData(String column, Object value) {
    return null;
  }
}
