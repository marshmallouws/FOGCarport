package PresentationLayer;

import data.FOGException;
import entity.Employee;
import entity.Order;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import logic.LogicFacade;

/**
 *
 * @author Casper
 */
public class BackendPageCommand extends Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, FOGException {
        if(request.getSession().getAttribute("user")==null)throw new FOGException("Du skal være logget ind for at tilgå denne side.");
        HttpSession session = request.getSession();
        
        
        Employee currLogged = (Employee)session.getAttribute("user");
        
        if (currLogged == null) {

            response.sendRedirect("");
            
        } else {
        
            LogicFacade logic = new LogicFacade();
            List<Order> myOrders = logic.getOwnOrders(((Employee)session.getAttribute("user")).getId());
            List<Order> unassignedOrders = logic.getOrdersUnassigned();


            request.setAttribute("myOrders", myOrders);
            request.setAttribute("unassignedOrders", unassignedOrders);

            request.getRequestDispatcher("/WEB-INF/backendpage.jsp").forward(request, response);
        
        }
        
    }
    
}
