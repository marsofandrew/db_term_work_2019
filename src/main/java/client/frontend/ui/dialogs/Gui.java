package client.frontend.ui.dialogs;

import oracle.jdbc.pooling.Tuple;

import java.util.List;

public class Gui {

  public static List<Tuple<Object, Object>> showDialog(Dialog dialog) {
    dialog.setVisible(true);
    dialog.dispose();

    return dialog.getResults();
  }

  private Gui() {
  }
}
