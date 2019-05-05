package client.frontend.ui.tables;

import client.backend.objects.DbElement;
import client.backend.objects.DbTable;
import client.backend.objects.IdentifierType;
import client.backend.objects.Table;
import client.backend.objects.cars.Cars;
import client.backend.objects.masters.Masters;
import client.backend.objects.services.Services;
import client.backend.objects.works.Works;
import client.frontend.ui.dialogs.Gui;
import client.frontend.ui.dialogs.UiDialog;
import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import com.privatejgoodies.common.base.Strings;
import com.sun.istack.internal.NotNull;
import io.vertx.core.json.JsonObject;
import oracle.jdbc.pooling.Tuple;

import javax.swing.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class WorksUi extends UiDbTable {

  private static final Works TABLE = new Works();

  private JComboBox<UiMaster> masterBox;
  private JComboBox<UiService> serviceBox;
  private JComboBox<UiCar> carBox;
  private static final DateFormat format = new SimpleDateFormat("MMM d, yyyy");
  private static final DateFormat dbDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

  public WorksUi() {
    super(TABLE);
    changeHeaders();
  }

  @Override
  protected Tuple<String[], Object[]> getDataForModification(DbElement element) {
    List<Object> initialValues = new ArrayList<>(Arrays.asList(element.getId()));
    List<Object> val = element.getValues();
    List<String> headers = element.getColumns();
    initialValues.add(element.getValues().get(headers.indexOf("DATE_WORK")));
    initialValues.add(element.getValues().get(headers.indexOf("MASTER_ID")));
    initialValues.add(element.getValues().get(headers.indexOf("CAR_ID")));
    initialValues.add(element.getValues().get(headers.indexOf("SERVICE_ID")));

    DatePicker datePicker = new DatePicker(getDatePickerSettings());
    UiDialog dialog = new UiDialog(Arrays.asList("ID", "DATE", "MASTER", "CAR", "SERVICE"), initialValues, "Create work");
    dialog.putInfoMessage("Input data to create new work");
    createComboBoxes();
    dialog.changeUI(((panel, names, values, fields) -> {
      List<JTextField> forRemove = new ArrayList<>();
      for (int i = 0; i < names.size(); i++) {
        if (i < fields.size()) {
          if (!names.get(i).equals("ID")) {
            panel.remove(fields.get(i));
            forRemove.add(fields.get(i));
          }
        }
      }
      fields.removeAll(forRemove);
      fields.get(0).setEditable(false);
      try {
        if (initialValues.get(1)!=null) {
          datePicker.setText(format.format(dbDate.parse(initialValues.get(1).toString())));
        }
      } catch (ParseException e) {
        e.printStackTrace();
      }
      WorksUi.selectData(initialValues.get(2), masterBox);
      WorksUi.selectData(initialValues.get(3), carBox);
      WorksUi.selectData(initialValues.get(4), serviceBox);

      panel.add(datePicker, UiDialog.getStandardConstraintsForValueGetter(1));
      panel.add(masterBox, UiDialog.getStandardConstraintsForValueGetter(2));
      panel.add(carBox, UiDialog.getStandardConstraintsForValueGetter(3));
      panel.add(serviceBox, UiDialog.getStandardConstraintsForValueGetter(4));
    }));
    dialog.setResultGetter(((panel, names, values, fields) -> getResult(panel, names, values, fields, datePicker)));
    List<Tuple<Object, Object>> results = Gui.showDialog(dialog);
    if (results == null){
      return null;
    }
    Tuple<String[], Object[]> response = new Tuple<>(new String[4], new Object[4]);
    for (int i = 0; i < results.size(); i++) {
      Tuple<Object, Object> result = results.get(i);
      response.get1()[i] = result.get1().toString();
      if (!response.get1()[i].equals("DATE_WORK")) {
        response.get2()[i] = result.get2();
      } else {
        try {
          response.get2()[i] = result.get2() == null ? null : format.parse(result.get2().toString()).toInstant();
        } catch (ParseException e) {
          e.printStackTrace();
        }
      }
    }
    return response;
  }

  @Override
  protected JsonObject getDataForAdding() throws ParseException {
    UiDialog dialog = new UiDialog(Arrays.asList("DATE", "MASTER", "CAR", "SERVICE"), null, "Create work");
    dialog.putInfoMessage("Input data to create new work");
    createComboBoxes();
    DatePicker datePicker = new DatePicker(getDatePickerSettings());
    dialog.changeUI(((panel, names, values, fields) -> createAddDialogUi(panel, names, values, fields, datePicker)));
    dialog.setResultGetter(((panel, names, values, fields) -> getResult(panel, names, values, fields, datePicker)));
    List<Tuple<Object, Object>> results = Gui.showDialog(dialog);
    if (results == null) {
      return null;
    }
    JsonObject object = new JsonObject();

    for (Tuple<Object, Object> result : results) {
      if (!result.get1().equals("DATE_WORK")) {
        object.put(result.get1().toString(), result.get2());
      } else {
        object.put(result.get1().toString(), result.get2() == null ? null : format.parse(result.get2().toString()).toInstant());
      }
    }
    return object;
  }

  @Override
  protected void additionalMousePressedActions() {

  }

  @Override
  protected JsonObject getInfoText(String column, int identifier, IdentifierType type) {
    DbTable table = getDbTable();
    Tuple<Boolean, JsonObject> response = table.getObject(identifier, type).getFkInfo(column.replaceAll(" ", "_"));
    if (!response.get1()) {
      return null;
    }
    return response.get2();
  }

  @Override
  protected void changeHeaders() {
    Helpers.changeTable(getDbTable(), getTable(), string -> string.replaceAll("_", " "));
  }

  private DatePickerSettings getDatePickerSettings() {
    DatePickerSettings datePickerSettings = new DatePickerSettings();
    datePickerSettings.setAllowKeyboardEditing(false);
    return datePickerSettings;
  }

  private void createComboBoxes() {
    Table table = new Masters();
    List<DbElement> elements = table.getObjects();
    UiMaster[] masters = new UiMaster[elements.size() + 1];
    for (int i = 0; i < elements.size(); i++) {
      masters[i + 1] = new UiMaster(elements.get(i));
    }
    masterBox = new JComboBox<>(masters);

    table = new Cars();
    elements = table.getObjects();
    UiCar[] cars = new UiCar[elements.size() + 1];
    for (int i = 0; i < elements.size(); i++) {
      cars[i + 1] = new UiCar(elements.get(i));
    }
    carBox = new JComboBox<>(cars);

    table = new Services();
    elements = table.getObjects();
    UiService[] services = new UiService[elements.size() + 1];
    for (int i = 0; i < elements.size(); i++) {
      services[i + 1] = new UiService(elements.get(i));
    }
    serviceBox = new JComboBox<>(services);
  }

  private void createAddDialogUi(JPanel panel, List<String> names, List<Object> values, List<JTextField> fields, DatePicker datePicker) {
    for (JTextField field : fields) {
      panel.remove(field);
    }
    fields.clear();
    panel.add(datePicker, UiDialog.getStandardConstraintsForValueGetter(0));
    panel.add(masterBox, UiDialog.getStandardConstraintsForValueGetter(1));
    panel.add(carBox, UiDialog.getStandardConstraintsForValueGetter(2));
    panel.add(serviceBox, UiDialog.getStandardConstraintsForValueGetter(3));
  }

  private List<Tuple<Object, Object>> getResult(JPanel panel, List<String> names, List<Object> values, List<JTextField> fields,
      DatePicker datePicker) {
    List<Tuple<Object, Object>> list = new ArrayList<>();
    list.add(new Tuple<>("DATE_WORK", Strings.isNotEmpty(datePicker.getText()) ? datePicker.getText() : null));
    list.add(new Tuple<>("MASTER_ID", masterBox.getSelectedItem() == null ? null : ((UiMaster) masterBox.getSelectedItem()).getId()));
    list.add(new Tuple<>("CAR_ID", carBox.getSelectedItem() == null ? null : ((UiCar) carBox.getSelectedItem()).getId()));
    list.add(new Tuple<>("SERVICE_ID", serviceBox.getSelectedItem() == null ? null : ((UiService) serviceBox.getSelectedItem()).getId()));
    return list;
  }

  private static void selectData(Object value, JComboBox<? extends UiDbObject> comboBox) {
    if (value == null){
      return;
    }
    for (int i = 0; i < comboBox.getItemCount(); i++) {
      UiDbObject dbObject = comboBox.getItemAt(i);
      if (dbObject != null && dbObject.getId() == Integer.valueOf(value.toString())) {
        comboBox.setSelectedIndex(i);
        break;
      }
    }
  }

  private interface UiDbObject {
    public int getId();
  }

  private class UiMaster implements UiDbObject {

    private int id;
    private String name;

    public UiMaster(DbElement element) {
      id = element.getId();
      int index = element.getColumns().indexOf("NAME");
      if (index >= 0) {
        name = String.valueOf(element.getValues().get(index));
      }
    }

    @Override
    public int getId() {
      return id;
    }

    @Override
    public String toString() {
      return String.format("%d| %s", id, name);
    }
  }

  private class UiService implements UiDbObject {

    private int id;
    private String name;

    public UiService(DbElement element) {
      id = element.getId();
      int index = element.getColumns().indexOf("NAME");
      if (index >= 0) {
        name = String.valueOf(element.getValues().get(index));
      }
    }

    @Override
    public int getId() {
      return id;
    }

    @Override
    public String toString() {
      return String.format("%d| %s", id, name);
    }
  }

  private class UiCar implements UiDbObject {

    private int id;
    private String num;

    public UiCar(DbElement element) {
      id = element.getId();
      int index = element.getColumns().indexOf("NUM");
      if (index >= 0) {
        num = String.valueOf(element.getValues().get(index));
      }
    }

    @Override
    public int getId() {
      return id;
    }

    @Override
    public String toString() {
      return String.format("%d| %s", id, num);
    }
  }
}

