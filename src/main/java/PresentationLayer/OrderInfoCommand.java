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
        
        int orderID = Integer.parseInt(request.getParameter("orderID"));
        Order orderToShow = lf.getOrder(orderID);
        Customer c = lf.getCustomer(orderToShow.getCustomerId());
        
        request.setAttribute("order", orderToShow);
        request.setAttribute("customer", c);
        
        request.getRequestDispatcher("/WEB-INF/orderdetails.jsp").forward(request, response);
    }
    
}
