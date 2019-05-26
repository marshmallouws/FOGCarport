package PresentationLayer;

import data.FOGException;
import entity.Carport;
import entity.Customer;
import entity.Order;
import entity.Employee;
import entity.Odetail;
import entity.Product;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import logic.LogicFacade;

/**
 *
 * @author caspe
 */
public class OrderInfoAdminCommand extends Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, FOGException {
        if(request.getSession().getAttribute("user")==null)throw new FOGException("Du skal være logget ind for at tilgå denne side.");
        
        LogicFacade lf = new LogicFacade();
        
        int interval = 30;
        
        int carportMinWidth = 240;
        int carportMaxWidth = 750;
        int carportMinLength = 240;
        int carportMaxLength = 780;
        int carportMinHeight = 180;
        int carportMaxHeight = 300;
        
        int shedMinWidth = 210;
        int shedMaxWidth = 720;
        int shedMinLength = 150;
        int shedMaxLength = 690;
        
        int roofInterval = 5;
        int roofMinAngle = 15;
        int roofMaxAngle = 45;
        
        List<Integer> carportSelectWidth = new ArrayList();
        List<Integer> carportSelectLength = new ArrayList();
        List<Integer> carportSelectHeight = new ArrayList();
        List<Integer> shedSelectWidth = new ArrayList();
        List<Integer> shedSelectLength = new ArrayList();
        List<Integer> roofSelectAngle = new ArrayList();
        List<Product> roofTypes = lf.getRoofTypes();
        
        List<Employee> employees = lf.getEmployees();
        
        // carport width
        for (int i = carportMinWidth; i <= carportMaxWidth; i += interval) {
            carportSelectWidth.add(i);
        }
        
        // carport length
        for (int i = carportMinLength; i <= carportMaxLength; i += interval) {
            carportSelectLength.add(i);
        }
        
        // carport height
        for (int i = carportMinHeight; i <= carportMaxHeight; i += interval) {
            carportSelectHeight.add(i);
        }
        
        // shed width
        for (int i = shedMinWidth; i <= shedMaxWidth; i += interval) {
            shedSelectWidth.add(i);
        }
        
        // shed length
        for (int i = shedMinLength; i <= shedMaxLength; i += interval) {
            shedSelectLength.add(i);
        }
        
        // roof angle
        for (int i = roofMinAngle; i <= roofMaxAngle; i += roofInterval) {
            roofSelectAngle.add(i);
        }
        
        request.setAttribute("carportSelectWidth", carportSelectWidth);
        request.setAttribute("carportSelectLength", carportSelectLength);
        request.setAttribute("carportSelectHeight", carportSelectHeight);
        request.setAttribute("shedSelectWidth", shedSelectWidth);
        request.setAttribute("shedSelectLength", shedSelectLength);
        request.setAttribute("roofSelectAngle", roofSelectAngle);
        request.setAttribute("roofTypes", roofTypes);
        
        request.setAttribute("employees", employees);
        
        int orderID = 0;
        Order orderToShow = null;
        Customer customer = null;
        Carport carport = null;
        
        try {
            orderID = Integer.parseInt(request.getParameter("orderID"));
            
            orderToShow = lf.getOrder(orderID);
            customer = lf.getCustomer(orderToShow.getCustomerId());
            List<Odetail> odetails = lf.getOdetails(orderID);
            carport = new Carport(odetails);
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            throw new FOGException("Ugyldigt input. Kontakt support.");
        }
        
        if (orderToShow == null || customer == null) {
            throw new FOGException("Kunne ikke få detajler om ordren. Kontakt support.");
        } else {
            request.setAttribute("customer", customer);
            request.setAttribute("order", orderToShow);
            request.setAttribute("carport", carport);
            request.getRequestDispatcher("/WEB-INF/orderdetails-admin.jsp").forward(request, response);
        }
        
    }
    
}
