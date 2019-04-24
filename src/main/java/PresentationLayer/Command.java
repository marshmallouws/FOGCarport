package PresentationLayer;

import java.io.IOException;
import java.util.HashMap;
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
        String path = request.getPathInfo().substring(1);
        //String path = request.getParameter("action");

        switch (path) {
            case "orderpage":
                c = new OrderPageCommand();
                break;
            default:
                c = new UnknownCommand();
        }

        return c;
    }

}
