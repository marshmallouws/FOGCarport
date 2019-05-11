/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PresentationLayer;

import data.FOGException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author caspe
 */
public class CarportProductsEditCommand extends Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, FOGException {
        
        String[] id = request.getParameterValues("id[]");
        String[] comments = request.getParameterValues("comments[]");
        
        for (int i = 0; i < comments.length; i++) {
            System.out.println(id[i]);
            System.out.println(comments[i]);
        }
        
    }
    
}
