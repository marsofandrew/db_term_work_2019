package client.backend;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import server.frontend.RequestExecutor;

public class ServerConnector implements Connector {

  public boolean connect() {
    return false;
  }

  public boolean disconnect() {
    return false;
  }

  public JsonArray get(String request) {
    return RequestExecutor.get(request);
  }

  public JsonObject post(String request, JsonObject data) {
    return RequestExecutor.post(request, data);
  }

  public JsonObject patch(String request, JsonObject data) {
    return RequestExecutor.patch(request, data);
  }

  @Override
  public JsonObject delete(String request) {
    return RequestExecutor.delete(request);
  }


}
