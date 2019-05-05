package client.backend.objects.works;

import client.backend.objects.DbElement;
import client.backend.objects.DbTable;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import static client.backend.Connections.SERVER;

public class Works extends DbTable {

  public Works() {
    super();
  }

  @Override
  protected JsonObject addElement(JsonObject data) {
    return SERVER.post("/work", data);
  }

  @Override
  protected JsonArray getData() {
    return SERVER.get("/data?select=*&from=works&other=order by id asc");
  }

  @Override
  protected DbElement createDbElement(JsonObject object) {
    return new Work(object);
  }
}
