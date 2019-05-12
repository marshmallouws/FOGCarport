package PresentationLayer;

import data.DevMapper;
import data.FOGException;
import entity.Customer;
import entity.Order;
import java.io.IOException;
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
public class AddOrderCommand extends Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String _carportWidth = request.getParameter("carportWidth");
        String _carportLength = request.getParameter("carportLength");
        String _shedWidth = request.getParameter("shedWidth");
        String _shedLength = request.getParameter("shedLength");
        String _roofAngle = request.getParameter("roofAngle");
        String _roofType = request.getParameter("roofType");

        String fullname = request.getParameter("fullname");
        String address = request.getParameter("address");
        String zip = request.getParameter("zip");
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");
        String message = request.getParameter("message");

        LogicFacade lf = new LogicFacade();
        Customer c = null;
        try {
            int formatZip = Integer.parseInt(zip);
            int formatPhone = Integer.parseInt(phone);

            c = new Customer(fullname, email, address, formatZip, formatPhone);
            int custID = lf.createCustomer(c);
            
            c.setID(custID);
            
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        }

        try {
            int height = 0; // skal implementeres som på order admin page
            int carportWidth = Integer.parseInt(_carportWidth);
            int carportLength = Integer.parseInt(_carportLength);
            int shedWidth = Integer.parseInt(_shedWidth);
            int shedLength = Integer.parseInt(_shedLength);
            int roofAngle = Integer.parseInt(_roofAngle);
            int roofType = Integer.parseInt(_roofType);

            Order order = new Order(height, carportWidth, carportLength, shedWidth, shedLength, roofAngle, roofType);
            int orderID = lf.createOrder(order, c);
            
            
            if (orderID > 0) {
                Order orderNew = lf.getOrder(orderID);
                
                lf.createOdetail(new DevMapper().buildCarport(orderNew));
                request.setAttribute("success", true);
            } else {
                request.setAttribute("success", false);
            }
            request.getRequestDispatcher("/WEB-INF/landingpage.jsp").forward(request, response);

        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        } catch (FOGException ex) {
            ex.printStackTrace();
        }

    }

}
