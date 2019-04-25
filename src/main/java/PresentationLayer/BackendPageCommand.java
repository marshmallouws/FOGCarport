package PresentationLayer;

import data.OrderMapper;
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
public class BackendPageCommand extends Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        LogicFacade logic = new LogicFacade();
        List<Order> orders = logic.getOrders();
        
        request.setAttribute("orders", orders);
        
        request.getRequestDispatcher("./backendpage.jsp").forward(request, response);
        
    }
    
}
