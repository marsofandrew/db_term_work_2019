package client.frontend.ui.dialogs;

import oracle.jdbc.pooling.Tuple;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static java.awt.GridBagConstraints.BOTH;
import static java.awt.GridBagConstraints.WEST;

public class UiDialog extends Dialog {
  private JPanel contentPane;
  private JButton buttonOK;
  private JButton buttonCancel;
  private JPanel panel;
  private JPanel infoPanel;
  private JLabel infoLabel;
  private List<String> names;
  private List<Object> values;
  private List<JTextField> fields;
  private List<Tuple<Object, Object>> results;
  private UiResultGetter resultGetter;

  public static GridBagConstraints getStandardConstraintsForValueGetter(int yIndex) {
    GridBagConstraints constraints2 = new GridBagConstraints();
    constraints2.anchor = WEST;
    constraints2.fill = BOTH;
    constraints2.gridy = yIndex;
    constraints2.gridx = 1;
    constraints2.weightx = 9;
    return constraints2;
  }

  public UiDialog(List<String> names, List<Object> initialValues) {
    this(names, initialValues, "");
  }

  public UiDialog(List<String> names, List<Object> initialValues, String title) {
    this(names, initialValues, title, new Dimension(600, 400));
  }

  public UiDialog(List<String> names, List<Object> initialValues, String title, Dimension size) {
    setContentPane(contentPane);
    setModal(true);
    getRootPane().setDefaultButton(buttonOK);
    this.names = names;
    this.values = initialValues;
    this.setSize(size);
    this.setResizable(false);
    this.setTitle(title);
    infoPanel.setVisible(false);
    fields = new ArrayList<>(names.size());
    setUiParams();
    setCustomUI();
    resultGetter = this::takeResults;
  }

  /**
   * Getter for the resultGetter.
   *
   * @return The resultGetter.
   */
  public UiResultGetter getResultGetter() {
    return resultGetter;
  }

  @Override
  public List<Tuple<Object, Object>> getResults() {
    return results;
  }

  public void changeUI(UiDialogUiChanger changer) {
    changer.changeUi(panel, names, values, fields);
  }

  @Override
  public void putInfoMessage(String message) {
    infoPanel.setVisible(true);
    infoLabel.setText(message);
  }

  /**
   * Setter for the resultGetter.
   *
   * @param resultGetter The resultGetter.
   * @return This, so the API can be used fluently.
   */
  public UiDialog setResultGetter(UiResultGetter resultGetter) {
    this.resultGetter = resultGetter;
    return this;
  }

  /**
   * Getter for the values.
   *
   * @return The values.
   */
  public List<Object> getValues() {
    return values;
  }

  /**
   * Getter for the names.
   *
   * @return The names.
   */
  public List<String> getNames() {
    return names;
  }

  /**
   * Getter for the panel.
   *
   * @return The panel.
   */
  public JPanel getPanel() {
    return panel;
  }


  private void onOK() {
    results = resultGetter.getResults(panel, names, values, fields);
    dispose();
  }

  private List<Tuple<Object, Object>> takeResults(JPanel panel, List<String> names, List<Object> values, List<JTextField> fields) {
    List<Tuple<Object, Object>> list = new ArrayList<>();
    for (int i = 0; i < Math.max(names == null ? 0 : names.size(), values == null ? 0 : values.size()); i++) {
      Object value = null;
      String name = null;
      if (names != null && i < names.size()) {
        name = names.get(i);
      }
      if (values != null && i < values.size()) {
        value = values.get(i);
        if (fields != null && i < fields.size()) {
          value = fields.get(i).getText();
        }
      }
      list.add(new Tuple<>(name, value));
    }
    return list;
  }

  private void onCancel() {
    dispose();
  }

  private void setUiParams() {
    buttonOK.addActionListener(e -> onOK());

    buttonCancel.addActionListener(e -> onCancel());

    // call onCancel() when cross is clicked
    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        onCancel();
      }
    });

    // call onCancel() on ESCAPE
    contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
  }

  private void setCustomUI() {
    if (names == null) {
      return;
    }
    for (int i = 0; i < names.size(); i++) {
      GridBagConstraints constraints = new GridBagConstraints();
      constraints.anchor = WEST;
      constraints.gridy = i;
      constraints.gridx = 0;
      constraints.weightx = 1;
      constraints.fill = BOTH;
      JLabel label = new JLabel(names.get(i));
      panel.add(label, constraints);
      GridBagConstraints constraints2 = getStandardConstraintsForValueGetter(i);
      JTextField field = new JTextField();
      if (values != null && i < values.size() && values.get(i) != null) {
        field.setText(values.get(i).toString());
      }
      fields.add(field);
      panel.add(field, constraints2);
    }
  }


}
