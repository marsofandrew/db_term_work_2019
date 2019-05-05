package client.backend.objects.services;

import client.backend.objects.DbObject;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import static client.backend.Connections.SERVER;

public class Service extends DbObject {
  public Service(JsonObject object) {
    super(object);
  }

  @Override
  protected JsonObject modifyElement(JsonObject data) {
    return SERVER.patch("/service", data);
  }

  @Override
  protected JsonObject deleteElement() {
    return SERVER.delete(String.format("/service/%d", id));
  }

  @Override
  protected JsonArray getFkData(String column, Object value) {
    return null;
  }
}
