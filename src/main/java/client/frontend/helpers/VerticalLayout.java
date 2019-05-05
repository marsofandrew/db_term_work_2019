package client.frontend.helpers;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static java.awt.GridBagConstraints.BOTH;
import static java.awt.GridBagConstraints.NORTHWEST;
import static java.awt.GridBagConstraints.RELATIVE;
import static java.awt.GridBagConstraints.WEST;

public class VerticalLayout {
  private JPanel mainPanel;
  private JPanel panel;
  private List<Component> components;

  public VerticalLayout() {
    panel = new JPanel(new GridBagLayout());
    components = new ArrayList<>();
    mainPanel = new JPanel(new BorderLayout());
    mainPanel.add(panel, BorderLayout.NORTH);
  }

  public void add(Component component, int index) {
    components.add(component);
    addToPanel(component, index);
  }

  public void add(Component component) {
    add(component, components.size());
  }

  public void remove(Component component) {
    components.remove(component);
    panel.remove(component);
  }

  public void remove(int index) {
    components.remove(index);
    panel.remove(index);
  }

  public void removeAll() {
    panel.removeAll();
    components.clear();
  }

  /**
   * Getter for the panel.
   *
   * @return The panel.
   */
  public JPanel getPanel() {
    return mainPanel;
  }

  /**
   * Getter for the components.
   *
   * @return The components.
   */
  public List<Component> getComponents() {
    return components;
  }

  public void update() {
    panel.removeAll();
    for (int i = 0; i < components.size(); i++) {
      addToPanel(components.get(i), i);
    }
  }

  private void addToPanel(Component component, int index) {
    GridBagConstraints constraints = new GridBagConstraints();
    constraints.gridx = 0;
    constraints.gridy = index;
    constraints.insets = new Insets(5, 10, 5, 10);
    constraints.anchor = NORTHWEST;
    panel.add(component, constraints);
  }


}
