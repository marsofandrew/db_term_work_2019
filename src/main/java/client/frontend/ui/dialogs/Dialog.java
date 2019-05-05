package client.frontend.ui.dialogs;

import oracle.jdbc.pooling.Tuple;

import javax.swing.*;
import java.util.List;

public abstract class Dialog extends JDialog {
  public abstract List<Tuple<Object, Object>> getResults();

  public abstract void putInfoMessage(String message);
}
