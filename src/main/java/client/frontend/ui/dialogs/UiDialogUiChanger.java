package client.frontend.ui.dialogs;

import javax.swing.*;
import java.text.ParseException;
import java.util.List;

public interface UiDialogUiChanger {
  public void changeUi(JPanel panel, List<String> names, List<Object> values, List<JTextField> fields);
}
