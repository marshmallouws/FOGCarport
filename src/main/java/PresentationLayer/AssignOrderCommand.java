package PresentationLayer;

import data.FOGException;
import entity.Employee;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import logic.LogicFacade;

/**
 *
 * @author caspe
 */
public class AssignOrderCommand extends Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, FOGException {
        if(request.getSession().getAttribute("user")==null)throw new FOGException("Du skal være logget ind for at tilgå denne side.");
        HttpSession session = request.getSession();
        Employee user = (Employee) session.getAttribute("user");
        LogicFacade logic = new LogicFacade();

        String _orderID = request.getParameter("orderID");
        int orderID;
        int employeeID;
            try {
                orderID = Integer.parseInt(_orderID);
                employeeID = user.getId();
                logic.assignOrder(orderID, employeeID);

                response.sendRedirect("byggecenter?view=backendpage");

            } catch (NumberFormatException ex) {
                // error handling
            }
    }

}
