/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PresentationLayer;

import data.FOGException;
import entity.Odetail;
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
public class CarportProductsEditCommand extends Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, FOGException {
        if(request.getSession().getAttribute("user")==null)throw new FOGException("Du skal være logget ind for at tilgå denne side.");
        LogicFacade logic = new LogicFacade();
        
        List<Odetail> odetails = new ArrayList();
        
        int orderID = Integer.parseInt(request.getParameter("orderID"));
        String[] ids = request.getParameterValues("id[]");
        String[] comments = request.getParameterValues("comments[]");
        
        
        int counter = 0;
        for (String i : ids ) {
            int id = Integer.parseInt(i);
            odetails.add(new Odetail(id, comments[counter]));
            counter++;
        }
        
        logic.editOdetails(odetails);
        
        response.sendRedirect("byggecenter?view=carport&orderID=" + orderID);
        
    }
    
}
