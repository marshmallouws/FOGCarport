package PresentationLayer;

import data.UpdateException;
import entity.Order;
import java.io.IOException;
import java.sql.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import logic.LogicFacade;

/**
 *
 * @author caspe
 */
public class UpdateOrderCommand extends Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        LogicFacade logic = new LogicFacade();
        Order order;
        
        String _orderID = request.getParameter("orderID");
        String _carportHeight = request.getParameter("carportHeight");
        String _carportLength = request.getParameter("carportLength");
        String _carportWidth = request.getParameter("carportWidth");
        String _shedWidth = request.getParameter("shedWidth");
        String _shedLength = request.getParameter("shedLength");
        String _roofAngle = request.getParameter("roofAngle");
        String _employeeID = request.getParameter("employee");
        
        String fullname = request.getParameter("fullname");
        
        try {
            int orderID = Integer.parseInt(_orderID);
            int carportHeight = Integer.parseInt(_carportHeight);
            int carportLength = Integer.parseInt(_carportLength);
            int carportWidth = Integer.parseInt(_carportWidth);
            int shedWidth = Integer.parseInt(_shedWidth);
            int shedLength = Integer.parseInt(_shedLength);
            int roofAngle = Integer.parseInt(_roofAngle);
            int employeeID = Integer.parseInt(_employeeID);
            
            order = new Order(orderID, employeeID, carportHeight, carportWidth, carportLength, shedLength, shedWidth, roofAngle);
            Order updatedOrder = logic.updateOrder(order);
            
            response.sendRedirect("byggecenter?view=orderinfoadmin&orderID=" + updatedOrder.getId());
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        } catch (UpdateException ex) {
            request.setAttribute("error", ex.getMessage());
            request.getRequestDispatcher("byggecenter?view=orderinfoadmin&orderID=" + _orderID).forward(request, response);
            
        }
        
        
        
    }
    
}
