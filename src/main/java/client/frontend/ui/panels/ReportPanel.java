package client.frontend.ui.panels;

import client.backend.objects.Helpers;
import com.github.lgooddatepicker.components.DatePicker;
import com.privatejgoodies.common.base.Strings;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import javax.swing.*;
import java.awt.*;
import java.text.ParseException;
import java.time.Instant;

import static client.backend.Connections.SERVER;
import static client.frontend.helpers.Helpers.reactOnResponse;
import static client.frontend.ui.panels.Helpers.DATE_FORMAT;
import static client.frontend.ui.panels.Helpers.getStandardDatePickerSettings;
import static java.awt.GridBagConstraints.BOTH;
import static java.awt.GridBagConstraints.HORIZONTAL;
import static java.awt.GridBagConstraints.WEST;

public class ReportPanel implements UiPanel {
  private JPanel panel;
  private JTextField amountTf;
  private JTextField ourTf;
  private JTextField foreignTf;
  private DatePicker startDate;
  private DatePicker endDate;
  private JButton btnReport;


  public ReportPanel() {

    createCustomUi();
    addListeners();
  }

  @Override
  public JPanel getPanel() {
    return panel;
  }

  private void createCustomUi() {
    startDate.setSettings(getStandardDatePickerSettings());
    endDate.setSettings(getStandardDatePickerSettings());
  }

  private void addListeners() {
    btnReport.addActionListener(e -> {
      try {
        react();
      } catch (ParseException ex) {
        ex.printStackTrace();
      }
    });
  }


  private void react() throws ParseException {
    Instant startDateInstant = Strings.isNotEmpty(startDate.getText()) ? DATE_FORMAT.parse(startDate.getText()).toInstant() : null;
    Instant endDateInstant = Strings.isNotEmpty(endDate.getText()) ? DATE_FORMAT.parse(endDate.getText()).toInstant() : null;
    StringBuilder builder = new StringBuilder("/total_revenue_report?");
    if (startDateInstant != null && endDateInstant != null) {
      builder.append("start_date=" + startDateInstant.toString());
      builder.append("&");
      builder.append("end_date=" + endDateInstant.toString());
    } else if (startDateInstant != null) {
      builder.append("start_date=" + startDateInstant.toString());
    } else if (endDateInstant != null) {
      builder.append("end_date=" + endDateInstant.toString());
    }

    JsonArray response = SERVER.get(builder.toString());
    if (response == null || response.isEmpty()) {
      return;
    }
    boolean statusOk = Helpers.handleResponse(response.getJsonObject(0));
    reactOnResponse(statusOk, response.getJsonObject(0), panel);
    if (statusOk && response.size() > 1) {
      JsonObject data = response.getJsonObject(1);
      amountTf.setText(data.getString("ORDERS"));
      ourTf.setText(data.getString("OUR_COST") == null ? "0" : data.getString("OUR_COST"));
      foreignTf.setText(data.getString("FOREIGN_COST") == null ? "0" : data.getString("FOREIGN_COST"));
    }
  }
}
