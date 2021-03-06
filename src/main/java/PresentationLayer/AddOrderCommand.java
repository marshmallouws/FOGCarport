package PresentationLayer;

import data.BuildException;
import com.mysql.cj.util.StringUtils;
import data.FOGException;
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
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, FOGException {
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
        if (StringUtils.isStrictlyNumeric(phone) != true
                || StringUtils.isStrictlyNumeric(zip) != true
                || zip.length() != 4 || fullname.length() > 45
                || !email.contains("@") || !email.contains(".")) {
            throw new FOGException("Det ser ud til, at kundeinformationen ikke var udfyldt korrekt!");
        }

        LogicFacade lf = new LogicFacade();


        try {

            if (StringUtils.isStrictlyNumeric(_carportWidth) == false
                    || StringUtils.isStrictlyNumeric(_carportLength) == false
                    || StringUtils.isStrictlyNumeric(_shedWidth) == false
                    || StringUtils.isStrictlyNumeric(_shedLength) == false
                    || StringUtils.isStrictlyNumeric(_roofAngle) == false
                    || StringUtils.isStrictlyNumeric(_carportHeight) == false) {
                throw new FOGException("Kun tal kan bruges til at oprette en ordre.");
            } else {
                if (Integer.parseInt(_carportWidth) < 240 || Integer.parseInt(_carportWidth) > 750) {
                    throw new FOGException("Det ser ud til, at størrelse på carporten er for stor! Kontakt support.");
                }

                if (Integer.parseInt(_carportLength) < 240 || Integer.parseInt(_carportLength) > 780) {
                    throw new FOGException("Det ser ud til, at størrelse på carporten er for stor! Kontakt support.");
                }

                if (Integer.parseInt(_roofAngle) < 0 || Integer.parseInt(_roofAngle) > 45) {
                    throw new FOGException("Det ser ud til, at vinklen på carportens tag er for stor! Kontakt support.");
                }

                if (Integer.parseInt(_carportHeight) < 225 || Integer.parseInt(_carportHeight) > 500) {
                    throw new FOGException("Det ser ud til, at højden på carporten er for stor! Kontakt support.");
                }

                if (Integer.parseInt(_shedWidth) != 0 && (Integer.parseInt(_shedWidth) < 210 || Integer.parseInt(_shedWidth) > 720)) {
                    throw new FOGException("Det ser ud til, at skuret på carporten ikke passer! Kontakt support.");
                }

                if (Integer.parseInt(_shedLength) != 0 && (Integer.parseInt(_shedLength) < 150 || Integer.parseInt(_shedLength) > 690)) {
                    throw new FOGException("Det ser ud til, at skuret på carporten ikke passer! Kontakt support.");
                }
            }

            int height = Integer.parseInt(_carportHeight); // skal implementeres som på order admin page
            int carportWidth = Integer.parseInt(_carportWidth);
            int carportLength = Integer.parseInt(_carportLength);
            int shedWidth = Integer.parseInt(_shedWidth);
            int shedLength = Integer.parseInt(_shedLength);
            int roofAngle = Integer.parseInt(_roofAngle);
            int roofType = Integer.parseInt(_roofType);
            if (shedWidth == 0 || shedLength == 0) {
                shedWidth = 0;
                shedLength = 0;
            }
            Order order = new Order(height, carportWidth, carportLength, shedWidth, shedLength, roofAngle, roofType);
            int orderID = lf.createOrder(order, fullname, email, address, Integer.parseInt(zip), Integer.parseInt(phone));
            
            String json;
            response.setContentType("text/plain");
            response.setCharacterEncoding("UTF-8");
            if (orderID > 0) {
                order.setId(orderID);
                lf.createOdetail(lf.buildCarport(order));
                json = ""+orderID;
            } else {
                json = "error";
            }
            response.getWriter().write(json);

        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        } catch (BuildException ex) {
            throw new FOGException(ex.getMessage());
        }

    }

}
