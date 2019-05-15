package data;

import entity.Carport;
import entity.Category;
import entity.Odetail;
import entity.Order;
import entity.Orequest;
import entity.Product;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Casper
 */
public class Builder {

    public static void main(String[] args) {
        Order order = new Order(270, 1900, 1900, 200, 200, 20, 12);
        
        List<Orequest> blueprint = null;
        try {
            blueprint = new Builder().carportBlueprint(null);
            
            for (Odetail o : new Builder().carportBuilder(blueprint, order)) {
            System.out.println(o.getProduct().getName() + " " + o.getProduct().getLength() + " cm. " + o.getQty() + " stk. " + o.getAmount() + " kr. " + " " + o.getComment() + " " + o.getProduct().isActive());
        }
        } catch (BuildException ex) {
            System.out.println("Fail..");
        }
        
    }

    // width afgør hvor mange rækker
    private Map<Integer, Integer> calcSpaerMap(int categoryID, int productID, int x, int y, boolean angled) throws BuildException {
        Map<Integer, Integer> map = new HashMap();
        
        if(categoryID == 0 || productID == 0) {
            throw new BuildException("Intet produkt valgt til at udregne spær");
        }
        
        if (x == 0 || y == 0) {
            throw new BuildException("Mangler mål til udregning af spær");
        }
        
        List<Product> woods = getProductsAllForBuild(categoryID, productID);
        
        if (woods == null || woods.isEmpty()) {
            throw new BuildException(("Ingen produkter med categoryID; " + categoryID + " og productID: " + productID +  " blev fundet til at udregne spær"));
        }

        int qty = 10; // minimum
        int gap = x / qty;

        while (gap >= 60) {
            qty += 5;
            gap = x / qty;
        }

        int len = y;

        int max = calcMaxLength(woods);
        int min = calcMinLength(woods);

        while (len > 0) {
            if (len > max) {
                map.put(max, map.getOrDefault(max, 0) + 1);
                len -= max;
            } else if (len < min) {
                map.put(min, map.getOrDefault(min, 0) + 1);
                len -= min;
            } else {
                for (Product p : woods) {
                    if (p.getLength() > len) {
                        map.put(p.getLength(), map.getOrDefault(p.getLength(), 0) + 1);
                        len -= p.getLength();
                        break;
                    }
                }
            }
        }

        if (angled) {
            for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
                map.put(entry.getKey(), (entry.getValue() * 2) * qty);
            }
        } else {
            for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
                map.put(entry.getKey(), entry.getValue() * qty);
            }
        }

        return map;
    }

    private Map<Integer, Integer> calcWoodsMap(int categoryID, int productID, int length) throws BuildException {
        Map<Integer, Integer> map = new HashMap();
        List<Product> woods = getProductsAllForBuild(categoryID, productID);

        int len = length;
        int max = calcMaxLength(woods);
        int min = calcMinLength(woods);

        while (len > 0) {
            if (len > max) {
                map.put(max, map.getOrDefault(max, 0) + 1);
                len -= max;
            } else if (len < min) {
                map.put(min, map.getOrDefault(min, 0) + 1);
                len -= min;
            } else {
                for (Product p : woods) {
                    if (p.getLength() > len) {
                        map.put(p.getLength(), map.getOrDefault(p.getLength(), 0) + 1);
                        len -= p.getLength();
                        break;
                    }
                }
            }
        }

        return map;
    }

    // Beregner antallet af produkter til et tag
    private Map<Integer, Integer> calcRoofMap(int categoryID, int productID, Order order) {
        try {
            Map<Integer, Integer> map = new HashMap();
            List<Product> roofs = getProductsAllForBuild(categoryID, productID);

            if (roofs == null || roofs.isEmpty()) {
                throw new Exception("Ingen produkter med categoryID " + categoryID + " og productID " + productID + " blev fundet til at beregne taget..");
            }

            int width = roofs.get(0).getWidth() / 10; // målet angives i mm. i databasen
            int carportLength = order.getLenght();
            int carportWidth;

            // tjekker om taget er fladt
            if (order.getRoofAngle() == 0) {
                carportWidth = order.getWidth();
            } else {
                carportWidth = (int) calcRoofAngledLength(order);
            }

            int rows = 0;
            int cols = 0;

            int max = calcMaxLength(roofs);
            int min = calcMinLength(roofs);

            // rows
            while (carportLength > 0) {
                rows++;
                carportLength -= width;
            }

            // cols
            while (carportWidth > 0) {
                if (carportWidth >= max) {
                    map.put(max, map.getOrDefault(max, 0) + 1);
                    cols++;
                    carportWidth -= max;
                } else if (carportWidth <= min) {
                    map.put(min, map.getOrDefault(min, 0) + 1);
                    cols++;
                    carportWidth -= min;
                } else {
                    for (Product p : roofs) {
                        if (p.getLength() >= carportWidth) {
                            map.put(p.getLength(), map.getOrDefault(p.getLength(), 0) + 1);
                            cols++;
                            carportWidth -= p.getLength();
                            break;
                        }
                    }
                }
            }

            if (order.getRoofAngle() == 0) {
                // én side ved fladt tag
                for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
                    map.put(entry.getKey(), entry.getValue() * rows);
                }
            } else {
                // to sider ved hældning
                for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
                    map.put(entry.getKey(), (entry.getValue() * 2) * rows);
                }
            }

            return map;

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

    }

    private Map<Integer, Integer> calcSurfaceMap(int x, int y, int categoryID, int productID, Order order, int sides) {
        try {
            Map<Integer, Integer> map = new HashMap();
            List<Product> woods = getProductsAllForBuild(categoryID, productID);

            if (woods == null || woods.isEmpty()) {
                throw new Exception("Ingen produkter med categoryID " + categoryID + " og productID " + productID + " blev fundet til at beregne taget..");
            }

            int wid = woods.get(0).getWidth() / 10; // målet angives i mm. i databasen

            int length = x;
            int width = y;

            int rows = 0;
            int cols = 0;

            int max = calcMaxLength(woods);
            int min = calcMinLength(woods);

            // rows
            while (length > 0) {
                rows++;
                length -= wid;
            }

            // cols
            while (width > 0) {
                if (width >= max) {
                    map.put(max, map.getOrDefault(max, 0) + 1);
                    cols++;
                    width -= max;
                } else if (width <= min) {
                    map.put(min, map.getOrDefault(min, 0) + 1);
                    cols++;
                    width -= min;
                } else {
                    for (Product p : woods) {
                        if (p.getLength() >= width) {
                            map.put(p.getLength(), map.getOrDefault(p.getLength(), 0) + 1);
                            cols++;
                            width -= p.getLength();
                            break;
                        }
                    }
                }
            }
            // én side ved fladt tag
            for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
                map.put(entry.getKey(), (entry.getValue() * sides) * rows);
            }

            return map;

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

    }

    // henter alle varianter af et bestemt produkt i en kategori
    public List<Product> getProductsAllForBuild(int categoryID, int productID) {
        List<Product> products = new ArrayList();
        try {
            Connection con = Connector.connection();
            String query = "SELECT product_variants.product_id, product_variants.id, products_in_categories.category_id, categories.category_name, products.thickness, products.width, length, price, stock, product_variants.active, products.product_name FROM carports.product_variants\n"
                    + "JOIN products_in_categories ON product_variants.product_id = products_in_categories.product_id\n"
                    + "JOIN products ON product_variants.product_id = products.id\n"
                    + "JOIN categories ON products_in_categories.category_id = categories.id\n"
                    + "WHERE category_id = ? AND product_variants.product_id = ?;";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, categoryID);
            ps.setInt(2, productID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("product_id");
                int variant_id = rs.getInt("id");
                Category category = new Category(rs.getInt("category_id"), rs.getString("category_name"));
                int thickness = rs.getInt("thickness");
                int width = rs.getInt("width");
                int length = rs.getInt("length");
                double price = rs.getDouble("price");
                int stock = rs.getInt("stock");
                String name = rs.getString("product_name");
                boolean active = rs.getBoolean("active");

                products.add(new Product(id, variant_id, category, thickness, width, length, price, stock, name, active));
            }
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();

        }
        return products;
    }

    public Product buildProduct(ResultSet rs) throws SQLException {
        int id = rs.getInt("product_id");
        int variant_id = rs.getInt("id");
        Category category = new Category(rs.getInt("category_id"), rs.getString("category_name"));
        int thickness = rs.getInt("thickness");
        int width = rs.getInt("width");
        int length = rs.getInt("length");
        double price = rs.getDouble("price");
        int stock = rs.getInt("stock");
        String name = rs.getString("product_name");
        boolean active = rs.getBoolean("active");

        return new Product(id, variant_id, category, thickness, width, length, price, stock, name, active);
    }

    private List<Orequest> addProductToBuild(List<Orequest> reqs, Order order, Map<Integer, Integer> lengths, int categoryID, int productID, String comment) {
        int catID = categoryID;

        for (Map.Entry<Integer, Integer> entry : lengths.entrySet()) {
            int lengthMin = entry.getKey();
            int lengthMax = entry.getKey();
            int prodID = productID;
            int width = 195; // bliver pt. ikke benyttet, nødløsning til søm

            Product product = new Product(catID, prodID, lengthMin, lengthMax, width);

            int qty = entry.getValue();
            double amount = qty * (product.getPrice() * (product.getLength() / 100));
            reqs.add(new Orequest(product, order.getId(), qty, amount, comment));
        }

        return reqs;
    }

    // Standard blueprint for en carport
    public List<Orequest> carportBlueprint(Order order) throws BuildException {
        String comment;
        int catID;
        int prodID;
        int length;
        List<Orequest> reqs = new ArrayList();
        
        if (order == null) {
            throw new BuildException("Ingen ordre tilknyttet blueprintet..");
        }
        
        if (order.getHeight() <= 0 || order.getLenght() <= 0 || order.getWidth() <= 0 ) {
            throw new BuildException("Mål mangler på ordre..");
        }

        //Remme
        comment = "Remme i sider, sadles ned i stolper";
        catID = 2;
        prodID = 5;
        length = order.getLenght();
        //addProductToBuild(reqs, order, calcRemmeMap(order), catID, prodID, comment);
        addProductToBuild(reqs, order, calcWoodsMap(catID, prodID, length), catID, prodID, comment);

        // Stolper
        comment = "Stolper";
        catID = 1;
        prodID = 7;
        addProductToBuild(reqs, order, calcStolperMap(catID, prodID, order), catID, prodID, comment);

        // Spær almindelig
        comment = "Spær";
        catID = 8;
        prodID = 5;
        addProductToBuild(reqs, order, calcSpaerMap(catID, prodID, order.getLenght(), order.getWidth(), false), catID, prodID, comment);

        // Understernbrædder siderne
        comment = "Understernbrædder siderne";
        catID = 3;
        prodID = 1;
        length = order.getLenght();
        addProductToBuild(reqs, order, calcWoodsMap(catID, prodID, length), catID, prodID, comment);

        // Understernbrædder for- og bagende
        comment = "Understernbrædder for- og bagende";
        length = order.getWidth();
        addProductToBuild(reqs, order, calcWoodsMap(catID, prodID, length), catID, prodID, comment);

        // Tag fladt
        if (order.getRoofAngle() == 0) {
            comment = "Tagplader monteres på spær";
            catID = 7;
            prodID = order.getRoofType();
            addProductToBuild(reqs, order, calcRoofMap(catID, prodID, order), catID, prodID, comment);
        } else {
            comment = "Tagplader monteres på spær med hældning";
            catID = 7;
            prodID = order.getRoofType();
            addProductToBuild(reqs, order, calcRoofMap(catID, prodID, order), catID, prodID, comment);

            // Spær til taget
            comment = "Spær til taget";
            catID = 8;
            prodID = 5;
            addProductToBuild(reqs, order, calcSpaerMap(catID, prodID, order.getLenght(), (int) calcRoofAngledLength(order), true), catID, prodID, comment);

        }

        // Bundskruer
        comment = "Skruer til tagplader";
        length = 60;
        catID = 11;
        prodID = 13;
        addProductToBuild(reqs, order, calcScrews(7, length, 8, reqs), catID, prodID, comment);

        // Beklædning af skur sider
        if (order.getShedLength() > 0 && order.getShedWidth() > 0) {
            comment = "Til beklædning af skur siderne";
            catID = 5;
            prodID = 8;
            addProductToBuild(reqs, order, calcSurfaceMap(order.getLenght(), order.getHeight(), catID, prodID, order, 2), catID, prodID, comment);

            comment = "Til beklædning af skur gavle";
            catID = 5;
            prodID = 8;
            addProductToBuild(reqs, order, calcSurfaceMap(order.getWidth(), order.getHeight(), catID, prodID, order, 2), catID, prodID, comment);

            // Skruer til ydrebeklædning
            comment = "Til montering af yderste beklædning";
            length = 70;
            catID = 11;
            prodID = 15;
            addProductToBuild(reqs, order, calcScrews(5, length, 6, reqs), catID, prodID, comment);

            // skruer til inderbeklædning
            comment = "Til montering af inderste beklædning";
            length = 50;
            addProductToBuild(reqs, order, calcScrews(5, length, 8, reqs), catID, prodID, comment);
        }

        return reqs;
    }

    // En færdig stykliste for en carport ud fra et blueprint
    public List<Odetail> carportBuilder(List<Orequest> request, Order order) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        List<Odetail> odetails = new ArrayList();
        String query;

        try {
            con = Connector.connection();
            for (Orequest r : request) {
                query = "SELECT product_variants.product_id, product_variants.id, products_in_categories.category_id, categories.category_name, products.thickness, products.width, length, price, stock, product_variants.active, products.product_name FROM carports.product_variants\n"
                        + "JOIN products_in_categories ON product_variants.product_id = products_in_categories.product_id\n"
                        + "JOIN products ON product_variants.product_id = products.id\n"
                        + "JOIN categories ON products_in_categories.category_id = categories.id\n"
                        + "WHERE category_id = ? AND (length BETWEEN ? AND ?) AND product_variants.product_id = ?;";

                ps = con.prepareStatement(query);
                ps.setInt(1, r.getProduct().getCategory_id());
                ps.setInt(2, r.getProduct().getLengthMin());
                ps.setInt(3, r.getProduct().getLengthMax());
                ps.setInt(4, r.getProduct().getId());
                rs = ps.executeQuery();

                if (rs.next()) {
                    Product product = buildProduct(rs);

                    int qty = r.getQty();
                    double amount;
                    // skruer
                    if (product.getCategory().getId() == 11) {
                        amount = qty * product.getPrice();
                    } else {
                        amount = qty * product.getPrice() * (product.getLength() / 100);
                    }

                    String comment = r.getComment();
                    odetails.add(new Odetail(product, order.getId(), qty, amount, comment));

                }
            }

        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                    rs = null;
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }

            if (ps != null) {
                try {
                    ps.close();
                    ps = null;
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }

            try {
                if (con != null && !con.isClosed()) {
                    con.close();
                    con = null;
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        return odetails;
    }

    // tjekker om styklisten indholder alle nødvendige produkter
    public boolean validateCarport(Carport carport, Order order) {
        boolean status = false;
        int[] shed = {1};
        if (order.getShedLength() > 0 && order.getShedWidth() > 0) {
            for (int c : shed) {
                for (Odetail o : carport.getItems()) {
                    if (o.getProduct().getCategory().getId() == c) {
                        status = true;
                        break;
                    } else {
                        status = false;
                    }
                }
            }

        }

        return status;
    }

    private Map<Integer, Integer> calcStolperMap(int categoryID, int productID, Order order) throws BuildException {
        List<Product> woods = getProductsAllForBuild(categoryID, productID);
        int bigLen = 1000;
        int height = order.getHeight();

        int max = calcMaxLength(woods);
        int min = calcMinLength(woods);

        int count = 4; // minimum

        if (order.getLenght() >= bigLen) {
            count += 2;
        }

        if (order.getShedLength() > 0 && order.getShedWidth() > 0) {
            count += 7;
        }

        Map<Integer, Integer> map = new HashMap();
        int length = order.getHeight() + 100; // skal placeres i jorden
        
        while (height > 0) {
            if (height > max) {
                map.put(max, map.getOrDefault(max, 0) + 1);
                height -= max;
            } else if (height < min) {
                map.put(min, map.getOrDefault(min, 0) + 1);
                height -= min;
            } else {
                for (Product p : woods) {
                    if (p.getLength() > height) {
                        map.put(p.getLength(), map.getOrDefault(p.getLength(), 0) + (1 * count));
                        height -= p.getLength();
                        break;
                    }
                }
            }
        }

        //map.put(length, map.getOrDefault(order.getHeight(), 0) + count);

        return map;

    }

    private Map<Integer, Integer> calcScrews(int productCatID, int screwLength, int ratio, List<Orequest> blueprint) {
        Map<Integer, Integer> map = new HashMap();

        for (Orequest o : blueprint) {
            if (o.getProduct().getCategory_id() == productCatID) {
                map.put(screwLength, map.getOrDefault(screwLength, 0) + o.getQty() * ratio);
            }
        }

        return map;
    }

    // Længden på siden af taget
    private double calcRoofAngledLength(Order order) {

        double degree = order.getRoofAngle();
        double radian = Math.toRadians(degree);

        double c = order.getWidth();
        double cos = Math.cos(radian);

        double length = c / (2 * cos);

        System.out.println("Længde: " + length);

        return length;
    }

    // max længden af et bestemt produkt
    private int calcMaxLength(List<Product> woods) throws BuildException {
        
        if (woods == null || woods.isEmpty()) {
            throw new BuildException("Ingen produkter tilgængelig til at udregne maxLength ");
        }
        
        int max = 0;

        for (Product p : woods) {
            if (p.getLength() > max) {
                max = p.getLength();
            }
        }

        return max;

    }

    // min længden af et bestemt produkt
    private int calcMinLength(List<Product> woods) throws BuildException {
        
        if (woods == null || woods.isEmpty()) {
            throw new BuildException("Ingen produkter tilgængelig til at udregne minLength ");
        }
        
        int min = woods.get(0).getLength();
            for (Product p : woods) {
                if (p.getLength() < min) {
                    min = p.getLength();
                }
            }

        return min;

    }

}
