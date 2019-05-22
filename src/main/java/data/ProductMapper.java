package data;

import entity.Category;
import entity.Product;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Annika
 */
public class ProductMapper implements ProductDAOInterface {
    
    private Connection con;
    
    public ProductMapper(ConnectorInterface con) {
        try {
            this.con = con.connect();
        } catch (ClassNotFoundException | SQLException e) {
            
        }
    }

    public Product buildProductVariant(ResultSet rs) throws SQLException {
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

    @Override
    public Product getProductVariant(int id) {
        Product product = null;
        try {

            String query = "SELECT product_variants.product_id, product_variants.id, products_in_categories.category_id, categories.category_name, products.thickness, products.width, length, price, stock, product_variants.active, products.product_name FROM carports.product_variants\n"
                    + "JOIN products_in_categories ON product_variants.product_id = products_in_categories.product_id\n"
                    + "JOIN products ON product_variants.product_id = products.id\n"
                    + "JOIN categories ON products_in_categories.category_id = categories.id\n"
                    + "WHERE product_variants.id = ?;";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                product = buildProductVariant(rs);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return product;
    }

    @Override
    public Product getProductMain(int id) {
        Product product = null;
        try {
            String query = "SELECT id, product_name, thickness, width, active FROM products WHERE id = ?;";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                product = new Product(rs.getInt("id"), rs.getString("product_name"), rs.getInt("thickness"), rs.getInt("width"), rs.getBoolean("active"));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return product;
    }



    @Override
    public ArrayList<Category> getCategories() {
        ArrayList<Category> cat = new ArrayList();
        try {
            String query = "SELECT id, category_name FROM categories;";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("category_name");
                boolean height = true;
                boolean length = true;
                boolean width = true;

                cat.add(new Category(id, name, height, width, length)); // OLD
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return cat;
    }

    @Override
    public ArrayList<Product> getProductVariantsList(int categoryID, int productID) {
        ArrayList<Product> prod = new ArrayList();
        try {
            String query = "SELECT product_variants.product_id, product_variants.id, products_in_categories.category_id, categories.category_name, products.thickness, products.width, length, price, stock, product_variants.active, products.product_name FROM carports.product_variants\n"
                    + "JOIN products_in_categories ON product_variants.product_id = products_in_categories.product_id\n"
                    + "JOIN products ON product_variants.product_id = products.id\n"
                    + "JOIN categories ON products_in_categories.category_id = categories.id\n"
                    + "WHERE category_id = ? AND products_in_categories.product_id = ?;";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, categoryID);
            ps.setInt(2, productID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                prod.add(buildProductVariant(rs));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return prod;
    }

    @Override
    public List<Product> getProductsInCategories(int categoryID) {
        List<Product> products = new ArrayList();
        try {
            String query = "SELECT * FROM products_in_categories\n"
                    + "JOIN products ON products.id = products_in_categories.product_id\n"
                    + "JOIN categories ON categories.id = products_in_categories.category_id\n"
                    + "WHERE category_id = ?;";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, categoryID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                products.add(new Product(rs.getInt("product_id"), new Category(rs.getInt("category_id"), rs.getString("category_name")), rs.getString("product_name"), rs.getBoolean("active")));
            }
            return products;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return products;
    }

    @Override
    public int createProductVariant(Product product) {
        int variantID = 0;
        try {
            String query = "INSERT INTO product_variants (product_id, length, price, stock) VALUES (?, ?, ?, ?);";
            PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, product.getId());
            ps.setInt(2, product.getLength());
            ps.setDouble(3, product.getPrice());
            ps.setInt(4, product.getStock());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();

            if (rs.next()) {
                variantID = rs.getInt(1);
            }

            return variantID;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return variantID;
        }
    }

    @Override
    public int createProduct(Product product) {
        try {
            int id = 0;
            con.setAutoCommit(false); // implement transactions with categoryID
            String query = "INSERT INTO products (product_name, thickness, width) VALUES (?,?,?);";
            PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, product.getName());
            ps.setInt(2, product.getThickness());
            ps.setInt(3, product.getWidth());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();

            if (rs.next()) {
                id = rs.getInt(1);
            }

            query = "INSERT INTO products_in_categories (category_id, product_id) VALUES (?, ?)";
            ps = con.prepareStatement(query);
            ps.setInt(1, product.getCategory_id());
            ps.setInt(2, id);
            ps.executeUpdate();

            con.commit();
            con.setAutoCommit(true);

            return id;
        } catch (SQLException ex) {
            ex.printStackTrace();
            try {
                con.rollback();
                return 0;
            } catch (SQLException ex1) {
                return 0;
            }
        }
    }

    @Override
    public boolean updateProductVariant(Product product) {
        try {
            String query = "UPDATE product_variants SET price = ?, stock = ?, active = ? WHERE id = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setDouble(1, product.getPrice());
            ps.setInt(2, product.getStock());
            ps.setBoolean(3, product.isActive());
            ps.setInt(4, product.getVariant_id()); //
            ps.executeUpdate();
            return true;

        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Product> getRoofTypes() {
        List<Product> roofs = new ArrayList();

        try {
            String query = "SELECT * FROM products_in_categories\n"
                    + "JOIN products ON products_in_categories.product_id = products.id\n"
                    + "WHERE category_id = 7;";

            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                roofs.add(new Product(rs.getInt("product_id"), rs.getString("product_name")));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return roofs;
    }

}
