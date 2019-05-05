package client.frontend.ui;

import client.frontend.ui.panels.BestMastersPanel;
import client.frontend.ui.panels.ReportPanel;
import client.frontend.ui.tables.CarsUi;
import client.frontend.ui.tables.MastersUi;
import client.frontend.ui.tables.ServicesUi;
import client.frontend.ui.tables.UiDbTable;
import client.frontend.ui.tables.WorksUi;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

import static java.awt.GridBagConstraints.BOTH;

public class MainWindow {
  private JTabbedPane mainTabPan;
  private JPanel mainPanel;
  private JPanel mastersPan;
  private JPanel servicesPan;
  private JPanel carsPan;
  private JPanel worksPan;
  private JPanel additionalPan;
  private JPanel reportPan;
  private JPanel bestMastersPan;
  private List<UiDbTable> tables;
  private List<JPanel> panels;

  public MainWindow() {
    tables = Arrays.asList(new MastersUi(), new ServicesUi(), new CarsUi(), new WorksUi());
    panels = Arrays.asList(mastersPan, servicesPan, carsPan, worksPan);
    createUIComponents();
  }


  private void createUIComponents() {
    for (int i = 0; i < tables.size(); i++) {
      panels.get(i).add(tables.get(i).getPanel());
    }
    GridBagConstraints constraints = new GridBagConstraints();
    constraints.fill = BOTH;
    reportPan.add(new ReportPanel().getPanel(), constraints);
    bestMastersPan.add(new BestMastersPanel().getPanel(), constraints);
  }


  /**
   * Getter for the mainPanel.
   *
   * @return The mainPanel.
   */
  public JPanel getMainPanel() {
    return mainPanel;
  }
}
