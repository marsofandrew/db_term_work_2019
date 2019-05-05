package server.frontend.commands.cars;

import server.backend.DBConnectorInterface;
import server.frontend.commands.DeleteCommand;

public class DeleteCarCommand extends DeleteCommand {
  public DeleteCarCommand(DBConnectorInterface dbConnectorInterface) {
    super(dbConnectorInterface);
  }

  @Override
  protected boolean canDelete(String request, DBConnectorInterface dbConnector) {
    return true;
  }

  @Override
  protected String getTable(String request) {
    return Cars.TABLE_NAME;
  }

  @Override
  protected String[] getIDs(String request) {
    return super.getIdsFromRequest(request);
  }
}
