package PresentationLayer;

import entity.User;
import java.io.IOException;
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
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        LogicFacade logic = new LogicFacade();
        
        String _orderID = request.getParameter("orderID");
        int orderID;
        int employeeID;
        
        if (user == null) {
            // error handling
        } else {
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
    
}
