package client.backend.objects.cars;

import client.backend.objects.DbElement;
import client.backend.objects.DbTable;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import static client.backend.Connections.SERVER;

public class Cars extends DbTable {

  public Cars() {
    super();
  }

  @Override
  protected JsonObject addElement(JsonObject data) {
    return SERVER.post("/car", data);
  }

  @Override
  protected JsonArray getData() {
    return SERVER.get("/data?select=*&from=cars&other=order by id asc");
  }

  @Override
  protected DbElement createDbElement(JsonObject object) {
    return new Car(object);
  }
}
