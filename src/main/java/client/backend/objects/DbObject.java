package client.backend.objects;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import oracle.jdbc.pooling.Tuple;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static server.frontend.commands.ModifyCommand.CONDITION_KEY;
import static server.frontend.commands.ModifyCommand.UPDATE_KEY;

public abstract class DbObject implements DbElement {

  protected int id;
  private List<Object> values;
  private List<String> columns;

  public DbObject(JsonObject object) {
    values = new ArrayList<>();
    columns = new ArrayList<>();
    updateInfo(object);
  }

  public void updateInfo(JsonObject object) {
    values.clear();
    columns.clear();
    id = Helpers.getIdFromResponse(object);
    setColumns(object);
    setValues(object);
  }

  @Override
  public Tuple<Boolean, JsonObject> delete() {
    JsonObject response = deleteElement();
    return new Tuple<>(Helpers.handleResponse(response), response);
  }

  @Override
  public Tuple<Boolean, JsonObject> modify(String[] columns, Object[] values) {
    JsonObject data = new JsonObject();
    data.put(CONDITION_KEY, String.format("ID = %d", id));
    JsonObject val = new JsonObject();
    for (int i = 0; i < columns.length; i++) {
      val.put(columns[i], values[i]);
    }
    data.put(UPDATE_KEY, val);
    JsonObject response = modifyElement(data);
    return new Tuple<>(Helpers.handleResponse(response), response);
  }

  @Override
  public List<Object> getValues() {
    return values;
  }

  @Override
  public List<String> getColumns() {
    return columns;
  }

  @Override
  public int getId() {
    return id;
  }

  @Override
  public Tuple<Boolean, JsonObject> getFkInfo(String column) {
    JsonArray response = getFkData(column, values.get(columns.indexOf(column)));
    if (response == null || response.isEmpty() || !Helpers.handleResponse(response.getJsonObject(0))) {
      return new Tuple<>(false, null);
    }
    return new Tuple<>(Helpers.handleResponse(response.getJsonObject(0)), response.getJsonObject(1));
  }

  protected abstract JsonObject modifyElement(JsonObject data);

  protected abstract JsonObject deleteElement();

  protected abstract JsonArray getFkData(String column, Object value);

  protected void setColumns(JsonObject object) {
    columns = Arrays.asList(object.fieldNames().toArray(new String[0]));
  }

  protected void setValues(JsonObject object) {
    for (String key : object.fieldNames()) {
      values.add(object.getValue(key));
    }
  }
}
