package client.backend.objects.services;

import client.backend.objects.DbElement;
import client.backend.objects.DbTable;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import static client.backend.Connections.SERVER;

public class Services extends DbTable {

  public Services() {
    super();
  }

  @Override
  protected JsonObject addElement(JsonObject data) {
    return SERVER.post("/service", data);
  }

  @Override
  protected JsonArray getData() {
    return SERVER.get("/data?select=*&from=services&other=order by id asc");
  }

  @Override
  protected DbElement createDbElement(JsonObject object) {
    return new Service(object);
  }
}
