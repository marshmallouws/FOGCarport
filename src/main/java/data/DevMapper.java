package data;

import entity.Carport;
import entity.Category;
import entity.Odetail;
import entity.Order;
import entity.Orequest;
import entity.Product;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
 * @author caspe
 */
public class DevMapper {

    public boolean loadZipcodesFromFile(File file) {
        boolean status = false;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
            Connection con = Connector.connection();

            String str;
            while ((str = br.readLine()) != null) {
                String parts[] = str.split(" ", 2);
                int zip = Integer.parseInt(parts[0]);
                String city = parts[1];

                System.out.println("zip: " + zip + " city: " + city);

                String query = "REPLACE INTO zipcodes (zip, city) VALUES (?, ?)";
                PreparedStatement ps = con.prepareStatement(query);

                ps.setInt(1, zip);
                ps.setString(2, city);

                ps.executeUpdate();

                status = true;
            }

        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException | SQLException | IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        return status;

    }

    private int calcStolper(Order order) {
        int maxLengthCarport = 1000;

        // minimum
        int count = 4;

        // stor carport
        if (order.getLenght() >= maxLengthCarport) {
            count += 2;
        }

        // med skur
        if (order.getShedLength() > 0 && order.getShedWidth() > 0) {
            count += 7;
        }
        return count;
    }

    private int calcMaxLength(List<Product> woods) {

        int max = 0;

        for (Product p : woods) {
            if (p.getLength() > max) {
                max = p.getLength();
            }
        }

        return max;

    }

    private int calcMinLength(List<Product> woods) {
        int min = 0;
        try {
            min = woods.get(0).getLength();

            for (Product p : woods) {
                if (p.getLength() < min) {
                    min = p.getLength();
                }
            }

        } catch (IndexOutOfBoundsException ex) {
            ex.printStackTrace();
        }

        return min;

    }
    
    private Map<Integer, Integer> calcWoodsMap(int categoryID, int productID, int length) {
        // skal hentes dynamisk fra ordren, hvis anden type kan benyttes
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

    private List<Integer> calcRemmeSmart(Order order) {
        int categoryID = 2;
        int productID = 5; // skal hentes dynamisk fra ordren, hvis anden type kan benyttes
        List<Integer> lengths = new ArrayList();
        int length = order.getLenght();
        List<Integer> smart = new ArrayList();
        List<Product> remme = getProductsAllForBuild(categoryID, productID);

        for (Product p : remme) {
            smart.add(p.getLength());
        }

        int max = calcMaxLength(remme);
        int min = calcMinLength(remme);

        while (length > 0) {
            if (length > max) {
                lengths.add(max);
                length -= max;
            } else if (length < min) {
                lengths.add(min);
                length -= min;
            } else {
                for (Integer len : smart) {
                    if (len > length) {
                        lengths.add(len);
                        length -= len;
                        break;
                    }
                }
            }
        }

        return lengths;

    }

    private List<Integer> calcUnderSternLength(Order order) {
        int categoryID = 3;
        int productID = 1;
        List<Integer> lengths = new ArrayList();
        int length = order.getLenght();

        List<Product> underSterns = getProductsAllForBuild(categoryID, productID);

        int max = calcMaxLength(underSterns);
        int min = calcMinLength(underSterns);

        while (length > 0) {
            if (length > max) {
                lengths.add(max);
                length -= max;
            } else if (length < min) {
                lengths.add(min);
                length -= min;
            } else {
                lengths.add(length);
                length -= length;
            }
        }

        return lengths;
    }

    private List<Integer> calcUnderSternWidth(Order order) {
        int categoryID = 3;
        int productID = 1;
        List<Integer> lengths = new ArrayList();
        int width = order.getWidth();

        List<Product> underSterns = getProductsAllForBuild(categoryID, productID);
        int max = calcMaxLength(underSterns);
        int min = calcMinLength(underSterns);

        while (width > 0) {

            if (width > max) {
                lengths.add(max);
                width -= max;
            } else if (width < min) {
                lengths.add(min);
                width -= min;
            } else {
                lengths.add(width);
                width -= width;
            }
        }

        return lengths;
    }

    public int calcSpaer(Order order) {
        // umiddelbart kan en carport ikke være breddere end længden på et spær. Ellers skal vi i hvert fald tilføje noget ekstra til at holde, som ved remmen.
        int count = 10;
        int gap = order.getWidth() / count;
        while (gap >= 60) {
            count += 5;
            gap = order.getWidth() / count;
        }
        return count;
    }

    private int calcSpaerAngledHorizontal(double length) {
        int count = 7;
        int gap = (int) length / count;
        while (gap >= 60) {
            count += 3;
            gap = (int) length / count;
        }
        return count;
    }

    private Map<Integer, Integer> calcRoofFlat(Order order) throws FOGException {
        int categoryID = 7;
        int productID = order.getRoofType();
        Map<Integer, Integer> map = new HashMap();
        List<Product> roofs = getProductsAllForBuild(categoryID, productID);
        int width;

        try {
            width = roofs.get(0).getWidth() / 10;
        } catch (IndexOutOfBoundsException ex) {
            throw new FOGException(ex.getMessage());
        }

        int carportLength = order.getLenght();
        int carportWidth = order.getWidth();
        int rows = 0;
        int cols = 0;

        while (carportLength > 0) {
            rows++;
            carportLength -= width;
        }

        int max = 0;
        for (Product p : roofs) {
            if (p.getLength() > max) {
                max = p.getLength();
            }
        }

        int min = max;
        for (Product p : roofs) {
            if (p.getLength() < min) {
                min = p.getLength();
            }
        }

        while (carportWidth > 0) {

            if (carportWidth >= max) {
                cols++;
                carportWidth -= max;
                map.put(max, map.getOrDefault(max, 0) + 1);
            } else if (carportWidth <= min) {
                cols++;
                carportWidth -= min;
                map.put(min, map.getOrDefault(min, 0) + 1);
            } else {
                cols++;
                map.put(carportWidth, map.getOrDefault(carportWidth, 0) + 1);
                carportWidth -= carportWidth;

            }
        }

        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            map.put(entry.getKey(), entry.getValue() * rows);
        }

        return map;
    }

    private Map<Integer, Integer> calcRoofAngled(Order order) throws FOGException {
        int categoryID = 7;
        int productID = order.getRoofType();
        Map<Integer, Integer> map = new HashMap();
        List<Product> roofs = getProductsAllForBuild(categoryID, productID);
        int width;

        try {
            width = roofs.get(0).getWidth() / 10;
        } catch (IndexOutOfBoundsException ex) {
            throw new FOGException(ex.getMessage());
        }

        int carportLength = order.getLenght();
        int carportWidth = (int) calcRoofAngledLength(order);
        int rows = 0;
        int cols = 0;

        while (carportLength > 0) {
            rows++;
            carportLength -= width;
        }

        int max = 0;
        for (Product p : roofs) {
            if (p.getLength() > max) {
                max = p.getLength();
            }
        }

        int min = max;
        for (Product p : roofs) {
            if (p.getLength() < min) {
                min = p.getLength();
            }
        }

        while (carportWidth > 0) {

            if (carportWidth >= max) {
                cols++;
                carportWidth -= max;
                map.put(max, map.getOrDefault(max, 0) + 1);
            } else if (carportWidth <= min) {
                cols++;
                carportWidth -= min;
                map.put(min, map.getOrDefault(min, 0) + 1);
            } else {
                for (Product p : roofs) {
                    if (p.getLength() > carportWidth) {
                        cols++;
                        map.put(p.getLength(), map.getOrDefault(p.getLength(), 0) + 1);
                        carportWidth -= p.getLength();
                        break;
                    }
                }

            }
        }

        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            map.put(entry.getKey(), entry.getValue() * rows);
        }

        return map;
    }

    private double calcRoofAngledLength(Order order) {

        double degree = order.getRoofAngle();
        double radian = Math.toRadians(degree);

        double c = order.getWidth();
        double cos = Math.cos(radian);

        double length = c / (2 * cos);

        System.out.println("Længde: " + length);

        return length;
    }

    private int calcShed(Order order) throws FOGException {
        List<Product> woods = getShedSpecificType(order);
        int width;
        int countLen = 0;
        int countWid = 0;
        int total = 0;

        try {
            width = woods.get(0).getWidth() / 10;
        } catch (IndexOutOfBoundsException ex) {
            throw new FOGException(ex.getMessage());
        }

        int shedLength = order.getShedLength();
        int shedWidth = order.getShedWidth();

        int max = 0;
        for (Product p : woods) {
            if (p.getLength() > max) {
                max = p.getLength();
            }
        }

        int min = max;
        for (Product p : woods) {
            if (p.getLength() < min) {
                min = p.getLength();
            }
        }

        while (shedLength > 0) {
            countLen++;
            shedLength -= width;
        }

        while (shedWidth > 0) {
            countWid++;
            shedWidth -= width;
        }

        total = (2 * countLen) + (2 * countWid);

        return total;
    }

    public List<Product> getShedSpecificType(Order order) {
        List<Product> products = new ArrayList();
        try {
            Connection con = Connector.connection();
            String query = "SELECT product_variants.product_id, product_variants.id, products_in_categories.category_id, categories_test.category_name, products_test.thickness, products_test.width, length, price, stock, products_test.product_name FROM carports.product_variants\n"
                    + "JOIN products_in_categories ON product_variants.product_id = products_in_categories.product_id\n"
                    + "JOIN products_test ON product_variants.product_id = products_test.id\n"
                    + "JOIN categories_test ON products_in_categories.category_id = categories_test.id\n"
                    + "WHERE category_id = 5 AND product_variants.product_id = ?;";

            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, 8);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                products.add(buildProduct(rs));
            }
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }

        return products;
    }

    public List<Product> getProductsAllForBuild(int categoryID, int productID) {
        List<Product> products = new ArrayList();
        try {
            Connection con = Connector.connection();
            String query = "SELECT product_variants.product_id, product_variants.id, products_in_categories.category_id, categories_test.category_name, products_test.thickness, products_test.width, length, price, stock, active, products_test.product_name FROM carports.product_variants\n"
                    + "JOIN products_in_categories ON product_variants.product_id = products_in_categories.product_id\n"
                    + "JOIN products_test ON product_variants.product_id = products_test.id\n"
                    + "JOIN categories_test ON products_in_categories.category_id = categories_test.id\n"
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

    private void addStolper(List<Odetail> odetails, Order order) {
        try {
            Connection con = Connector.connection();
            String query = "SELECT product_variants.product_id, product_variants.id, products_in_categories.category_id, categories_test.category_name, products_test.thickness, products_test.width, length, price, stock, active, products_test.product_name FROM carports.product_variants\n"
                    + "JOIN products_in_categories ON product_variants.product_id = products_in_categories.product_id\n"
                    + "JOIN products_test ON product_variants.product_id = products_test.id\n"
                    + "JOIN categories_test ON products_in_categories.category_id = categories_test.id\n"
                    + "WHERE category_id = ? AND length = ?;";
            PreparedStatement ps = con.prepareStatement(query);

            ps.setInt(1, 1); // category id
            ps.setInt(2, 420); // random valgt længde (højde)
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                Product product = buildProduct(rs);

                int qty = calcStolper(order);
                double amount = qty * product.getPrice() * (product.getLength() / 100);
                String comment = "Stolper nedgraves 90 cm i jord";
                odetails.add(new Odetail(product, order.getId(), qty, amount, comment));

            }

        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }

    }

    private void addRemme(List<Odetail> odetails, Order order) {
        try {
            Connection con = Connector.connection();

            for (Integer len : calcRemmeSmart(order)) {
                String query = "SELECT product_variants.product_id, product_variants.id, products_in_categories.category_id, categories_test.category_name, products_test.thickness, products_test.width, length, price, stock, products_test.product_name FROM carports.product_variants\n"
                        + "JOIN products_in_categories ON product_variants.product_id = products_in_categories.product_id\n"
                        + "JOIN products_test ON product_variants.product_id = products_test.id\n"
                        + "JOIN categories_test ON products_in_categories.category_id = categories_test.id\n"
                        + "WHERE category_id = ? AND length = ?;";
                PreparedStatement ps = con.prepareStatement(query);

                ps.setInt(1, 2); // category id
                ps.setInt(2, len); // længde fra calcRemme
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {

                    Product product = buildProduct(rs);

                    int qty = 2; // én i hver side
                    double amount = qty * product.getPrice() * (product.getLength() / 100);
                    String comment = "Remme i sider, sadles ned i stolper";
                    odetails.add(new Odetail(product, order.getId(), qty, amount, comment));
                }

            }

        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }
    }

    private List<Orequest> addRemmeToBuild(Order order) {
        int catID = 2;
        List<Orequest> reqs = new ArrayList();

        for (Integer len : calcRemmeSmart(order)) {
            System.out.println(len);
            int lengthMin = len;
            int lengthMax = len;
            int productID = 5;
            int width = 195;

            Product product = new Product(catID, productID, lengthMin, lengthMax, width);

            int qty = 2;
            double amount = qty * product.getPrice() * (product.getLength() / 100);
            String comment = "Remme i sider, sadles ned i stolper";
            reqs.add(new Orequest(product, order.getId(), qty, amount, comment));
        }

        return reqs;

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
            double amount = qty * product.getPrice() * (product.getLength() / 100);
            reqs.add(new Orequest(product, order.getId(), qty, amount, comment));
        }

        return reqs;
    }

    private void addSpaer(List<Odetail> odetails, Order order) {
        try {
            Connection con = Connector.connection();
            String query = "SELECT product_variants.product_id, product_variants.id, products_in_categories.category_id, categories_test.category_name, products_test.thickness, products_test.width, length, price, stock, products_test.product_name FROM carports.product_variants\n"
                    + "JOIN products_in_categories ON product_variants.product_id = products_in_categories.product_id\n"
                    + "JOIN products_test ON product_variants.product_id = products_test.id\n"
                    + "JOIN categories_test ON products_in_categories.category_id = categories_test.id\n"
                    + "WHERE category_id = ? AND length = ?;";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, 8); // category id
            ps.setInt(2, order.getWidth()); // bredde på carport
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                Product product = buildProduct(rs);

                int qty = calcSpaer(order);
                double amount = qty * product.getPrice() * (product.getLength() / 100);
                String comment = "Spær";
                odetails.add(new Odetail(product, order.getId(), qty, amount, comment));
            }

            // Screws
            addUniversalSkruer(odetails, order);

        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void addUnderSternLength(List<Odetail> odetails, Order order) throws FOGException {
        try {
            Connection con = Connector.connection();
            for (Integer len : calcUnderSternLength(order)) {
                String query = "SELECT product_variants.product_id, product_variants.id, products_in_categories.category_id, categories_test.category_name, products_test.thickness, products_test.width, length, price, stock, products_test.product_name FROM carports.product_variants\n"
                        + "JOIN products_in_categories ON product_variants.product_id = products_in_categories.product_id\n"
                        + "JOIN products_test ON product_variants.product_id = products_test.id\n"
                        + "JOIN categories_test ON products_in_categories.category_id = categories_test.id\n"
                        + "WHERE category_id = ? AND length = ?;";
                PreparedStatement ps = con.prepareStatement(query);
                ps.setInt(1, 3); // category id
                ps.setInt(2, len); // længde fra calcUnderSternLength
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    Product product = buildProduct(rs);

                    int qty = 2; // én i hver side
                    double amount = qty * product.getPrice() * (product.getLength() / 100);
                    String comment = "Understernbrædder til siderne";
                    odetails.add(new Odetail(product, order.getId(), qty, amount, comment));
                }
            }

        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void addUnderSternWidth(List<Odetail> odetails, Order order) throws FOGException {
        try {
            Connection con = Connector.connection();
            for (Integer len : calcUnderSternWidth(order)) {
                String query = "SELECT product_variants.product_id, product_variants.id, products_in_categories.category_id, categories_test.category_name, products_test.thickness, products_test.width, length, price, stock, products_test.product_name FROM carports.product_variants\n"
                        + "JOIN products_in_categories ON product_variants.product_id = products_in_categories.product_id\n"
                        + "JOIN products_test ON product_variants.product_id = products_test.id\n"
                        + "JOIN categories_test ON products_in_categories.category_id = categories_test.id\n"
                        + "WHERE category_id = ? AND length = ?;";
                PreparedStatement ps = con.prepareStatement(query);
                ps.setInt(1, 3); // category id
                ps.setInt(2, len); // længde fra calcUnderSternWidth
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    Product product = buildProduct(rs);

                    int qty = 2; // én i hver side
                    double amount = qty * product.getPrice() * (product.getLength() / 100);

                    String comment = "Understernbrædder til for- & bagende";
                    odetails.add(new Odetail(product, order.getId(), qty, amount, comment));
                }
            }
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void addRoofFlat(List<Odetail> odetails, Order order) throws FOGException {
        try {
            Connection con = Connector.connection();
            for (Map.Entry<Integer, Integer> entry : calcRoofFlat(order).entrySet()) {
                String query = "SELECT product_variants.product_id, product_variants.id, products_in_categories.category_id, categories_test.category_name, products_test.thickness, products_test.width, length, price, stock, products_test.product_name FROM carports.product_variants\n"
                        + "JOIN products_in_categories ON product_variants.product_id = products_in_categories.product_id\n"
                        + "JOIN products_test ON product_variants.product_id = products_test.id\n"
                        + "JOIN categories_test ON products_in_categories.category_id = categories_test.id\n"
                        + "WHERE category_id = ? AND product_variants.product_id = ? AND length = ?;";
                PreparedStatement ps = con.prepareStatement(query);
                ps.setInt(1, 7); // category id
                ps.setInt(2, order.getRoofType()); // type af tagplade
                ps.setInt(3, entry.getKey()); // længde på tagplade
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    Product product = buildProduct(rs);
                    int qty = entry.getValue();
                    double amount = qty * product.getPrice() * (product.getLength() / 100);
                    String comment = "Tagplader monteres på spær";
                    odetails.add(new Odetail(product, order.getId(), qty, amount, comment));
                }
            }

            // Screws
            addBundSkruer(odetails, order);

        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void addRoofAngled(List<Odetail> odetails, Order order) throws FOGException {
        try {
            Connection con = Connector.connection();
            for (Map.Entry<Integer, Integer> entry : calcRoofAngled(order).entrySet()) {
                String query = "SELECT product_variants.product_id, product_variants.id, products_in_categories.category_id, categories_test.category_name, products_test.thickness, products_test.width, length, price, stock, products_test.product_name FROM carports.product_variants\n"
                        + "JOIN products_in_categories ON product_variants.product_id = products_in_categories.product_id\n"
                        + "JOIN products_test ON product_variants.product_id = products_test.id\n"
                        + "JOIN categories_test ON products_in_categories.category_id = categories_test.id\n"
                        + "WHERE category_id = ? AND product_variants.product_id = ? AND length = ?;";
                PreparedStatement ps = con.prepareStatement(query);
                ps.setInt(1, 7); // category id
                ps.setInt(2, order.getRoofType()); // type af tagplade
                ps.setInt(3, entry.getKey()); // længde på tagplade
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    Product product = buildProduct(rs);
                    int qty = entry.getValue() * 2; // taget har 2 sider
                    double amount = qty * product.getPrice() * (product.getLength() / 100);
                    String comment = "Tagplader monteres på spær med rejsning";
                    odetails.add(new Odetail(product, order.getId(), qty, amount, comment));
                }
            }

            // Screws
            addBundSkruer(odetails, order);

        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void addBundSkruer(List<Odetail> odetails, Order order) {
        try {
            Connection con = Connector.connection();
            String query = "SELECT product_variants.product_id, product_variants.id, products_in_categories.category_id, categories_test.category_name, products_test.thickness, products_test.width, length, price, stock, products_test.product_name FROM carports.product_variants\n"
                    + "JOIN products_in_categories ON product_variants.product_id = products_in_categories.product_id\n"
                    + "JOIN products_test ON product_variants.product_id = products_test.id\n"
                    + "JOIN categories_test ON products_in_categories.category_id = categories_test.id\n"
                    + "WHERE category_id = ?;";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, 9); // category id
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Product product = buildProduct(rs);

                int qty = calcBundSkruer(odetails);
                double amount = qty * product.getPrice();
                String comment = "Skruer til tagplader";
                odetails.add(new Odetail(product, order.getId(), qty, amount, comment));
            }

        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void addUniversalSkruer(List<Odetail> odetails, Order order) {
        try {
            Connection con = Connector.connection();
            String query = "SELECT product_variants.product_id, product_variants.id, products_in_categories.category_id, categories_test.category_name, products_test.thickness, products_test.width, length, price, stock, products_test.product_name FROM carports.product_variants\n"
                    + "JOIN products_in_categories ON product_variants.product_id = products_in_categories.product_id\n"
                    + "JOIN products_test ON product_variants.product_id = products_test.id\n"
                    + "JOIN categories_test ON products_in_categories.category_id = categories_test.id\n"
                    + "WHERE category_id = ?;";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, 10); // category id
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Product product = buildProduct(rs);

                int qty = calcUniversalSkruer(odetails);
                double amount = qty * product.getPrice();
                String comment = "Til montering af spær på rem";
                odetails.add(new Odetail(product, order.getId(), qty, amount, comment));
            }

        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void addShedSkruerInner(List<Odetail> odetails, Order order) {
        try {
            Connection con = Connector.connection();
            String query = "SELECT product_variants.product_id, product_variants.id, products_in_categories.category_id, categories_test.category_name, products_test.thickness, products_test.width, length, price, stock, products_test.product_name FROM carports.product_variants\n"
                    + "JOIN products_in_categories ON product_variants.product_id = products_in_categories.product_id\n"
                    + "JOIN products_test ON product_variants.product_id = products_test.id\n"
                    + "JOIN categories_test ON products_in_categories.category_id = categories_test.id\n"
                    + "WHERE category_id = ? AND width = ? AND length = ?;";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, 11); // category id
            ps.setInt(2, 5); // diameter på skrue (skal laves om til double)
            ps.setInt(3, 50); // længde på skruen
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Product product = buildProduct(rs);

                int qty = calcShedSkruerInner(odetails);
                double amount = qty * product.getPrice();
                String comment = "Til montering af inderste beklædning";
                odetails.add(new Odetail(product, order.getId(), qty, amount, comment));
            }

        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void addShedSkruerOuter(List<Odetail> odetails, Order order) {
        try {
            Connection con = Connector.connection();
            String query = "SELECT product_variants.product_id, product_variants.id, products_in_categories.category_id, categories_test.category_name, products_test.thickness, products_test.width, length, price, stock, products_test.product_name FROM carports.product_variants\n"
                    + "JOIN products_in_categories ON product_variants.product_id = products_in_categories.product_id\n"
                    + "JOIN products_test ON product_variants.product_id = products_test.id\n"
                    + "JOIN categories_test ON products_in_categories.category_id = categories_test.id\n"
                    + "WHERE category_id = ? AND width = ? AND length = ?;";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, 11); // category id
            ps.setInt(2, 5); // diameter på skrue (skal laves om til double)
            ps.setInt(3, 70); // længde på skruen
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Product product = buildProduct(rs);

                int qty = calcShedSkruerOuter(odetails);
                double amount = qty * product.getPrice();
                String comment = "Til montering af yderste beklædning";
                odetails.add(new Odetail(product, order.getId(), qty, amount, comment));
            }

        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }
    }

    private int calcBundSkruer(List<Odetail> odetails) {
        int total = 0;
        int counter = 8; // mængden af skruer pr. tagplade
        int categoryID = 7; // roof

        for (Odetail o : odetails) {
            if (o.getProduct().getCategory().getId() == categoryID) {
                total += o.getQty() * counter;
            }
        }

        return total;
    }

    private int calcUniversalSkruer(List<Odetail> odetails) {
        int total = 0;
        int counter = 2;
        int categoryID = 8; // spær

        for (Odetail o : odetails) {
            if (o.getProduct().getCategory().getId() == categoryID) {
                total += o.getQty() * counter;
            }
        }

        return total;
    }

    private int calcShedSkruerOuter(List<Odetail> odetails) {
        int total = 0;
        int counter = 8;
        int categoryID = 5; // beklædning

        for (Odetail o : odetails) {
            if (o.getProduct().getCategory().getId() == categoryID) {
                total += o.getQty() * counter;
            }
        }

        return total;
    }

    private int calcShedSkruerInner(List<Odetail> odetails) {
        int total = 0;
        int counter = 6;
        int categoryID = 5; // beklædning

        for (Odetail o : odetails) {
            if (o.getProduct().getCategory().getId() == categoryID) {
                total += o.getQty() * counter;
            }
        }

        return total;
    }

    private void addShed(List<Odetail> odetails, Order order) throws FOGException {
        try {
            Connection con = Connector.connection();
            String query = "SELECT product_variants.product_id, product_variants.id, products_in_categories.category_id, categories_test.category_name, products_test.thickness, products_test.width, length, price, stock, products_test.product_name FROM carports.product_variants\n"
                    + "JOIN products_in_categories ON product_variants.product_id = products_in_categories.product_id\n"
                    + "JOIN products_test ON product_variants.product_id = products_test.id\n"
                    + "JOIN categories_test ON products_in_categories.category_id = categories_test.id\n"
                    + "WHERE category_id = ? AND product_variants.product_id = ? AND length = ?;";

            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, 5); // category id
            ps.setInt(2, 8); // type af beklædning
            ps.setInt(3, order.getHeight()); // længden på beklædning (højden på carport - højden på taget)
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Product product = buildProduct(rs);
                int qty = calcShed(order);
                double amount = qty * product.getPrice() * (product.getLength() / 100);
                String comment = "til beklædning af skur";
                odetails.add(new Odetail(product, order.getId(), qty, amount, comment));
            }

            // Screws
            addShedSkruerInner(odetails, order);
            addShedSkruerOuter(odetails, order);

        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void addRoofAngledSpaerHorizontal(List<Odetail> odetails, Order order) {
        try {
            Connection con = Connector.connection();
            String query = "SELECT product_variants.product_id, product_variants.id, products_in_categories.category_id, categories_test.category_name, products_test.thickness, products_test.width, length, price, stock, products_test.product_name FROM carports.product_variants\n"
                    + "JOIN products_in_categories ON product_variants.product_id = products_in_categories.product_id\n"
                    + "JOIN products_test ON product_variants.product_id = products_test.id\n"
                    + "JOIN categories_test ON products_in_categories.category_id = categories_test.id\n"
                    + "WHERE category_id = ? AND length = ?;";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, 8); // category
            ps.setInt(2, order.getLenght());

            //ps.setInt(1, (int) calcRoofAngledLength(order)); // min length
            //ps.setInt(1, (int) calcRoofAngledLength(order) + 30); // max length
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Product product = buildProduct(rs);

                int qty = calcSpaerAngledHorizontal(calcRoofAngledLength(order));
                double amount = qty * product.getPrice() * (product.getLength() / 100);
                String comment = "Spær til taget - horizontal";
                odetails.add(new Odetail(product, order.getId(), qty, amount, comment));
            }
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void addRoofAngledSpaerVertical(List<Odetail> odetails, Order order) {
        try {
            Connection con = Connector.connection();
            String query = "SELECT product_variants.product_id, product_variants.id, products_in_categories.category_id, categories_test.category_name, products_test.thickness, products_test.width, length, price, stock, products_test.product_name FROM carports.product_variants\n"
                    + "JOIN products_in_categories ON product_variants.product_id = products_in_categories.product_id\n"
                    + "JOIN products_test ON product_variants.product_id = products_test.id\n"
                    + "JOIN categories_test ON products_in_categories.category_id = categories_test.id\n"
                    + "WHERE category_id = ? AND (length BETWEEN ? AND ?);";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, 8); // category
            //ps.setInt(2, order.getLenght());

            ps.setInt(2, (int) calcRoofAngledLength(order)); // min length
            ps.setInt(3, (int) calcRoofAngledLength(order) + 30); // max length
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Product product = buildProduct(rs);

                int qty = calcSpaer(order);
                double amount = qty * product.getPrice() * (product.getLength() / 100);
                String comment = "Spær til taget - vertical";
                odetails.add(new Odetail(product, order.getId(), qty, amount, comment));
            }
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }
    }

    // NEW - Not finished
    public List<Orequest> carportBlueprint(Order order) {
        String comment;
        int catID;
        int prodID;
        int length;
        List<Orequest> reqs = new ArrayList();

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
        //ddProductToBuild(reqs, order, calcStolperMap(order), catID, comment);

        // Spær
        comment = "";

        // Understernbrædder siderne
        comment = "Understernbrædder siderne";
        catID = 3;
        prodID = 1;
        length  = order.getLenght();
        addProductToBuild(reqs, order, calcWoodsMap(catID, prodID, length), catID, prodID, comment);

        // Understernbrædder for- og bagende
        comment = "Understernbrædder for- og bagende";
        length = order.getWidth();
        addProductToBuild(reqs, order, calcWoodsMap(catID, prodID, length), catID, prodID, comment);
        
        // Tag fladt
        if (order.getRoofAngle() == 0) {
        } else {
        }

        // Beklædning af skur
        if (order.getShedLength() > 0 && order.getShedWidth() > 0) {
        }

        return reqs;
    }

    public List<Odetail> carportBuilder(List<Orequest> request, Order order) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        List<Odetail> odetails = new ArrayList();
        String query;

        try {
            con = Connector.connection();
            for (Orequest r : request) {
                query = "SELECT product_variants.product_id, product_variants.id, products_in_categories.category_id, categories_test.category_name, products_test.thickness, products_test.width, length, price, stock, active, products_test.product_name FROM carports.product_variants\n"
                        + "JOIN products_in_categories ON product_variants.product_id = products_in_categories.product_id\n"
                        + "JOIN products_test ON product_variants.product_id = products_test.id\n"
                        + "JOIN categories_test ON products_in_categories.category_id = categories_test.id\n"
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
                    double amount = qty * product.getPrice() * (product.getLength() / 100);
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

    public List<Odetail> buildCarport(Order order) throws FOGException {
        List<Odetail> odetails = new ArrayList();

        // Stolper
        addStolper(odetails, order);

        // Remme
        addRemme(odetails, order);

        // Spær
        addSpaer(odetails, order);

        // Understernbrædder siderne
        addUnderSternLength(odetails, order);

        // Understernbrædder for- og bagende
        addUnderSternWidth(odetails, order);

        // Tag fladt
        if (order.getRoofAngle() == 0) {
            addRoofFlat(odetails, order);
        } else {
            addRoofAngledSpaerHorizontal(odetails, order);
            addRoofAngledSpaerVertical(odetails, order);
            addRoofAngled(odetails, order);
        }

        // Beklædning af skur
        if (order.getShedLength() > 0 && order.getShedWidth() > 0) {
            addShed(odetails, order);
        }

        //FINAL
//        for (Odetail o : odetails) {
//            System.out.println(o.getProduct().getName() + " " + o.getProduct().getLength() + " cm. " + o.getQty() + " stk. " + o.getAmount() + " kr. " + " " + o.getComment());
//        }
        return odetails;
    }

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

    public static void main(String[] args) {
        String path = "C:\\Users\\caspe\\Desktop\\zipcodes.txt";
        //String _path = ".\\zipcodes.txt";
        File file = new File(path);
        //System.out.println(new File(".").getAbsolutePath());

        //System.out.println(new DevMapper().loadZipcodesFromFile(file));
        Order order = new Order(270, 1900, 1900, 200, 200, 10, 12);

//        new DevMapper().calcRoofAngledLength(order);
//        System.out.println(new DevMapper().calcSpaerAngledHorizontal(new DevMapper().calcRoofAngledLength(order)));
//
//        try {
//            new DevMapper().buildCarport(order);
//        } catch (FOGException ex) {
//            ex.printStackTrace();
//        }
//        System.out.println("end");
        List<Orequest> blueprint = new DevMapper().carportBlueprint(order);
        for (Odetail o : new DevMapper().carportBuilder(blueprint, order)) {
            System.out.println(o.getProduct().getName() + " " + o.getProduct().getLength() + " cm. " + o.getQty() + " stk. " + o.getAmount() + " kr. " + " " + o.getComment() + " " + o.getProduct().isActive());
        }

        //System.out.println(new DevMapper().calcRemmeMap(order));

        //System.out.println(new DevMapper().calcRemmeMap(order));
//
//        try {
//            System.out.println(new DevMapper().calcShed(order));
//        } catch (FOGException ex) {
//            Logger.getLogger(DevMapper.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//        try {
//            System.out.println(new DevMapper().validateCarport(new Carport(new DevMapper().buildCarport(order)), order));
//        } catch (FOGException ex) {
//            Logger.getLogger(DevMapper.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

}
