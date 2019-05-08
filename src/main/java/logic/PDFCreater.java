/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import com.itextpdf.kernel.pdf.PdfDocument; 
import com.itextpdf.kernel.pdf.PdfWriter; 
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import entity.Order;
import java.io.FileNotFoundException;

/**
 *
 * @author Annika
 */
public class PDFCreater {

    //LogicFacade l = new LogicFacade();

    public void createPDF(int id) {
        //Order order = l.getOrder(id);
        
        try {
            PdfWriter writer = new PdfWriter("ordreno" + id + ".pdf");
            PdfDocument pdf = new PdfDocument(writer);
            pdf.addNewPage();
            
            Document doc = new Document(pdf);
            doc.add(new Paragraph("Ordre nummer " + id));
            
            doc.close();
            
        } catch (FileNotFoundException ex) {
            
        }
        
        
        
    }

    public static void main(String[] args) {
        
        PDFCreater p = new PDFCreater();
        p.createPDF(1);

    }
}
