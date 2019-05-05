package client.backend.objects;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import oracle.jdbc.pooling.Tuple;

import java.util.ArrayList;
import java.util.List;

public abstract class DbTable implements Table {

  private List<DbElement> elements;

  public DbTable() {
    elements = new ArrayList<>();
    setElements();
  }

  @Override
  public Tuple<Boolean, JsonObject> add(JsonObject data) {

    JsonObject response = addElement(data);
    boolean status = Helpers.handleResponse(response);
    if (status) {
      update();
    }
    return new Tuple<>(status, response);
  }

  @Override
  public Tuple<Boolean, JsonObject> modify(DbElement element, String[] columns, Object[] values) {
    return element.modify(columns, values);
  }

  @Override
  public Tuple<Boolean, JsonObject> modify(int identifier, String[] columns, Object[] values, IdentifierType type) {
    return modify(getObject(identifier, type), columns, values);
  }

  @Override
  public Tuple<Boolean, JsonObject> delete(int identifier, IdentifierType type) {
    return delete(getObject(identifier, type));
  }

  @Override
  public Tuple<Boolean, JsonObject> delete(DbElement element) {
    Tuple<Boolean, JsonObject> response = element.delete();
    if (response.get1()) {
      elements.remove(element);
    }
    return response;
  }

  @Override
  public Tuple<Boolean, JsonObject> update() {
    return setElements();
  }

  @Override
  public List<List<Object>> getValues() {
    List<List<Object>> data = new ArrayList<>(new ArrayList<>());
    for (DbElement element : elements) {
      data.add(element.getValues());
    }
    return data;
  }

  @Override
  public List<String> getHeaders() {
    if (elements.isEmpty()) {
      return new ArrayList<>();
    }
    return elements.get(0).getColumns();
  }

  @Override
  public DbElement getObject(int identifier, IdentifierType type) {
    if (type == IdentifierType.ID) {
      DbElement element = null;
      for (DbElement element1 : elements) {
        if (element1.getId() == identifier) {
          element = element1;
          break;
        }
      }
      return element;
    }
    return elements.get(identifier);
  }

  @Override
  public List<DbElement> getObjects() {
    return elements;
  }

  protected abstract JsonObject addElement(JsonObject data);

  protected abstract JsonArray getData();

  protected abstract DbElement createDbElement(JsonObject object);

  private Tuple<Boolean, JsonObject> setElements() {
    elements.clear();
    JsonArray response = getData();
    boolean status = Helpers.handleResponse(response.getJsonObject(0));
    if (!status) {
      throw new RuntimeException("Response with error or no response");
    }
    for (int i = 1; i < response.size(); i++) {
      elements.add(createDbElement(response.getJsonObject(i)));
    }
    return new Tuple<>(status, response.getJsonObject(0));
  }

}
