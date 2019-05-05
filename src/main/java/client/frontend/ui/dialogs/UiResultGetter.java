package client.frontend.ui.dialogs;

import oracle.jdbc.pooling.Tuple;

import javax.swing.*;
import java.util.List;

public interface UiResultGetter {
  public List<Tuple<Object, Object>> getResults(JPanel panel, List<String> names, List<Object> values, List<JTextField> fields);
}
