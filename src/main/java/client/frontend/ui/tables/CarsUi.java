package client.frontend.ui.tables;

import client.backend.objects.DbElement;
import client.backend.objects.IdentifierType;
import client.backend.objects.cars.Cars;
import client.frontend.ui.dialogs.Gui;
import client.frontend.ui.dialogs.UiDialog;
import client.frontend.ui.dialogs.UiResultGetter;
import io.vertx.core.json.JsonObject;
import oracle.jdbc.pooling.Tuple;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static client.frontend.ui.dialogs.UiDialog.getStandardConstraintsForValueGetter;

public class CarsUi extends UiDbTable {

  private static final List<String> NUMBER_KEYS = Arrays.asList("IS_FOREIGN");

  public CarsUi() {
    super(new Cars());
    changeHeaders();
  }

  @Override
  protected Tuple<String[], Object[]> getDataForModification(DbElement element) {
    List<Object> initialValues = new ArrayList<>();
    for (Object str : element.getValues()) {
      initialValues.add(str);
    }

    UiDialog dialog = new UiDialog(Arrays.asList("ID", "NUM", "COLOR", "MARK", "IS FOREIGN"), initialValues, "Modify car");
    JCheckBox checkBox = new JCheckBox();
    dialog.changeUI(((panel, names, values, fields) -> {
      fields.get(0).setEditable(false);
      fields.get(1).setEditable(false);
      panel.remove(fields.get(fields.size() - 1));
      fields.remove(fields.size() - 1);
      checkBox.setSelected(Integer.parseInt(String.valueOf(values.get(values.size() - 1))) == 1);
      panel.add(checkBox, getStandardConstraintsForValueGetter(fields.size()));
    }));
    UiResultGetter standard = dialog.getResultGetter();
    dialog.setResultGetter(((panel, names, values, fields) -> {
      List<Tuple<Object, Object>> results = standard.getResults(panel, names, values, fields);
      results.add(new Tuple<>(NUMBER_KEYS.get(0), checkBox.isSelected() ? 1 : 0));
      List<Tuple<Object, Object>> newResults = new ArrayList<>();
      for (Tuple<Object, Object> result : results) {
        if (result.get1().equals("ID") || result.get1().equals("NUM") || result.get1().equals("IS FOREIGN")) {
          continue;
        }
        newResults.add(result);
      }
      return newResults;
    }));

    dialog.putInfoMessage("Input data to modify car");


    List<Tuple<Object, Object>> results = Gui.showDialog(dialog);
    if (results == null) {
      return null;
    }


    Tuple<String[], Object[]> response = new Tuple<>(new String[3], new Object[3]);
    int i = 0;
    for (Tuple<Object, Object> result : results) {
      response.get1()[i] = result.get1().toString().replaceAll(" ", "_").toUpperCase();
      if (NUMBER_KEYS.contains(String.valueOf(result.get1()).toUpperCase().replaceAll(" ", "_"))) {
        response.get2()[i] = Integer.parseInt(String.valueOf(result.get2()));
      } else {
        response.get2()[i] = String.valueOf(result.get2());
      }
      i++;
    }

    return response;
  }

  @Override
  protected JsonObject getDataForAdding() {
    UiDialog dialog = new UiDialog(Arrays.asList("NUM", "COLOR", "MARK", "IS FOREIGN"), Arrays.asList("", "", "", 0), "Create car");
    JCheckBox checkBox = new JCheckBox();
    dialog.changeUI(((panel, names, values, fields) -> {
      panel.remove(fields.get(fields.size() - 1));
      fields.remove(fields.size() - 1);
      panel.add(checkBox, getStandardConstraintsForValueGetter(fields.size()));
    }));
    UiResultGetter standard = dialog.getResultGetter();
    dialog.setResultGetter(((panel, names, values, fields) -> {
      List<Tuple<Object, Object>> results = standard.getResults(panel, names, values, fields);
      results.add(new Tuple<>(NUMBER_KEYS.get(0), checkBox.isSelected() ? 1 : 0));
      return results;
    }));

    dialog.putInfoMessage("Input data to create car");


    List<Tuple<Object, Object>> results = Gui.showDialog(dialog);
    if (results == null) {
      return null;
    }

    JsonObject object = new JsonObject();
    for (Tuple<Object, Object> res : results) {
      if (NUMBER_KEYS.contains(res.get1().toString().replaceAll(" ", "_").toUpperCase())) {
        object.put(res.get1().toString().toUpperCase().replaceAll(" ", "_"), Integer.parseInt(res.get2().toString()));
      } else {
        object.put(res.get1().toString().toUpperCase().replaceAll(" ", "_"), res.get2());
      }
    }

    return object;
  }

  @Override
  protected void additionalMousePressedActions() {

  }

  @Override
  protected JsonObject getInfoText(String column, int identifier, IdentifierType type) {
    return null;
  }

  @Override
  protected void changeHeaders() {
    Helpers.changeTable(getDbTable(), getTable(), string -> string.replaceAll("_", " "));
  }
}
