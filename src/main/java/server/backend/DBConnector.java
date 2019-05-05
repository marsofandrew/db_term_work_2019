package server.backend;

import io.netty.util.internal.StringUtil;
import io.vertx.core.json.JsonObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DBConnector implements DBConnectorInterface {

  private String username;
  private String password;
  private Connection connection;

  public static List<JsonObject> getResults(ResultSet resultSet) throws SQLException {
    if (resultSet == null) {
      return null;
    }
    List<JsonObject> list = new ArrayList<JsonObject>();
    ResultSetMetaData metadata = resultSet.getMetaData();
    while (resultSet.next()) {
      JsonObject object = new JsonObject();
      for (int i = 1; i <= metadata.getColumnCount(); i++) {
        object.put(metadata.getColumnName(i), resultSet.getString(i));
      }
      list.add(object);
    }
    return list;
  }

  public DBConnector(String username, String password) {
    this.password = password;
    this.username = username;
  }

  public boolean connect() {
    try {
      connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", username, password);
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  public boolean disconnect() {
    try {
      connection.close();
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  public List<JsonObject> select(String columns, String table, String other) throws SQLException {
    ResultSet results;
    List<JsonObject> res;

    try (Statement statement = connection.createStatement()) {
      if (StringUtil.isNullOrEmpty(other)) {
        results = statement.executeQuery(String.format("SELECT %s FROM %s", columns, table));
      } else {
        results = statement.executeQuery(String.format("SELECT %s FROM %s %s", columns, table, other));
      }
      res = getResults(results);
    }
    return res;
  }

  public void insert(String table, String valuesNames, String values) throws SQLException {
    try (Statement statement = connection.createStatement()) {
      statement.executeUpdate(String.format("INSERT INTO %s (%s) VALUES (%s)", table, valuesNames, values));
    }
  }

  public void update(String table, String[] valueNames, String[] values, String condition) throws SQLException {
    try (Statement statement = connection.createStatement()) {
      if (valueNames.length != values.length) {
        throw new IllegalArgumentException("values names and values not equal");
      }

      StringBuilder builder = new StringBuilder(String.format("UPDATE %s SET ", table));
      for (int i = 0; i < valueNames.length - 1; i++) {
        builder.append(String.format("%s = %s, ", valueNames[i], values[i]));
      }
      builder.append(String.format("%s = %s ", valueNames[valueNames.length - 1], values[valueNames.length - 1]));
      if (!StringUtil.isNullOrEmpty(condition)) {
        builder.append(String.format(" WHERE %s", condition));
      }
      statement.executeUpdate(builder.toString());
    }
  }

  public void delete(String table, String condition) throws SQLException {
    try (Statement statement = connection.createStatement()) {
      statement.executeUpdate(String.format("DELETE FROM %s WHERE %s", table, condition));
    }
  }


  public List<JsonObject> exec(String request) throws SQLException {

    try (Statement statement = connection.createStatement()) {
      boolean hasRes = statement.execute(request);
      if (hasRes) {
        ResultSet set = statement.getResultSet();
        return getResults(set);
      }

    }
    return null;
  }

  public Connection getConnection() {
    return connection;
  }


}
