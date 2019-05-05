package client.frontend.ui.tables;

import client.backend.objects.DbElement;
import client.backend.objects.IdentifierType;
import client.backend.objects.services.Services;
import client.frontend.ui.dialogs.Gui;
import client.frontend.ui.dialogs.UiDialog;
import client.frontend.ui.dialogs.UiResultGetter;
import io.vertx.core.json.JsonObject;
import oracle.jdbc.pooling.Tuple;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ServicesUi extends UiDbTable {

  private List<String> NUMBER_KEYS = Arrays.asList("COST_OUR", "COST_FOREIGN");

  public ServicesUi() {
    super(new Services());
    changeHeaders();
  }


  @Override
  protected Tuple<String[], Object[]> getDataForModification(DbElement element) {
    List<Object> initialValues = new ArrayList<>();
    for (Object str : element.getValues()) {
      initialValues.add(str);
    }

    UiDialog dialog = new UiDialog(Arrays.asList("ID", "NAME", "COST OUR", "COST FOREIGN"), initialValues, "Modify service");
    dialog.putInfoMessage("Input data to modify service");
    dialog.changeUI(((panel, names, values, fields) -> fields.get(0).setEditable(false)));

    List<Tuple<Object, Object>> results = Gui.showDialog(dialog);
    if (results == null) {
      return null;
    }

    Tuple<String[], Object[]> response = new Tuple<>(new String[3], new Object[3]);
    int i = 0;
    for (Tuple<Object, Object> result : results) {
      if (result.get1().toString().replaceAll(" ", "_").toUpperCase().equals("ID")) {
        continue;
      }
      response.get1()[i] = result.get1().toString().replaceAll(" ", "_").toUpperCase();
      if (NUMBER_KEYS.contains(String.valueOf(result.get1()).toUpperCase().replaceAll(" ", "_"))) {
        response.get2()[i] = Double.parseDouble(String.valueOf(result.get2()));
      } else {
        response.get2()[i] = String.valueOf(result.get2());
      }
      i++;
    }

    return response;
  }

  @Override
  protected JsonObject getDataForAdding() {
    UiDialog dialog = new UiDialog(Arrays.asList("NAME", "COST OUR", "COST FOREIGN"), Arrays.asList("", 0, 0), "Create service");
    dialog.putInfoMessage("Input data to create service");

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
    Helpers.changeTable(getDbTable(), getTable(), this::convertHeader);
  }

  private String convertHeader(String header) {
    return header.replaceAll("_", " ");
  }
}
