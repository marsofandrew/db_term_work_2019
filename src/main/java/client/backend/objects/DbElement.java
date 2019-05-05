package client.backend.objects;

import io.vertx.core.json.JsonObject;
import oracle.jdbc.pooling.Tuple;

import java.util.List;

public interface DbElement {

  public int getId();

  public Tuple<Boolean, JsonObject> modify(String[] columns, Object[] values);

  public Tuple<Boolean, JsonObject> delete();

  public List<Object> getValues();

  public Tuple<Boolean, JsonObject> getFkInfo(String column);

  public List<String> getColumns();

}
