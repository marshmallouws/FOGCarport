package data;

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

    public int calcStolper(Order order) {
        // minimum
        int count = 4;

        // stor carport
        if (order.getLenght() >= 1000) {
            count += 2;
        }

        // med skur
        if (order.getShedLength() > 0 && order.getShedWidth() > 0) {
            count += 7;
        }
        return count;
    }

    public List<Integer> calcRemme(Order order) {
        List<Integer> lengths = new ArrayList();
        int length = order.getLenght();

        int min = 270;
        int max = 600;

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

    public List<Product> buildCarport(Order order) {
        List<Odetail> odetails = new ArrayList();
        List<Product> products = new ArrayList();
        Product product;
        String query;
        PreparedStatement ps;
        ResultSet rs;
        try {
            Connection con = Connector.connection();

            //STOLPER (længden af stolpen skal vælges ud fra valgte højde på ordren. Stolpen skal 90 cm ned i jorden, hvorfor denne skal lægges til)
            query = "SELECT product_variants.product_id, product_variants.id, products_in_categories.category_id, products_test.thickness, products_test.width, length, price, stock, products_test.product_name FROM carports.product_variants JOIN products_in_categories ON product_variants.product_id = products_in_categories.product_id JOIN products_test ON product_variants.product_id = products_test.id WHERE category_id = ? AND length = ?;";
            ps = con.prepareStatement(query);
            ps.setInt(1, 1); // category id
            ps.setInt(2, 420); // random valgt længde (højde)
            rs = ps.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("product_id");
                int variant_id = rs.getInt("id");
                int category_id = rs.getInt("category_id");
                int thickness = rs.getInt("thickness");
                int width = rs.getInt("width");
                int length = rs.getInt("length");
                double price = rs.getDouble("price");
                int stock = rs.getInt("stock");
                String name = rs.getString("product_name");

                product = new Product(id, variant_id, category_id, thickness, width, length, price, stock, name);
                products.add(new Product(id, variant_id, category_id, thickness, width, length, price, stock, name));

                int qty = calcStolper(order);
                double amount = qty * product.getPrice();

                odetails.add(new Odetail(product, qty, amount));

            }

            //REMME
            query = "SELECT product_variants.product_id, product_variants.id, products_in_categories.category_id, products_test.thickness, products_test.width, length, price, stock, products_test.product_name FROM carports.product_variants JOIN products_in_categories ON product_variants.product_id = products_in_categories.product_id JOIN products_test ON product_variants.product_id = products_test.id WHERE category_id = ? AND length = ?;";
            ps = con.prepareStatement(query);
            ps.setInt(1, 2); // category id
            ps.setInt(2, 600); // random valgt længde
            rs = ps.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("product_id");
                int variant_id = rs.getInt("id");
                int category_id = rs.getInt("category_id");
                int thickness = rs.getInt("thickness");
                int width = rs.getInt("width");
                int length = rs.getInt("length");
                double price = rs.getDouble("price");
                int stock = rs.getInt("stock");
                String name = rs.getString("product_name");

                products.add(new Product(id, variant_id, category_id, thickness, width, length, price, stock, name));
            }

            for (Odetail o : odetails) {
                System.out.println(o.getProduct().getName() + " " + o.getQty() + " " + o.getAmount());
            }

        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }

        //remme
        //understernbrædder
        //beklædning
        return products;
    }

    public static void main(String[] args) {
        String path = "C:\\Users\\caspe\\Desktop\\zipcodes.txt";
        String _path = ".\\zipcodes.txt";
        //File file = new File(_path);
        //System.out.println(new File(".").getAbsolutePath());

        //System.out.println(new DevMapper().loadZipcodesFromFile(file));
        Order order = new Order(0, 0, 1470, 200, 200, 0);
        new DevMapper().buildCarport(order);
        System.out.println(new DevMapper().calcRemme(order));

    }

}
