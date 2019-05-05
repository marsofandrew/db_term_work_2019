package server.frontend;

import java.util.Objects;

public class DbHelpers {
  public static String createAndCheckStringVar(String var) {
    Objects.requireNonNull(var);
    return String.format("N\'%s\'", var);
  }
}
