/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PresentationLayer;

import com.itextpdf.layout.Document;
import data.FOGException;
import entity.Order;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import logic.LogicFacade;
import logic.PDFCreator;

/**
 *
 * @author Annika
 */
public class DownloadPDFCommand extends Command {
    //private ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, FOGException {
        LogicFacade lf = new LogicFacade();
        response.setContentType("application/pdf");
        PDFCreator p = new PDFCreator(response.getOutputStream());
        String _orderID = request.getParameter("orderID");
        int orderid = 0;
        Order o = null;
        try {
            orderid = Integer.parseInt(_orderID);
            o = lf.getOrder(orderid);
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        }
        
        Document doc = p.createPDF(o, request.getRequestURL().toString().replace("byggecenter", ""));
        p.closeDoc(doc);

    }
    
}
