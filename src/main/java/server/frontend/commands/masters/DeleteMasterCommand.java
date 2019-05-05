package server.frontend.commands.masters;

import server.backend.DBConnectorInterface;
import server.frontend.commands.DeleteCommand;

public class DeleteMasterCommand extends DeleteCommand {
  public DeleteMasterCommand(DBConnectorInterface dbConnectorInterface) {
    super(dbConnectorInterface);
  }

  @Override
  protected boolean canDelete(String request, DBConnectorInterface dbConnector) {
    return true;
  }

  @Override
  protected String getTable(String request) {
    return Masters.TABLE_NAME;
  }

  @Override
  protected String[] getIDs(String request) {
    return super.getIdsFromRequest(request);
  }
}
