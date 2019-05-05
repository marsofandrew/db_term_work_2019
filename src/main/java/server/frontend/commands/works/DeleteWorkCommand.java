package server.frontend.commands.works;

import server.backend.DBConnectorInterface;
import server.frontend.commands.DeleteCommand;

public class DeleteWorkCommand extends DeleteCommand {
  public DeleteWorkCommand(DBConnectorInterface dbConnectorInterface) {
    super(dbConnectorInterface);
  }

  @Override
  protected boolean canDelete(String request, DBConnectorInterface dbConnector) {
    return true;
  }

  @Override
  protected String getTable(String request) {
    return Works.TABLE_NAME;
  }

  @Override
  protected String[] getIDs(String request) {
    return getIdsFromRequest(request);
  }
}
