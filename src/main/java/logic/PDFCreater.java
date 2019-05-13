/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import data.FOGException;
import entity.Category;
import entity.Employee;
import entity.Odetail;
import entity.Order;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Annika
 */
public class PDFCreater {

    LogicFacade l = new LogicFacade();

    public void createPDF(Order order) {
        Document doc = null;
        PdfDocument pdf = null;

        try {
            pdf = new PdfDocument(new PdfWriter("Ordre" + order.getId() + ".pdf"));
            doc = new Document(pdf);
            doc.setMargins(170, 50, 40, 50);
            
            Image logo = new Image(ImageDataFactory.create("logo.png"));
            
            ImageEventHandler handler = new ImageEventHandler(logo);
            pdf.addEventHandler(PdfDocumentEvent.END_PAGE, handler);

            createFrontPage(order, doc, pdf);
            //createFooter(doc);

            pdf.addNewPage(PageSize.A4);
            doc.add(new AreaBreak());

            createMaterialList(order, doc);
            /*
            //TODO: Observe if header is correct when adding new pages
            try {
                //createHeader("VÆREBRO\nCARPORTE", 25, pdf, doc);
                manipulatePdf(doc, pdf);
            } catch (Exception ex) {
                // IGNORE THIS
            } */

        } catch (FileNotFoundException ex) {

        } catch (MalformedURLException ex) {
            Logger.getLogger(PDFCreater.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // lets guard it from null pointer exception
            if (doc != null) {
                doc.close();
            }
        }

    }

    public void createFrontPage(Order order, Document doc, PdfDocument pdf) {

        Text info = new Text("Ordre nummer:" + order.getId());
        Text date = new Text(order.getDate().substring(0, 10));

        Paragraph pi = new Paragraph(info);
        pi.setTextAlignment(TextAlignment.CENTER);
        pi.setFontSize(20);

        Paragraph pd = new Paragraph(date);
        pd.setTextAlignment(TextAlignment.CENTER);
        pd.setFontSize(20);

        doc.add(pi);
        doc.add(pd);
    }

    private void createHeader(String title, int fontsize, PdfDocument pdfDoc, Document doc) throws Exception {
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

    private void createFooter(Document doc) {
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

    private void createMaterialList(Order order, Document doc) {
        Text inf = new Text("Husk at kontrollere styklisten inden du går i gang");
        Paragraph p = new Paragraph(inf);

        doc.add(p);

        List<Odetail> details = null;
        float[] pointColumnWidths = {137F, 90F, 90F, 137F};
        Table table = new Table(pointColumnWidths);
        try {
            details = l.buildCarport(order);
        } catch (FOGException ex) {
            Logger.getLogger(PDFCreater.class.getName()).log(Level.SEVERE, null, ex);
        }

        Cell c1 = new Cell();
        c1.add("Beskrivelse").setBold();

        Cell c2 = new Cell();
        c2.add("Længde").setBold();

        Cell c3 = new Cell();
        c3.add("Antal").setBold();

        Cell c4 = new Cell();
        c4.add("Kommentar").setBold();

        table.addCell(c1);
        table.addCell(c2);
        table.addCell(c3);
        table.addCell(c4);

        doc.add(table);

        List<Category> categories = l.getCategorieslist();

        /* TODO: Optimize to prevent looping through all 
            Odetails for every instance of Category. Maybe this should be
            done in the method buildCarport so that it returns a Map */
        for (Category cat : categories) {

            Table t = new Table(pointColumnWidths);
            String title = cat.getName();

            boolean printTitle = true;

            for (Odetail o : details) {
                if (o.getProduct().getCategory().getName().equals(title)) {

                    if (printTitle) {
                        t.addCell(new Cell(1, 4).add(title).setBold());
                        printTitle = false;
                    }

                    t.addCell(new Cell().add(o.getProduct().getName()));
                    t.addCell(new Cell().add(String.valueOf(o.getProduct().getLength())));
                    t.addCell(new Cell().add(String.valueOf(o.getQty())));
                    t.addCell(new Cell().add(o.getComment()));
                }
            }

            if (!t.isEmpty()) {
                doc.add(new Paragraph(new Text("\n")));
                doc.add(t);
            }

        }

    }

    public static void main(String[] args) {

        PDFCreater p = new PDFCreater();
        LogicFacade l = new LogicFacade();
        Employee empl = new Employee(1, "aaa", "bbb");
        Order order = l.getOrder(3);
        //Order order = l.getOrder(1);
        p.createPDF(order);

    }

    public class ImageEventHandler implements IEventHandler {

        protected Image img;


        public ImageEventHandler(Image img) {
            this.img = img;
        }

        @Override
        public void handleEvent(Event event) {
            PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
            PdfDocument pdfDoc = docEvent.getDocument();
            PdfPage page = docEvent.getPage();
            PdfCanvas aboveCanvas = new PdfCanvas(page.newContentStreamAfter(),
                    page.getResources(), pdfDoc);
            Rectangle area = page.getPageSize();
            new Canvas(aboveCanvas, pdfDoc, area)
                    .add(img);
            
        }
    }
}
