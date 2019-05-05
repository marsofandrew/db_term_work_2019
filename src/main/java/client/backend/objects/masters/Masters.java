package client.backend.objects.masters;

import client.backend.objects.DbElement;
import client.backend.objects.DbTable;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import oracle.jdbc.pooling.Tuple;

import java.util.List;

import static client.backend.Connections.SERVER;

public class Masters extends DbTable {

  public Masters() {
    super();
  }

  @Override
  protected JsonObject addElement(JsonObject data) {

    return SERVER.post("/master", data);
  }

  @Override
  protected JsonArray getData() {
    return SERVER.get("/data?select=*&from=masters&other=order by id asc");
  }

  @Override
  protected DbElement createDbElement(JsonObject object) {
    return new Master(object);
  }
}
