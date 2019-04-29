package PresentationLayer;

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

    public abstract void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;

    public static Command from(HttpServletRequest request) {
        Command c;
        String origin = request.getParameter("view");

        Map<String, Command> commands = new HashMap();
        commands.put("login", new LoginCommand());
        commands.put("backendpage", new BackendPageCommand());
        commands.put("orderpage", new OrderPageCommand());
        commands.put("addorder", new AddOrderCommand());
        commands.put("assignorder", new AssignOrderCommand());
        commands.put("orderinfo", new OrderInfoCommand());
        commands.put("orderinfoadmin", new OrderInfoAdminCommand());
        
        c = commands.getOrDefault(origin, new UnknownCommand());

        return c;
    }

}
