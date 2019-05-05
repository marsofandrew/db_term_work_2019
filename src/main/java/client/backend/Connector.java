package client.backend;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public interface Connector {

  public boolean connect();

  public boolean disconnect();

  public JsonArray get(String request);

  public JsonObject post(String request, JsonObject data);

  public JsonObject patch(String request, JsonObject data);

  public JsonObject delete(String request);

}
