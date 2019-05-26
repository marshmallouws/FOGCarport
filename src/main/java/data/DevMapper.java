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
    private Connection con;
    
    public DevMapper(ConnectorInterface con) {
        try {
            this.con = con.connect();
            
        } catch (ClassNotFoundException | SQLException e) {
            
        }
    }
    

    public boolean loadZipcodesFromFile(File file) {
        boolean status = false;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));

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
        } catch (SQLException | IOException ex) {
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

    private void addUniversalSkruer(List<Odetail> odetails, Order order) {
        try {
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

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
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

    public static void main(String[] args) {
        ConnectorInterface conn = Connector.getInstance();
        DevMapper dm = new DevMapper(conn);
        String path = System.getProperty("user.dir")+"\\src\\main\\java\\data\\zipcodes.txt";
        File file = new File(path);
        System.out.println(dm.loadZipcodesFromFile(file));
    }

}
