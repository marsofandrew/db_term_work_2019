package server.frontend.commands;

import server.backend.DBConnectorInterface;

public abstract class DBCommand implements Command {
  protected DBConnectorInterface dbConnectorInterface;

  public DBCommand(DBConnectorInterface dbConnectorInterface) {
    this.dbConnectorInterface = dbConnectorInterface;
  }
}
