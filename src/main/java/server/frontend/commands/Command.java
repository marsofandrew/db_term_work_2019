package server.frontend.commands;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public interface Command {
  public JsonArray execute(String request, JsonObject data);
}