package PresentationLayer;

import entity.Order;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import logic.LogicFacade;

/**
 *
 * @author Casper
 */
public class AddOrderCommand extends Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String _carportWidth = request.getParameter("carportWidth");
        String _carportLength = request.getParameter("carportLength");
        String _shedWidth = request.getParameter("shedWidth");
        String _shedLength = request.getParameter("shedLength");
        String _roofAngle = request.getParameter("roofAngle");
        
        String fullname = request.getParameter("fullname");
        String address = request.getParameter("address");
        String zip = request.getParameter("zip");
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");
        String message = request.getParameter("message");
        
        LogicFacade lf = new LogicFacade();

        try {
            int height = 0; // skal beregnes hvis roofAngle > 0
            int carportWidth = Integer.parseInt(_carportWidth);
            int carportLength = Integer.parseInt(_carportLength);
            int shedWidth = Integer.parseInt(_shedWidth);
            int shedLength = Integer.parseInt(_shedLength);
            int roofAngle = Integer.parseInt(_roofAngle);

            Order order = new Order(height, carportWidth, carportLength, shedWidth, shedLength, roofAngle);
            boolean success = lf.createOrder(order);
            
            request.setAttribute("succes", success);
            request.getRequestDispatcher("./landingpage.jsp").forward(request, response);

        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        }

    }

}
