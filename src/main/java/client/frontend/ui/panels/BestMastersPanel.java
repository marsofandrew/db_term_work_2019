package client.frontend.ui.panels;

import com.github.lgooddatepicker.components.DatePicker;
import com.privatejgoodies.common.base.Strings;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableModel;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static client.backend.Connections.SERVER;
import static client.backend.objects.Helpers.handleResponse;
import static client.frontend.helpers.Helpers.reactOnResponse;
import static client.frontend.ui.panels.Helpers.DATE_FORMAT;
import static client.frontend.ui.panels.Helpers.getStandardDatePickerSettings;

public class BestMastersPanel implements UiPanel {
  private JPanel panel;
  private JButton btnReport;
  private JTable table;
  private DatePicker datePicker;

  public BestMastersPanel() {
    datePicker.setSettings(getStandardDatePickerSettings());
    table.putClientProperty("terminateEditOnFocusLost", true);
    table.addMouseListener(new MouseAdapter() {

      @Override
      public void mousePressed(MouseEvent e) {
        cancelEditingTable();
      }

    });
    table.addKeyListener(new KeyAdapter() {
      @Override
      public void keyTyped(KeyEvent e) {
        cancelEditingTable();
      }
    });
    addListeners();
  }

  @Override
  public JPanel getPanel() {
    return panel;
  }

  private void addListeners() {
    btnReport.addActionListener(e -> react());
  }

  private void react() {
    Instant startDate = null;
    try {
      startDate = Strings.isNotEmpty(datePicker.getText()) ? DATE_FORMAT.parse(datePicker.getText()).toInstant() : null;
    } catch (ParseException e) {
      e.printStackTrace();
    }
    if (startDate == null) {
      Calendar now = Calendar.getInstance();
      now.add(Calendar.MONTH, -1);
      startDate = now.toInstant();
    }
    JsonArray response = SERVER.get("/best_masters_report?start_date=" + startDate);
    boolean statusOk = handleResponse(response.getJsonObject(0));
    reactOnResponse(statusOk, response.getJsonObject(0), panel);
    if (!statusOk) {
      return;
    }
    response.remove(0);

    if (response.size() < 1) {
      return;
    }
    String[][] data = new String[response.size()][];
    for (int i = 0; i < response.size(); i++) {
      JsonObject object = response.getJsonObject(i);
      List<String> strings = new ArrayList<>();
      for (String key : object.fieldNames()) {
        strings.add(String.valueOf(object.getValue(key)));
      }
      data[i] = strings.toArray(new String[0]);
    }
    String[] headers = response.getJsonObject(0).fieldNames().toArray(new String[0]);
    convertHeaders(headers);
    DefaultTableModel model = new DefaultTableModel(data, headers);
    table.setModel(model);
  }

  private String[] convertHeaders(String[] headers) {
    for (int i = 0; i < headers.length; i++) {
      headers[i] = headers[i].replaceAll("_", " ");
    }
    return headers;
  }

  private void cancelEditingTable() {
    if (table.isEditing()) {
      table.editingCanceled(new ChangeEvent(table));
    }
  }
}
