package client.frontend.ui.tables;

import client.backend.objects.DbTable;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static client.frontend.ui.tables.UiDbTable.convertValuesToArray;

public class Helpers {

  public static void changeTable(DbTable table, JTable jTable, HeaderConverter converter) {
    List<List<Object>> data = table.getValues();
    List<String> headers = new ArrayList<>(table.getHeaders().size());
    table.getHeaders().forEach(headers::add);
    for (int i = 0; i < headers.size(); i++) {
      if (headers.get(i).contains("_")) {
        headers.set(i, converter.convert(headers.get(i)));
      }
    }
    TableModel model = new DefaultTableModel(convertValuesToArray(data), headers.toArray(new String[0]));
    jTable.setModel(model);
  }

  private Helpers() {
  }
}
