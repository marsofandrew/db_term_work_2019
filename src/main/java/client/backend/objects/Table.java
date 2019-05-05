package client.backend.objects;

import io.vertx.core.json.JsonObject;
import oracle.jdbc.pooling.Tuple;

import java.util.List;

public interface Table {
  public Tuple<Boolean, JsonObject> add(JsonObject data);

  public Tuple<Boolean, JsonObject> modify(DbElement element, String[] columns, Object[] values);

  public Tuple<Boolean, JsonObject> modify(int identifier, String[] columns, Object[] values, IdentifierType type);

  public Tuple<Boolean, JsonObject> delete(int identifier, IdentifierType type);

  public Tuple<Boolean, JsonObject> delete(DbElement element);

  public Tuple<Boolean, JsonObject> update();

  public List<List<Object>> getValues();

  public List<String> getHeaders();

  public DbElement getObject(int identifier, IdentifierType type);

  public List<DbElement> getObjects();
}
