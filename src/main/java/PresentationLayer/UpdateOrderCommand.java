package PresentationLayer;

import data.FOGException;
import data.UpdateException;
import entity.Order;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import logic.LogicFacade;

/**
 *
 * @author caspe
 */
public class UpdateOrderCommand extends Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, FOGException {
        if(request.getSession().getAttribute("user")==null)throw new FOGException("Du skal være logget ind for at tilgå denne side.");
        LogicFacade logic = new LogicFacade();
        Order order;
        
        String _orderID = request.getParameter("orderID");
        String _carportHeight = request.getParameter("carportHeight");
        String _carportLength = request.getParameter("carportLength");
        String _carportWidth = request.getParameter("carportWidth");
        String _shedWidth = request.getParameter("shedWidth");
        String _shedLength = request.getParameter("shedLength");
        String _roofAngle = request.getParameter("roofAngle");
        String _employeeID = request.getParameter("employee");
        String _salesPrice = request.getParameter("price");
        // Should also contain status and sales price.
        String fullname = request.getParameter("fullname");
        
        try {
            int orderID = Integer.parseInt(_orderID);
            int carportHeight = Integer.parseInt(_carportHeight);
            int carportLength = Integer.parseInt(_carportLength);
            int carportWidth = Integer.parseInt(_carportWidth);
            int shedWidth = Integer.parseInt(_shedWidth);
            int shedLength = Integer.parseInt(_shedLength);
            int roofAngle = Integer.parseInt(_roofAngle);
            int employeeID = Integer.parseInt(_employeeID);
            double salesPrice = 0;
            
            class Error {
                String error = null;
                
                Error(String error){
                    this.error = error;
                }
                
            }
            
            Error err = null;
            
            if(_salesPrice != null && !(_salesPrice.isEmpty())) {
                salesPrice = Double.parseDouble(_salesPrice);
            }
            
            if(carportHeight < 180 || carportHeight > 300) {
                err = new Error("Højden skal være mellem 180 og 300");
            }
            
            if(carportLength%30 != 0 || carportLength < 240 || carportLength > 780) {
                err = new Error("Længden skal være mellem 240 og 780");
            }
            
            if(carportWidth%30 != 0 || carportWidth < 240 || carportWidth > 780) {
                err = new Error("Bredden skal være mellem 240 og 780");
            }
            
            if(shedWidth < 210 || shedWidth > 720) {
                err = new Error("Skurets bredde skal være mellem 210 og 720");
            }
            
            if(shedWidth > carportWidth-30) {
                err = new Error("Skurets bredde må ikke være større end carportens bredde");
            }
            
            if(shedLength < 150 || shedLength > 690) {
                err = new Error("Skurets længde skal være mellem 150 og 690");
            }
            
            if(shedLength > carportLength-30) {
                err = new Error("Skurets længde må ikke være større end carportens bredde");
            }
            
            if(roofAngle%5 != 0 || roofAngle < 10 || roofAngle > 45) {
                err = new Error("Taget skal være mellem 15 og 45 grader");
            }
            
            if(err != null) {
                request.setAttribute("error", err.error);
                request.getRequestDispatcher("byggecenter?view=orderinfoadmin&orderID=" + _orderID).forward(request, response);
                
                return;
            }
            
            order = new Order(orderID, employeeID, carportHeight, carportWidth, carportLength, shedLength, shedWidth, roofAngle, salesPrice);
            Order updatedOrder = logic.updateOrder(order);
            
            response.sendRedirect("byggecenter?view=orderinfoadmin&orderID=" + updatedOrder.getId());
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        } catch (UpdateException ex) {
            request.setAttribute("error", ex.getMessage());
            request.getRequestDispatcher("byggecenter?view=orderinfoadmin&orderID=" + _orderID).forward(request, response);
            
        }
    }
}
