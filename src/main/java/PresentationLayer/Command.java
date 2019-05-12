package PresentationLayer;

import data.FOGException;
import data.UpdateException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author caspe
 */
public abstract class Command {

    public abstract void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, FOGException;

    public static Command from(HttpServletRequest request) {
        Command c;
        String origin = request.getParameter("view");

        Map<String, Command> commands = new HashMap();
        commands.put("login", new LoginCommand());
        commands.put("backendpage", new BackendPageCommand());
        commands.put("orderpage", new OrderPageCommand());
        commands.put("addorder", new AddOrderCommand());
        commands.put("assignorder", new AssignOrderCommand());
        commands.put("updateorder", new UpdateOrderCommand());
        commands.put("orderinfo", new OrderInfoCommand());
        commands.put("orderinfoadmin", new OrderInfoAdminCommand());
        commands.put("mats", new MaterialPageCommand());
        commands.put("carport", new CarportProductsCommand());
        commands.put("carportEdit", new CarportProductsEditCommand());
        
        c = commands.getOrDefault(origin, new UnknownCommand());

        return c;
    }

}
