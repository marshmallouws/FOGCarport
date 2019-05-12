/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PresentationLayer;

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
public class OrderPageCommand extends Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        LogicFacade logic = new LogicFacade();
        
        int interval = 30;
        
        int carportMinWidth = 240;
        int carportMaxWidth = 750;
        int carportMinLength = 240;
        int carportMaxLength = 780;
        
        int shedMinWidth = 210;
        int shedMaxWidth = 720;
        int shedMinLength = 150;
        int shedMaxLength = 690;
        
        List<Integer> carportSelectWidth = new ArrayList();
        List<Integer> carportSelectLength = new ArrayList();
        List<Integer> shedSelectWidth = new ArrayList();
        List<Integer> shedSelectLength = new ArrayList();
        List<Product> roofTypes = logic.getRoofTypes();
        
        for (int i = carportMinWidth; i <= carportMaxWidth; i += interval) {
            carportSelectWidth.add(i);
        }
        
        for (int i = carportMinLength; i <= carportMaxLength; i += interval) {
            carportSelectLength.add(i);
        }
        
        for (int i = shedMinWidth; i <= shedMaxWidth; i += interval) {
            shedSelectWidth.add(i);
        }
        
        for (int i = shedMinLength; i <= shedMaxLength; i += interval) {
            shedSelectLength.add(i);
        }
        
        request.setAttribute("carportSelectWidth", carportSelectWidth);
        request.setAttribute("carportSelectLength", carportSelectLength);
        request.setAttribute("shedSelectWidth", shedSelectWidth);
        request.setAttribute("shedSelectLength", shedSelectLength);
        
        request.setAttribute("roofTypes", roofTypes);
        
        request.getRequestDispatcher("/WEB-INF/orderpage.jsp").forward(request, response);

    }

}
