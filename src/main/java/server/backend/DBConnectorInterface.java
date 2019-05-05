package server.backend;

import io.vertx.core.json.JsonObject;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface DBConnectorInterface {

  public boolean connect();

  public boolean disconnect();

  public List<JsonObject> select(String columns, String table, String other) throws SQLException;

  public void insert(String table, String valuesNames, String values) throws SQLException;

  public void update(String table, String[] valueNames, String[] values, String condition) throws SQLException;

  public void delete(String table, String condition) throws SQLException;

  public List<JsonObject> exec(String request) throws SQLException;

  public Connection getConnection();
}
