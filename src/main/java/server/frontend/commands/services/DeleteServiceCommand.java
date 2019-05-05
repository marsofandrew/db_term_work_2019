package server.frontend.commands.services;

import server.backend.DBConnectorInterface;
import server.frontend.commands.DeleteCommand;

public class DeleteServiceCommand extends DeleteCommand {
  public DeleteServiceCommand(DBConnectorInterface dbConnectorInterface) {
    super(dbConnectorInterface);
  }

  @Override
  protected boolean canDelete(String request, DBConnectorInterface dbConnector) {
    return true;
  }

  @Override
  protected String getTable(String request) {
    return Services.TABLE_NAME;
  }

  @Override
  protected String[] getIDs(String request) {
    return getIdsFromRequest(request);
  }
}
