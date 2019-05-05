package server.frontend;


import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import server.backend.DBConnector;
import server.backend.DBConnectorInterface;
import server.frontend.commands.Command;
import server.frontend.commands.access.LoginCommand;
import server.frontend.commands.access.RegisterCommand;
import server.frontend.commands.cars.CreateCarCommand;
import server.frontend.commands.cars.DeleteCarCommand;
import server.frontend.commands.cars.ModifyCarCommand;
import server.frontend.commands.gets.BestMasterReportCommand;
import server.frontend.commands.gets.GetCommand;
import server.frontend.commands.gets.TotalRevenueReportCommand;
import server.frontend.commands.masters.CreateMasterCommand;
import server.frontend.commands.masters.DeleteMasterCommand;
import server.frontend.commands.services.CreateServiceCommand;
import server.frontend.commands.services.DeleteServiceCommand;
import server.frontend.commands.services.ModifyServiceCommand;
import server.frontend.commands.works.CreateWorkCommand;
import server.frontend.commands.works.DeleteWorkCommand;
import server.frontend.commands.works.ModifyWorkCommand;

import java.util.HashMap;
import java.util.Map;

public class RequestExecutor {

  public static final String GET = "get";
  public static final String POST = "post";
  public static final String PATCH = "patch";
  public static final String DELETE = "delete";

  private static final DBConnectorInterface dbConnector = new DBConnector("C##andrew", "a123");

  private static Map<String, Command> commandMap = initializeCommands();
  public static final String WORK = "/work";
  public static final String MASTER = "/master";
  public static final String SERVICE = "/service";
  public static final String CAR = "/car";

  private RequestExecutor() {
  }

  private static String createKey(String type, String request) {
    return String.format("%s: %s", type, request);
  }

  private static Map<String, Command> initializeCommands() {
    Map<String, Command> map = new HashMap<String, Command>();

    map.put(createKey(POST, WORK), new CreateWorkCommand(dbConnector));
    map.put(createKey(POST, "/user"), new RegisterCommand(dbConnector));
    map.put(createKey(POST, MASTER), new CreateMasterCommand(dbConnector));
    map.put(createKey(POST, SERVICE), new CreateServiceCommand(dbConnector));
    map.put(createKey(POST, CAR), new CreateCarCommand(dbConnector));
    map.put(createKey(PATCH, CAR), new ModifyCarCommand(dbConnector));
    map.put(createKey(PATCH, SERVICE), new ModifyServiceCommand(dbConnector));
    map.put(createKey(PATCH, WORK), new ModifyWorkCommand(dbConnector));
    map.put(createKey(DELETE, WORK), new DeleteWorkCommand(dbConnector));
    map.put(createKey(DELETE, CAR), new DeleteCarCommand(dbConnector));
    map.put(createKey(DELETE, MASTER), new DeleteMasterCommand(dbConnector));
    map.put(createKey(DELETE, SERVICE), new DeleteServiceCommand(dbConnector));
    map.put(createKey(GET, "/data"), new GetCommand(dbConnector));
    map.put(createKey(GET, "/total_revenue_report"), new TotalRevenueReportCommand(dbConnector));
    map.put(createKey(GET, "/best_masters_report"), new BestMasterReportCommand(dbConnector));
    map.put(createKey(GET, "/login"), new LoginCommand(dbConnector));
    return map;
  }

  public static JsonArray get(String request) {
    Command command = commandMap.get(createKey(GET, request.split("\\?")[0]));
    return command.execute(request, new JsonObject());
  }

  public static JsonObject post(String request, JsonObject data) {
    Command command = commandMap.get(createKey(POST, request));
    JsonArray response = command.execute(request, data);
    return response.getJsonObject(0);
  }

  public static JsonObject patch(String request, JsonObject data) {
    Command command = commandMap.get(createKey(PATCH, "/" + request.split("/")[1]));
    JsonArray response = command.execute(request, data);
    return response.getJsonObject(0);
  }

  public static JsonObject delete(String request) {
    Command command = commandMap.get(createKey(DELETE, "/" + request.split("/")[1]));
    JsonArray response = command.execute(request, new JsonObject());
    return response.getJsonObject(0);
  }

}


