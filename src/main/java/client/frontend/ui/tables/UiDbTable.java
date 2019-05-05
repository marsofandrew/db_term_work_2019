package client.frontend.ui.tables;

import client.backend.objects.DbElement;
import client.backend.objects.DbTable;
import client.backend.objects.IdentifierType;
import client.frontend.helpers.Helpers;
import io.vertx.core.json.JsonObject;
import oracle.jdbc.pooling.Tuple;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.InputMethodListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class UiDbTable implements UiTable {

  private JPanel mainPanel;
  protected JButton btnAdd;
  protected JButton btnModify;
  protected JButton btnDelete;
  protected JButton btnUpdateData;
  private JPanel bottomPanel;
  private JTable table;
  private JPanel infoPanel;
  private JLabel closeLabel;
  private JTable infoTable;
  private JScrollPane scroll;

  private DbTable dbTable;

  public UiDbTable(DbTable table) {
    this.dbTable = table;

    addListeners();
    updateUI();
  }

  public static Object[][] convertValuesToArray(List<List<Object>> values) {
    Object[][] dataArray = new Object[values.size()][];
    for (int i = 0; i < values.size(); i++) {
      dataArray[i] = values.get(i).toArray(new String[0]);
    }
    return dataArray;
  }

  @Override
  public JPanel getPanel() {
    return mainPanel;
  }

  @Override
  public boolean updateData() {
    Tuple<Boolean, JsonObject> response = dbTable.update();
    Helpers.reactOnResponse(response.get1(), response.get2(), mainPanel);
    updateUI();
    return response.get1();
  }

  @Override
  public boolean deleteElements() {
    for (int i : table.getSelectedRows()) {
      int id = Integer.parseInt(table.getValueAt(i, 0).toString());
      Tuple<Boolean, JsonObject> status = dbTable.delete(id, IdentifierType.ID);
      if (!status.get1()) {
        Helpers.reactOnResponse(status.get1(), status.get2(), mainPanel);
        updateUI();
        return false;
      }
    }
    updateUI();
    return true;
  }

  @Override
  public boolean addElement() {
    JsonObject element = null;
    try {
      element = getDataForAdding();
    } catch (Exception e) {
      JOptionPane.showMessageDialog(mainPanel, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
      return false;
    }
    if (element == null) {
      return true;
    }
    Tuple<Boolean, JsonObject> response = dbTable.add(element);
    Helpers.reactOnResponse(response.get1(), response.get2(), mainPanel);
    if (response.get1()) {
      updateUI();
    }
    return response.get1();
  }

  @Override
  public boolean modifyElements() {
    for (int i : table.getSelectedRows()) {
      int id = getId(i);
      Tuple<String[], Object[]> data = getDataForModification(dbTable.getObject(id, IdentifierType.ID));
      if (data == null) {
        continue;
      }
      Tuple<Boolean, JsonObject> status = dbTable.modify(id, data.get1(), data.get2(), IdentifierType.ID);
      if (status == null) {
        continue;
      }
      if (!status.get1()) {
        Helpers.reactOnResponse(status.get1(), status.get2(), mainPanel);
        updateUI();
        return false;
      }
    }
    updateUI();
    return true;
  }

  protected abstract Tuple<String[], Object[]> getDataForModification(DbElement element);

  protected abstract JsonObject getDataForAdding() throws Exception;

  protected void createUI() {
    table.removeAll();
    infoPanel.setVisible(false);
    infoPanel.setEnabled(false);
    Tuple<Boolean, JsonObject> status = dbTable.update();
    Helpers.reactOnResponse(status.get1(), status.get2(), mainPanel);

    List<String> headers = dbTable.getHeaders();
    List<List<Object>> data = dbTable.getValues();

    DefaultTableModel model = new DefaultTableModel(convertValuesToArray(data), headers.toArray(new String[0]));
    table.setModel(model);

    table.putClientProperty("terminateEditOnFocusLost", true);
  }

  protected JTable getTable() {
    return table;
  }

  /**
   * Getter for the dbTable.
   *
   * @return The dbTable.
   */
  protected DbTable getDbTable() {
    return dbTable;
  }

  protected abstract void additionalMousePressedActions();

  protected abstract JsonObject getInfoText(String column, int identifier, IdentifierType type);

  private void addListeners() {
    btnAdd.addActionListener(e -> makeActionOnClick(addElement()));
    btnDelete.addActionListener(e -> makeActionOnClick(deleteElements()));
    btnModify.addActionListener(e -> makeActionOnClick(modifyElements()));
    btnUpdateData.addActionListener(e -> makeActionOnClick(updateData()));
    table.addMouseListener(new MouseAdapter() {

      @Override
      public void mousePressed(MouseEvent e) {
        showInfoText();
        cancelEditingTable();
        setButtonsEnabled();
      }
    });
    table.addKeyListener(new KeyAdapter() {
      @Override
      public void keyTyped(KeyEvent e) {
        cancelEditingTable();
      }
    });
    closeLabel.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        infoPanel.setVisible(false);
      }
    });

    scroll.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        setButtonsEnabled();
      }
    });
  }

  private void setButtonsEnabled() {
    btnModify.setEnabled(table.getSelectedRows().length > 0);
    btnDelete.setEnabled(table.getSelectedRows().length > 0);
  }


  private void makeActionOnClick(boolean isSuccess) {
    if (!isSuccess) {
      JOptionPane.showMessageDialog(mainPanel, "Action failed", "ERROR", JOptionPane.ERROR_MESSAGE);
    }
    updateUI();
  }

  private void updateUI() {
    createUI();
    setButtonsEnabled();
    changeHeaders();
  }

  private void cancelEditingTable() {
    if (table.isEditing()) {
      table.editingCanceled(new ChangeEvent(table));
    }
  }

  protected void showInfoText() {
    int row = table.getSelectedRow();
    int column = table.getSelectedColumn();
    JsonObject infoText = getInfoText(table.getColumnName(column), getId(row), IdentifierType.ID);
    if (infoText == null || infoText.isEmpty()) {
      return;
    }
    infoPanel.setVisible(true);

    DefaultTableModel model = new DefaultTableModel();
    model.addColumn("COLUMN");
    model.addColumn("VALUES");
    List<String> keys = new ArrayList<>(infoText.fieldNames());
    for (int i = 0; i < keys.size(); i++) {
      String key = keys.get(i);
      model.addRow(new String[]{key, infoText.getValue(key).toString()});
    }
    infoTable.setModel(model);
  }

  protected void changeHeaders() {

  }

  private int getId(int row) {
    return Integer.parseInt(table.getValueAt(row, 0).toString());
  }
}
