package client.frontend.ui.tables;

import javax.swing.*;

public interface UiTable {
  public JPanel getPanel();

  public boolean addElement();

  public boolean modifyElements();

  public boolean deleteElements();

  public boolean updateData();
}
