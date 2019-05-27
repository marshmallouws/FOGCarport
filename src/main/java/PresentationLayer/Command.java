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

//windows1252
public abstract class Command {

    private final static Map<String, Command> COMMANDS;
    static {
        COMMANDS = new HashMap<>();
        COMMANDS.put("login", new LoginCommand());
        COMMANDS.put("backendpage", new BackendPageCommand());
        COMMANDS.put("orderpage", new OrderPageCommand());
        COMMANDS.put("addorder", new AddOrderCommand());
        COMMANDS.put("assignorder", new AssignOrderCommand());
        COMMANDS.put("updateorder", new UpdateOrderCommand());
        COMMANDS.put("orderinfo", new OrderInfoCommand());
        COMMANDS.put("orderinfoadmin", new OrderInfoAdminCommand());
        COMMANDS.put("mats", new MaterialPageCommand());
        COMMANDS.put("carport", new CarportProductsCommand());
        COMMANDS.put("carportEdit", new CarportProductsEditCommand());
        COMMANDS.put("downloadpdf", new DownloadPDFCommand());
        COMMANDS.put("svg", new SVGCommand());
    }

    //private static Map<String, Command> commands = null;

    public abstract void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, FOGException;

    public static Command from(HttpServletRequest request) {
//        if (commands == null) {
//            initMap();
//        }
        Command c;
        String origin = request.getParameter("view");

        c = COMMANDS.getOrDefault(origin, new UnknownCommand());

        return c;
    }

//    private static void initMap() {
//        synchronized (Command.class) {
//            if (commands == null) {
//
//                commands = new HashMap<>();
//                commands.put("login", new LoginCommand());
//                commands.put("backendpage", new BackendPageCommand());
//                commands.put("orderpage", new OrderPageCommand());
//                commands.put("addorder", new AddOrderCommand());
//                commands.put("assignorder", new AssignOrderCommand());
//                commands.put("updateorder", new UpdateOrderCommand());
//                commands.put("orderinfo", new OrderInfoCommand());
//                commands.put("orderinfoadmin", new OrderInfoAdminCommand());
//                commands.put("mats", new MaterialPageCommand());
//                commands.put("carport", new CarportProductsCommand());
//                commands.put("carportEdit", new CarportProductsEditCommand());
//                commands.put("downloadpdf", new DownloadPDFCommand());
//            }
//        }
//    }

}
