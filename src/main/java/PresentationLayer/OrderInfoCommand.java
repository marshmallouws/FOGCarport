package PresentationLayer;

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
        
        int orderID = (int)request.getAttribute("orderID");
        Order orderToShow = lf.getOrder(orderID);
        
        request.setAttribute("order", orderToShow);
        
        request.getRequestDispatcher("./orderdetials.jsp").forward(request, response);
    }
    
}
