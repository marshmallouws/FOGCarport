/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
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
import com.itextpdf.layout.element.ListItem;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.ListNumberingType;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.svg.converter.SvgConverter;
import data.BuildException;
import data.BuilderMapper;
import data.Connector;
import data.ConnectorInterface;
import data.ProductMapper;
import entity.Blueprint;
import entity.Carport;
import entity.Category;
import entity.Odetail;
import entity.Order;
import entity.Product;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Annika
 */
public class PDFCreator {

    OutputStream outputStream;
    LogicFacade l = new LogicFacade();

    public PDFCreator(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public Document createPDF(Order order, String path) {
        Document doc = null;
        PdfDocument pdf = null;
        PdfWriter writer = null;

        try {
            writer = new PdfWriter(outputStream);
            pdf = new PdfDocument(writer);
            doc = new Document(pdf);
            doc.setMargins(130, 50, 40, 50);

            Image logo = new Image(ImageDataFactory.create(path + "/images/logo.PNG"));

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
            doc.add(new AreaBreak());
            createInstructions(order, doc);
         
            doc.add(new AreaBreak());
            pdf.addNewPage(PageSize.A4);
            addSVG(pdf,doc,order,path,1);
            addSVG(pdf,doc,order,path,2);

            //} catch (MalformedURLException ex) {
            //Logger.getLogger(PDFCreator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BuildException ex) {
            Logger.getLogger(PDFCreator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(PDFCreator.class.getName()).log(Level.SEVERE, null, ex);
        }
        return doc;

    }

    public void closeDoc(Document doc) {
        if (doc != null) {
            doc.close();
        }
    }
    
    private void addSVG(PdfDocument docu, Document doc, Order order, String path, int svg) {
        try {
            String urlString = path+"/byggecenter?view=svg&order="+order.getId()+"&display="+svg;
            URL url = new URL(urlString);
            Image image = SvgConverter.convertToImage(url.openStream(), docu);
            doc.add(image);
        } catch (Exception e) {
            //
        }
    }

    private void createInstructions(Order order, Document doc) throws BuildException {
        doc.add(new Paragraph(new Text("Husk at kontrollere styklisten inden du går i gang").setBold()));

        ConnectorInterface con = Connector.getInstance();
        ProductMapper pm = new ProductMapper(con);
        BuilderMapper bm = new BuilderMapper(con);
        List<Blueprint> blueprint = bm.getBlueprint(1);
        List<Odetail> odetails = l.buildCarport(order);
        Carport carport = new Carport(odetails, blueprint);

        com.itextpdf.layout.element.List list = new com.itextpdf.layout.element.List(ListNumberingType.DECIMAL);
        list.add(new ListItem("Grundplan afsættes ved at hamre en stump"
                + "lægte (A) i jorden til ca. markering af"
                + "carportens hjørnestolper, en pæl i hvert hjørne."));

        // stolper
        String _stolperCount = String.valueOf(carport.getCountCategory(1));
        if (order.getShedLength() != 0) {
            list.add(new ListItem("Placér de" +_stolperCount+ " skurstolper"));
        }

        // remme
        list.add(new ListItem("Når stolper er monteret, Skæres et hak/blad udvendig i toppen af hver stolpe (E)\n"
                + "til remmen (F)\n"
                + "Der skal skæres ud, så remmen flugter eller er lidt højere end toppen af stolpen,\n"
                + "Så remmen får et fald bagud på carporten.\n"
                + "Dette gøres nemmest ved at holde remmen op mod stolpen og fast holde med en skrue tvinge.\n"
                + "Sæt højden på oversiden af remmen ved forreste stolpe til, 209 cm. (Ved plan grund) og sænke\n"
                + "bagenden til 200 cm. ved bagerste stolpe, derefter kan der streges op på stolperne, så får du lavet\n"
                + "et saddelhakket med fald på. Bemærk at faldet kan øges hvis man ønsker det, ved at sænke\n"
                + "bagenden"));

        // alm. spær
        list.add(new ListItem("Universal/spær beslag til fastgørelse af spær på rem.\n"
                + "Start med at opmærke på oversiden af remmene, hvor det forreste og det bagerste spær skal\n"
                + "placeres. Opmærk på begge sidder af hvert spær, spæret skal senere placeres imellem disse to\n"
                + "streger. Der opmærkes på samme måde til de mellemliggende spær.\n"
                + "Afstanden mellem spærene skal være ens max 60.cm"));

        
        // indsæt SVG af spær
        
        // understernbræt
        Product understern = pm.getProductMain(carport.getProductUsed(4));
        String _understern = understern.getThickness() + " x " + understern.getWidth();
        
        list.add(new ListItem("Start med understernbræt (" + _understern + " mm) som skal sidde på det forreste spær (den høje ende).\n"
                + "Afkort det så det har samme længde som spæret plus 5.cm.\n"
                + "Monter sternbrættet så det er 2,5 cm længere end spæret i hver side og sternbrættets overkant\n"
                + "flugter med spæret.\n"
                + "De to under Sternbrædder i siderne monteres ligeledes så de flugter overkant af spærene, og\n"
                + "skrues fast i enderne af spærene. Længden tilpasses individuelt afhængig af om der monteres\n"
                + "tagrende. (tagrende medfølger ikke)\n"
                + "Det bagerste under sternbræt afkortes og tilpasses så det passer imellem de to side sternbrædder,\n"
                + "og skrues fast på bagerste spær. Bemærk: Overkant bagerste sternbræt skal også være lig med\n"
                + "overkant af bagerste spær.\n"
                + "Her efter kan over sternen (25x125) monteres, på forenden, samt sider, men skal placeres 30 mm.\n"
                + "Højere end spærene, således at trapez taget bliver skjult."));

        doc.add(list);

    }

    private void createFrontPage(Order order, Document doc, PdfDocument pdf) {

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

    private void createMaterialList(Order order, Document doc) throws BuildException {
        Text inf = new Text("Husk at kontrollere styklisten inden du går i gang");
        Paragraph p = new Paragraph(inf);

        doc.add(p);

        List<Odetail> details = null;
        float[] pointColumnWidths = {137F, 90F, 90F, 137F};
        Table table = new Table(pointColumnWidths);
        details = l.buildCarport(order);

        Cell c1 = new Cell();
        c1.add(new Paragraph("Beskrivelse")).setBold();

        Cell c2 = new Cell();
        c2.add(new Paragraph("Længde")).setBold();

        Cell c3 = new Cell();
        c3.add(new Paragraph("Antal")).setBold();

        Cell c4 = new Cell();
        c4.add(new Paragraph("Kommentar")).setBold();

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
                        t.addCell(new Cell(1, 4).add(new Paragraph(title)).setBold());
                        printTitle = false;
                    }

                    t.addCell(new Cell().add(new Paragraph(o.getProduct().getName())));
                    t.addCell(new Cell().add(new Paragraph(String.valueOf(o.getProduct().getLength()))));
                    t.addCell(new Cell().add(new Paragraph(String.valueOf(o.getQty()))));
                    t.addCell(new Cell().add(new Paragraph(o.getComment())));
                }
            }

            if (!t.isEmpty()) {
                doc.add(new Paragraph(new Text("\n")));
                doc.add(t);
            }

        }

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
            img.scaleToFit(300, 300);
            area.applyMargins(-15, 0, 0, -15, true);
            new Canvas(aboveCanvas, pdfDoc, area)
                    .add(img);
            

        }
    }
}
