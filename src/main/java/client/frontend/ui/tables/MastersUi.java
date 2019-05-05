package client.frontend.ui.tables;

import client.backend.objects.DbElement;
import client.backend.objects.IdentifierType;
import client.backend.objects.masters.Masters;
import io.vertx.core.json.JsonObject;
import oracle.jdbc.pooling.Tuple;

import javax.swing.*;


public class MastersUi extends UiDbTable {

  public MastersUi() {
    super(new Masters());
    btnModify.setVisible(false);
  }

  @Override
  protected Tuple<String[], Object[]> getDataForModification(DbElement element) {
    return null;
  }

  @Override
  protected JsonObject getDataForAdding() {
    String name = JOptionPane.showInputDialog(getPanel(), "Input name", "Add new master", JOptionPane.QUESTION_MESSAGE);
    if (name == null) {
      return null;
    }
    return new JsonObject().put("name", name);
  }

  @Override
  protected void additionalMousePressedActions() {

  }

  @Override
  protected JsonObject getInfoText(String column, int identifier, IdentifierType type) {
    return null;
  }

}
