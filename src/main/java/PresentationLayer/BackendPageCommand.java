package PresentationLayer;

import data.OrderMapper;
import entity.Order;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Casper
 */
public class BackendPageCommand extends Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        OrderMapper om = new OrderMapper();
        List<Order> orders = om.getOrders();
        
        request.setAttribute("orders", orders);
        
        request.getRequestDispatcher("./backendpage.jsp").forward(request, response);
        
    }
    
}
