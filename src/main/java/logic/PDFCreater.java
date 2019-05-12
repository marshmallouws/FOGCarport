/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import entity.Order;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Annika
 */
public class PDFCreater {

    LogicFacade l = new LogicFacade();

    public void createPDF(int id) {

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

    public void createFrontPage(int id) {
        try {
            PdfWriter writer = new PdfWriter("ordreno" + id + ".pdf");
            PdfDocument pdf = new PdfDocument(writer);
            pdf.addNewPage();

            Document doc = new Document(pdf);

            PdfFont font = PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD);

            Order order = l.getOrder(1);
            Text info = new Text("Ordre nummer:" + id);
            Text date = new Text(order.getDate().substring(0, 10));

            Paragraph pi = new Paragraph(info);
            pi.setTextAlignment(TextAlignment.CENTER);
            pi.setPadding(100);
            pi.setFontSize(20);

            Paragraph pd = new Paragraph(date);
            pd.setTextAlignment(TextAlignment.CENTER);
            pd.setPaddingTop(10);
            pd.setFontSize(20);

            doc.add(pi);
            doc.add(pd);
            
            createFooter(doc);
            
            pdf.addNewPage(PageSize.A4);
            doc.add(new AreaBreak());
            body(doc, pdf);
            
                        
            try {
                header("VÆREBRO\nCARPORTE", 25, pdf, doc);
            } catch (Exception ex) {
                // IGNORE THIS BITCH
            }
            
            doc.close();

        } catch (FileNotFoundException ex) {

        } catch (IOException e) {

        }
    }
    
    private void body(Document doc, PdfDocument pdfDoc) {
        Text inf = new Text("Husk at kontrollere styklisten inden du går i gang");
        Paragraph p = new Paragraph(inf);
        p.setPaddingTop(100);
     
        doc.add(p);
    }
    
    
    private void header(String title, int fontsize, PdfDocument pdfDoc, Document doc) throws Exception {
        Paragraph header = new Paragraph(title)
                .setFont(PdfFontFactory.createFont(FontConstants.HELVETICA))
                .setFontSize(fontsize);
        float x, y;
        for (int i = 1; i <= pdfDoc.getNumberOfPages(); i++) {
            pdfDoc.getPage(i).setIgnorePageRotationForContent(true);
            //System.out.println(pdfDoc.getPage(i).getRotation());
            if (pdfDoc.getPage(i).getRotation() % 180 == 0) {
                x = pdfDoc.getPage(i).getPageSize().getWidth() / 2;
                y = pdfDoc.getPage(i).getPageSize().getTop() - 120;
            } else {
                System.out.println("rotated");
                x = pdfDoc.getPage(i).getPageSize().getHeight() / 2;
                y = pdfDoc.getPage(i).getPageSize().getRight() - 20;
            }
 
            doc.showTextAligned(header, x, y, i, TextAlignment.CENTER, VerticalAlignment.BOTTOM, 0);
        }
    }

    public void createFooter(Document doc) {
        try {
            String imageFile = "logo.png";
            ImageData data = ImageDataFactory.create(imageFile);
            Image img = new Image(data);
            img.setFixedPosition(400, 100);
            doc.add(img);
        } catch (MalformedURLException ex) {
            Logger.getLogger(PDFCreater.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {

        PDFCreater p = new PDFCreater();
        p.createFrontPage(1);

    }
}
