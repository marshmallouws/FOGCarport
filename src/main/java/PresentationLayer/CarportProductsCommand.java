package PresentationLayer;

import data.FOGException;
import entity.Odetail;
import entity.Order;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, FOGException {
        
        LogicFacade lf = new LogicFacade();
        
        int orderID = Integer.parseInt(request.getParameter("orderID"));
        Order order = lf.getOrder(orderID);
        
        List<Odetail> odetails;
        try {
            odetails = lf.buildCarport(order);
            request.setAttribute("carport", odetails);
        } catch (FOGException ex) {
            throw new FOGException(ex.getMessage());
        }
        
        request.setAttribute("order", order);
        
        
        request.getRequestDispatcher("/WEB-INF/carport.jsp").forward(request, response);
    }
    
}
