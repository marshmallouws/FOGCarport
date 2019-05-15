package PresentationLayer;

import com.mysql.cj.util.StringUtils;
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
        String _carportHeight = request.getParameter("carportHeight");
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
        
        // validate info
        if (StringUtils.isStrictlyNumeric(phone) != true || 
                StringUtils.isStrictlyNumeric(zip) != true || 
                zip.length() > 4 || 
                zip.length() < 4 || fullname.length() > 45 ||
                !email.contains("@") || !email.contains(".")) 
        {
            request.setAttribute("error", "Det ser ud til, at kunde informationen ikke var udfyldt korrekt!");
            request.getRequestDispatcher("/WEB-INF/errorpage.jsp").forward(request, response);
            return;
        }

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
            
            if (StringUtils.isStrictlyNumeric(_carportWidth) == false || 
                StringUtils.isStrictlyNumeric(_carportLength) == false || 
                StringUtils.isStrictlyNumeric(_shedWidth) == false || 
                StringUtils.isStrictlyNumeric(_shedLength) == false || 
                StringUtils.isStrictlyNumeric(_roofAngle) == false || 
                StringUtils.isStrictlyNumeric(_carportHeight) == false) 
            {
                request.setAttribute("error", "Kun tal kan bruges til at oprette en ordre.");
                request.getRequestDispatcher("/WEB-INF/errorpage.jsp").forward(request, response);
                return;
            } else {
                if (Integer.parseInt(_carportWidth) < 240 || Integer.parseInt(_carportWidth) > 750) {
                    request.setAttribute("error", "Det ser ud til, at størrelse på carporten er for stor! Kontakt support.");
                    request.getRequestDispatcher("/WEB-INF/errorpage.jsp").forward(request, response);
                    return;
                }
                
                if (Integer.parseInt(_carportLength) < 240 || Integer.parseInt(_carportLength) > 780) {
                    request.setAttribute("error", "Det ser ud til, at størrelse på carporten er for stor! Kontakt support.");
                    request.getRequestDispatcher("/WEB-INF/errorpage.jsp").forward(request, response);
                    return;
                }
                
                if (Integer.parseInt(_roofAngle) < 0 || Integer.parseInt(_roofAngle) > 45) {
                    request.setAttribute("error", "Det ser ud til, at vinklen på carportens tag er for stor! Kontakt support.");
                    request.getRequestDispatcher("/WEB-INF/errorpage.jsp").forward(request, response);
                    return;
                }
                
                if (Integer.parseInt(_carportHeight) < 225 || Integer.parseInt(_carportHeight) > 500) {
                    request.setAttribute("error", "Det ser ud til, at højden på carporten er for stor! Kontakt support.");
                    request.getRequestDispatcher("/WEB-INF/errorpage.jsp").forward(request, response);
                    return;
                }
            }
            
            int height = Integer.parseInt(_carportHeight); // skal implementeres som på order admin page
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
                
                lf.createOdetail(lf.buildCarport(orderNew));
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
