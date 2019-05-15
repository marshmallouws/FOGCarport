package PresentationLayer;

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
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        int orderID = 0;
        
        try {
            orderID = Integer.parseInt(request.getParameter("orderID"));
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Ugyldigt input. Kontakt support.");
            request.getRequestDispatcher("/WEB-INF/errorpage.jsp").forward(request, response);
            return;
        }
        
        
        Order orderToShow = lf.getOrder(orderID);
        Customer c = lf.getCustomer(orderToShow.getCustomerId());
        
        if (orderToShow == null || c == null) {
            request.setAttribute("error", "Kunne ikke få detajler om ordren. Kontakt support.");
            request.getRequestDispatcher("/WEB-INF/errorpage.jsp").forward(request, response);
            return;
        } else {
            request.setAttribute("order", orderToShow);
            request.setAttribute("customer", c);
            
            request.getRequestDispatcher("/WEB-INF/orderdetails.jsp").forward(request, response);
        }
        
    }
    
}
