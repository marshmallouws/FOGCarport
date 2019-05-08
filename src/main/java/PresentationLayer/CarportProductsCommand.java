package PresentationLayer;

import entity.Odetail;
import entity.Order;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import logic.LogicFacade;

/**
 *
 * @author Casper
 */
public class CarportProductsCommand extends Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        LogicFacade lf = new LogicFacade();
        
        int orderID = Integer.parseInt(request.getParameter("orderID"));
        Order order = lf.getOrder(orderID);
        
        List<Odetail> odetails = lf.buildCarport(order);
        
        request.setAttribute("order", order);
        request.setAttribute("carport", odetails);
        
        request.getRequestDispatcher("/WEB-INF/carport.jsp").forward(request, response);
    }
    
}
