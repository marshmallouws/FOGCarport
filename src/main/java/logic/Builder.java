package logic;

import data.BuildException;
import data.BuilderMapper;
import data.Connector;
import data.ConnectorInterface;
import entity.Blueprint;
import entity.Carport;
import entity.Category;
import entity.Odetail;
import entity.Order;
import entity.Orequest;
import entity.Product;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Casper
 */
public class Builder {

    private Connection conn;
    private ConnectorInterface con = Connector.getInstance();
    private BuilderMapper bm = new BuilderMapper(con);

    public Builder(ConnectorInterface conn) {
        try {
            this.conn = conn.connect();
        } catch (ClassNotFoundException | SQLException e) {

        }
    }

    public static void main(String[] args) {
        Connector con = Connector.getInstance();
        Builder b = new Builder(con);
        BuilderMapper bm = new BuilderMapper(con);
        Order o = new Order(270, 640, 640, 300, 300, 20, 12);
        //System.out.println(b.calcRoofMap(7, o.getRoofType(), o));
        //System.out.println(b.calcSurfaceMap(o.getShedLength(), o.getHeight(), 5, 8, o, 2));

        try {
            System.out.println(b.carportBlueprint(o, bm.getBlueprint(1)));
        } catch (BuildException ex) {
            ex.printStackTrace();
        }

    }

    /**
     * A method to calculate lengths and qty of spaer using a specific product. If angled is true the calculation is for both sides of an angled roof.
     * @param categoryID
     * @param productID
     * @param x
     * @param y
     * @param angled
     * @return Map of Integer and Integer where key is length and value is qty
     * @throws BuildException 
     */
    public Map<Integer, Integer> calcSpaerMap(int categoryID, int productID, int x, int y, boolean angled) throws BuildException {
        Map<Integer, Integer> map = new HashMap();

        if (categoryID == 0 || productID == 0) {
            throw new BuildException("Intet produkt valgt til at udregne spær");
        }

        if (x == 0 || y == 0) {
            throw new BuildException("Mangler mål til udregning af spær");
        }

        List<Product> woods = bm.getProductsAllForBuild(categoryID, productID);

        if (woods == null || woods.isEmpty()) {
            throw new BuildException(("Ingen produkter med categoryID; " + categoryID + " og productID: " + productID + " blev fundet til at udregne spær"));
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

    /**
     * A method to calculate length and qty of a product to build a specific length. The result will always give you more wood than you need, so it can be manually cut of if needed when building.
     * @param categoryID
     * @param productID
     * @param length
     * @return Map of Integer and Integer where key is length and value is qty
     * @throws BuildException 
     */
    public Map<Integer, Integer> calcWoodsMap(int categoryID, int productID, int length) throws BuildException {
        Map<Integer, Integer> map = new HashMap();
        List<Product> woods = bm.getProductsAllForBuild(categoryID, productID);

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
/**
 * A method to calculate length and qty of Product to build a flat or angled roof.
 * @param categoryID
 * @param productID
 * @param order
 * @return Map of Integer and Integer where key is length and value is qty
 */
    public Map<Integer, Integer> calcRoofMap(int categoryID, int productID, Order order) {
        try {
            Map<Integer, Integer> map = new HashMap();
            List<Product> roofs = bm.getProductsAllForBuild(categoryID, productID);

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

    /**
     * A method to calculate length and qty of Product to build a flat surface like walls and floors
     * @param x
     * @param y
     * @param categoryID
     * @param productID
     * @param order
     * @param sides
     * @return Map of Integer and Integer where key is length and value is qty
     */
    public Map<Integer, Integer> calcSurfaceMap(int x, int y, int categoryID, int productID, Order order, int sides) {
        try {
            Map<Integer, Integer> map = new HashMap();
            List<Product> woods = bm.getProductsAllForBuild(categoryID, productID);

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

    /**
     * Method to build a Product from a ResultSet
     * @param rs
     * @return Product
     * @throws SQLException 
     */
    public static Product buildProduct(ResultSet rs) throws SQLException {
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

    /**
     * Adds Orequest objects with Product object to the List of Orequest objects given to this method using the Maps from calculations.
     * @param reqs
     * @param order
     * @param lengths
     * @param categoryID
     * @param productID
     * @param comment
     * @return Updated list of Orequest
     */
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

    /**
     * A method for generating the list of Orequest needed to build a Carport of a specifc Model using a blueprint. The Blueprint is represented by a List of Blueprint
     * @param order
     * @param blueprints
     * @return List of Orequest to give to a carportBuilder to build a Carport
     * @throws BuildException 
     */
    public List<Orequest> carportBlueprint(Order order, List<Blueprint> blueprints) throws BuildException {
        List<Orequest> reqs = new ArrayList();
        int x; // x to calc
        int y; // y to calc
        for (Blueprint b : blueprints) {
            switch (b.getUsage()) {
                // Stolper
                case 1:
                    addProductToBuild(reqs, order, calcStolperMap(b.getCategory_id(), b.getProduct_id(), order), b.getCategory_id(), b.getProduct_id(), b.getMessage());
                    break;
                // Remme
                case 2:
                    x = order.getLenght();
                    addProductToBuild(reqs, order, calcWoodsMap(b.getCategory_id(), b.getProduct_id(), x), b.getCategory_id(), b.getProduct_id(), b.getMessage());
                    break;
                // Spær
                case 3:
                    x = order.getLenght();
                    y = order.getWidth();
                    addProductToBuild(reqs, order, calcSpaerMap(b.getCategory_id(), b.getProduct_id(), x, y, false), b.getCategory_id(), b.getProduct_id(), b.getMessage());
                    break;
                // Understernbrædder sider
                case 4:
                    x = order.getLenght();
                    addProductToBuild(reqs, order, calcWoodsMap(b.getCategory_id(), b.getProduct_id(), x), b.getCategory_id(), b.getProduct_id(), b.getMessage());
                    break;
                // Understernbrædder for- og bagende
                case 5:
                    y = order.getWidth();
                    addProductToBuild(reqs, order, calcWoodsMap(b.getCategory_id(), b.getProduct_id(), y), b.getCategory_id(), b.getProduct_id(), b.getMessage());
                    break;

                // Tagplader tag fladt 
                case 6:
                    if (order.getRoofAngle() == 0) {
                        addProductToBuild(reqs, order, calcRoofMap(b.getCategory_id(), order.getRoofType(), order), b.getCategory_id(), order.getRoofType(), b.getMessage());
                    }
                    break;
                    
                // Tagsten tag rejsning
                case 7:
                    if (order.getRoofAngle() > 0) {
                        addProductToBuild(reqs, order, calcRoofMap(b.getCategory_id(), order.getRoofType(), order), b.getCategory_id(), order.getRoofType(), b.getMessage());
                    }
                    break;

                // Spær tag rejsning    
                case 8:
                    if (order.getRoofAngle() > 0) {
                        addProductToBuild(reqs, order, calcSpaerMap(b.getCategory_id(), b.getProduct_id(), order.getLenght(), (int) calcRoofAngledLength(order), true), b.getCategory_id(), b.getProduct_id(), b.getMessage());
                    }
                    break;

                // skruer til tagplader (bundskruer)    
                case 9:
                    addProductToBuild(reqs, order, calcScrews(7, 60, 8, reqs), b.getCategory_id(), b.getProduct_id(), b.getMessage());
                    break;

                // Beklædning sider
                case 10:
                    if (order.getShedLength() > 0 && order.getShedWidth() > 0) {
                        addProductToBuild(reqs, order, calcSurfaceMap(order.getShedLength(), order.getHeight(), b.getCategory_id(), b.getProduct_id(), order, 2), b.getCategory_id(), b.getProduct_id(), b.getMessage());
                    }
                    break;

                // Beklædning gavle
                case 11:
                    if (order.getShedLength() > 0 && order.getShedWidth() > 0) {
                        addProductToBuild(reqs, order, calcSurfaceMap(order.getShedWidth(), order.getHeight(), b.getCategory_id(), b.getProduct_id(), order, 2), b.getCategory_id(), b.getProduct_id(), b.getMessage());
                    }
                    break;

                // Skruer beklædning yderste
                case 12:
                    addProductToBuild(reqs, order, calcScrews(5, 70, 6, reqs), b.getCategory_id(), b.getProduct_id(), b.getMessage());
                    break;

                // Skruer beklædning inderste
                case 13:
                    addProductToBuild(reqs, order, calcScrews(5, 50, 8, reqs), b.getCategory_id(), b.getProduct_id(), b.getMessage());
                    break;

                default:
                    break;
            }

        }
        return reqs;
    }

    // tjekker om styklisten indholder alle nødvendige produkter
    public boolean validateCarport(Carport carport, Order order, List<Blueprint> blueprint) {
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

    public Map<Integer, Integer> calcStolperMap(int categoryID, int productID, Order order) throws BuildException {
        List<Product> woods = bm.getProductsAllForBuild(categoryID, productID);
        int bigLen = 1000; // big carport size
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
    public double calcRoofAngledLength(Order order) {

        double degree = order.getRoofAngle();
        double radian = Math.toRadians(degree);

        double c = order.getWidth();
        double cos = Math.cos(radian);

        double length = c / (2 * cos);

        return length;
    }

    // max længden af et bestemt produkt
    public int calcMaxLength(List<Product> woods) throws BuildException {
        List<Integer> len = new ArrayList();

        if (woods == null || woods.isEmpty()) {
            throw new BuildException("Ingen produkter tilgængelig til at udregne maxLength ");
        }
        
        for (Product p : woods) {
            len.add(p.getLength());
        }

        Collections.sort(len);

        int max = 0;

        for (Integer i : len) {
            if (i > max) {
                max = i;
            }
        }

        return max;

    }

    // min længden af et bestemt produkt
    public int calcMinLength(List<Product> woods) throws BuildException {
        List<Integer> len = new ArrayList();

        for (Product p : woods) {
            len.add(p.getLength());
        }
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
