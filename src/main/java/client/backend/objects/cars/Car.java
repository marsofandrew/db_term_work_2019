package client.backend.objects.cars;

import client.backend.objects.DbObject;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import static client.backend.Connections.SERVER;

public class Car extends DbObject {
  public Car(JsonObject object) {
    super(object);
  }

  @Override
  protected JsonObject modifyElement(JsonObject data) {
    return SERVER.patch("/car", data);
  }

  @Override
  protected JsonObject deleteElement() {
    return SERVER.delete(String.format("/car/%d", id));
  }

  @Override
  protected JsonArray getFkData(String column, Object value) {
    return null;
  }
}
