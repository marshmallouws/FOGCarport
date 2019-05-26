package PresentationLayer;

import data.FOGException;
import entity.Customer;
import entity.Order;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import logic.LogicFacade;

/**
 *
 * @author Martin
 */
public class OrderInfoCommand extends Command {

    LogicFacade lf = new LogicFacade();
    
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, FOGException {
        
        int orderID = 0;
        
        try {
            orderID = Integer.parseInt(request.getParameter("orderID"));
        } catch (NumberFormatException e) {
            throw new FOGException("Ugyldigt input. Kontakt support.");
        }
        
        
        Order orderToShow = lf.getOrder(orderID);
        Customer c = lf.getCustomer(orderToShow.getCustomerId());
        
        if (orderToShow == null || c == null) {
            throw new FOGException("Kunne ikke få detajler om ordren. Kontakt support.");
        } else {
            request.setAttribute("order", orderToShow);
            request.setAttribute("customer", c);
            
            request.getRequestDispatcher("/WEB-INF/orderdetails.jsp").forward(request, response);
        }
        
    }
    
}
