package data;

import entity.Category;
import entity.Odetail;
import entity.Order;
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
import java.util.List;

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

    public int calcStolper(Order order) {
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

    public List<Integer> calcRemme(Order order) {
        // max længde på ordre skal vi bestemme, da der ellers skal beregnes flere stolper på
        List<Integer> lengths = new ArrayList();
        int length = order.getLenght();

        int max = 0;
        for (Product p : getRemme(order)) {
            if (p.getLength() > max) {
                max = p.getLength();
            }
        }

        int min = max;
        for (Product p : getRemme(order)) {
            if (p.getLength() < min) {
                min = p.getLength();
            }
        }

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

    public List<Integer> calcUnderSternLength(Order order) {
        List<Integer> lengths = new ArrayList();
        int length = order.getLenght();

        int max = 0;
        for (Product p : getUnderStern(order)) {
            if (p.getLength() > max) {
                max = p.getLength();
            }
        }

        int min = max;
        for (Product p : getUnderStern(order)) {
            if (p.getLength() < min) {
                min = p.getLength();
            }
        }

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

    public List<Integer> calcUnderSternWidth(Order order) {
        List<Integer> lengths = new ArrayList();
        int width = order.getWidth();

        int max = 0;
        for (Product p : getUnderStern(order)) {
            if (p.getLength() > max) {
                max = p.getLength();
            }
        }

        int min = max;
        for (Product p : getUnderStern(order)) {
            if (p.getLength() < min) {
                min = p.getLength();
            }
        }

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

    public List<Product> getRemme(Order order) {
        List<Product> products = new ArrayList();
        try {
            Connection con = Connector.connection();
            String query = "SELECT product_variants.product_id, product_variants.id, products_in_categories.category_id, categories_test.category_name, products_test.thickness, products_test.width, length, price, stock, products_test.product_name FROM carports.product_variants\n"
                    + "JOIN products_in_categories ON product_variants.product_id = products_in_categories.product_id\n"
                    + "JOIN products_test ON product_variants.product_id = products_test.id\n"
                    + "JOIN categories_test ON products_in_categories.category_id = categories_test.id\n"
                    + "WHERE category_id = 2;";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("product_id");
                int variant_id = rs.getInt("id");
                Category category = new Category(rs.getInt("category_id"), rs.getString("category_name"));
                int category_id = rs.getInt("category_id");
                int thickness = rs.getInt("thickness");
                int width = rs.getInt("width");
                int length = rs.getInt("length");
                double price = rs.getDouble("price");
                int stock = rs.getInt("stock");
                String name = rs.getString("product_name");

                products.add(new Product(id, variant_id, category, thickness, width, length, price, stock, name));
            }
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }
        return products;
    }

    public List<Product> getUnderStern(Order order) {
        List<Product> products = new ArrayList();
        try {
            Connection con = Connector.connection();
            String query = "SELECT product_variants.product_id, product_variants.id, products_in_categories.category_id, categories_test.category_name, products_test.thickness, products_test.width, length, price, stock, products_test.product_name FROM carports.product_variants\n"
                    + "JOIN products_in_categories ON product_variants.product_id = products_in_categories.product_id\n"
                    + "JOIN products_test ON product_variants.product_id = products_test.id\n"
                    + "JOIN categories_test ON products_in_categories.category_id = categories_test.id\n"
                    + "WHERE category_id = 3;";
            PreparedStatement ps = con.prepareStatement(query);
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

                products.add(new Product(id, variant_id, category, thickness, width, length, price, stock, name));
            }
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }
        return products;
    }

    public List<Odetail> buildCarport(Order order) {
        List<Odetail> odetails = new ArrayList();
        Product product;
        String query = "SELECT product_variants.product_id, product_variants.id, products_in_categories.category_id, categories_test.category_name, products_test.thickness, products_test.width, length, price, stock, products_test.product_name FROM carports.product_variants\n"
                    + "JOIN products_in_categories ON product_variants.product_id = products_in_categories.product_id\n"
                    + "JOIN products_test ON product_variants.product_id = products_test.id\n"
                    + "JOIN categories_test ON products_in_categories.category_id = categories_test.id\n"
                    + "WHERE category_id = ? AND length = ?;";
        PreparedStatement ps;
        ResultSet rs;
        try {
            Connection con = Connector.connection();

            //STOLPER (længden af stolpen skal vælges ud fra valgte højde på ordren. Stolpen skal 90 cm ned i jorden, hvorfor denne skal lægges til)
//            query = "SELECT product_variants.product_id, product_variants.id, products_in_categories.category_id, categories_test.category_name, products_test.thickness, products_test.width, length, price, stock, products_test.product_name FROM carports.product_variants\n"
//                    + "JOIN products_in_categories ON product_variants.product_id = products_in_categories.product_id\n"
//                    + "JOIN products_test ON product_variants.product_id = products_test.id\n"
//                    + "JOIN categories_test ON products_in_categories.category_id = categories_test.id\n"
//                    + "WHERE category_id = ? AND length = ?;";
            ps = con.prepareStatement(query);
            ps.setInt(1, 1); // category id
            ps.setInt(2, 420); // random valgt længde (højde)
            rs = ps.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("product_id");
                int variant_id = rs.getInt("id");
                Category category = new Category(rs.getInt("category_id"), rs.getString("category_name"));
                int thickness = rs.getInt("thickness");
                int width = rs.getInt("width");
                int length = rs.getInt("length");
                double price = rs.getDouble("price");
                int stock = rs.getInt("stock");
                String name = rs.getString("product_name");

                product = new Product(id, variant_id, category, thickness, width, length, price, stock, name);

                int qty = calcStolper(order);
                double amount = qty * product.getPrice() * (product.getLength() / 100);
                String comment = "Stolper nedgraves 90 cm i jord";
                odetails.add(new Odetail(product, order.getId(), qty, amount, comment));

            }

            //REMME
            for (Integer len : calcRemme(order)) {
//                query = "SELECT product_variants.product_id, product_variants.id, products_in_categories.category_id, categories_test.category_name, products_test.thickness, products_test.width, length, price, stock, products_test.product_name FROM carports.product_variants\n"
//                    + "JOIN products_in_categories ON product_variants.product_id = products_in_categories.product_id\n"
//                    + "JOIN products_test ON product_variants.product_id = products_test.id\n"
//                    + "JOIN categories_test ON products_in_categories.category_id = categories_test.id\n"
//                    + "WHERE category_id = ? AND length = ?;";
                ps = con.prepareStatement(query);
                ps.setInt(1, 2); // category id
                ps.setInt(2, len); // længde fra calcRemme
                rs = ps.executeQuery();

                if (rs.next()) {
                    int id = rs.getInt("product_id");
                    int variant_id = rs.getInt("id");
                    Category category = new Category(rs.getInt("category_id"), rs.getString("category_name"));
                    int thickness = rs.getInt("thickness");
                    int width = rs.getInt("width");
                    int length = rs.getInt("length");
                    double price = rs.getDouble("price");
                    int stock = rs.getInt("stock");
                    String name = rs.getString("product_name");

                    product = new Product(id, variant_id, category, thickness, width, length, price, stock, name);

                    int qty = 2; // én i hver side
                    double amount = qty * product.getPrice() * (product.getLength() / 100);
                    String comment = "Remme i sider, sadles ned i stolper";
                    odetails.add(new Odetail(product, order.getId(), qty, amount, comment));
                }
            }

            //SPÆR
//            query = "SELECT product_variants.product_id, product_variants.id, products_in_categories.category_id, categories_test.category_name, products_test.thickness, products_test.width, length, price, stock, products_test.product_name FROM carports.product_variants\n"
//                    + "JOIN products_in_categories ON product_variants.product_id = products_in_categories.product_id\n"
//                    + "JOIN products_test ON product_variants.product_id = products_test.id\n"
//                    + "JOIN categories_test ON products_in_categories.category_id = categories_test.id\n"
//                    + "WHERE category_id = ? AND length = ?;";
            ps = con.prepareStatement(query);
            ps.setInt(1, 8); // category id
            ps.setInt(2, order.getWidth()); // bredde på carport
            rs = ps.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("product_id");
                int variant_id = rs.getInt("id");
                Category category = new Category(rs.getInt("category_id"), rs.getString("category_name"));
                int thickness = rs.getInt("thickness");
                int width = rs.getInt("width");
                int length = rs.getInt("length");
                double price = rs.getDouble("price");
                int stock = rs.getInt("stock");
                String name = rs.getString("product_name");

                product = new Product(id, variant_id, category, thickness, width, length, price, stock, name);

                int qty = calcSpaer(order);
                double amount = qty * product.getPrice() * (product.getLength() / 100);
                String comment = "Spær";
                odetails.add(new Odetail(product, order.getId(), qty, amount, comment));
            }

            //UNDERSTERNBRÆDDER - LENGTH
            for (Integer len : calcUnderSternLength(order)) {
//                query = "SELECT product_variants.product_id, product_variants.id, products_in_categories.category_id, categories_test.category_name, products_test.thickness, products_test.width, length, price, stock, products_test.product_name FROM carports.product_variants\n"
//                    + "JOIN products_in_categories ON product_variants.product_id = products_in_categories.product_id\n"
//                    + "JOIN products_test ON product_variants.product_id = products_test.id\n"
//                    + "JOIN categories_test ON products_in_categories.category_id = categories_test.id\n"
//                    + "WHERE category_id = ? AND length = ?;";
                ps = con.prepareStatement(query);
                ps.setInt(1, 3); // category id
                ps.setInt(2, len); // længde fra calcUnderSternLength
                rs = ps.executeQuery();

                if (rs.next()) {
                    int id = rs.getInt("product_id");
                    int variant_id = rs.getInt("id");
                    Category category = new Category(rs.getInt("category_id"), rs.getString("category_name"));
                    int thickness = rs.getInt("thickness");
                    int width = rs.getInt("width");
                    int length = rs.getInt("length");
                    double price = rs.getDouble("price");
                    int stock = rs.getInt("stock");
                    String name = rs.getString("product_name");

                    product = new Product(id, variant_id, category, thickness, width, length, price, stock, name);

                    int qty = 2; // én i hver side
                    double amount = qty * product.getPrice() * (product.getLength() / 100);
                    String comment = "Understernbrædder til siderne";
                    odetails.add(new Odetail(product, order.getId(), qty, amount, comment));
                }
            }

            //UNDERSTERNBRÆDDER - WIDTH
            for (Integer len : calcUnderSternWidth(order)) {
//                query = "SELECT product_variants.product_id, product_variants.id, products_in_categories.category_id, categories_test.category_name, products_test.thickness, products_test.width, length, price, stock, products_test.product_name FROM carports.product_variants\n"
//                    + "JOIN products_in_categories ON product_variants.product_id = products_in_categories.product_id\n"
//                    + "JOIN products_test ON product_variants.product_id = products_test.id\n"
//                    + "JOIN categories_test ON products_in_categories.category_id = categories_test.id\n"
//                    + "WHERE category_id = ? AND length = ?;";
                ps = con.prepareStatement(query);
                ps.setInt(1, 3); // category id
                ps.setInt(2, len); // længde fra calcUnderSternWidth
                rs = ps.executeQuery();

                if (rs.next()) {
                    int id = rs.getInt("product_id");
                    int variant_id = rs.getInt("id");
                    Category category = new Category(rs.getInt("category_id"), rs.getString("category_name"));
                    int thickness = rs.getInt("thickness");
                    int width = rs.getInt("width");
                    int length = rs.getInt("length");
                    double price = rs.getDouble("price");
                    int stock = rs.getInt("stock");
                    String name = rs.getString("product_name");

                    product = new Product(id, variant_id, category, thickness, width, length, price, stock, name);

                    int qty = 2; // én i hver side
                    double amount = qty * product.getPrice() * (product.getLength() / 100);

                    String comment = "Understernbrædder til for- & bagende";
                    odetails.add(new Odetail(product, order.getId(), qty, amount, comment));
                }
            }

            // FINAL
            for (Odetail o : odetails) {
                System.out.println(o.getProduct().getName() + " " + o.getProduct().getLength() + " cm. " + o.getQty() + " stk. " + o.getAmount() + " kr. ");
            }

        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }

        //beklædning
        return odetails;
    }

    public static void main(String[] args) {
        String path = "C:\\Users\\caspe\\Desktop\\zipcodes.txt";
        //String _path = ".\\zipcodes.txt";
        File file = new File(path);
        //System.out.println(new File(".").getAbsolutePath());

        System.out.println(new DevMapper().loadZipcodesFromFile(file));
        Order order = new Order(0, 720, 870, 200, 200, 0);
        new DevMapper().buildCarport(order);
    }

}
