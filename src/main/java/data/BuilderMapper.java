package data;

import entity.Blueprint;
import entity.Category;
import entity.Model;
import entity.Odetail;
import entity.Order;
import entity.Orequest;
import entity.Product;
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
public class BuilderMapper {

    private Connection conn;

    public BuilderMapper(ConnectorInterface conn) {
        try {
            this.conn = conn.connect();
        } catch (ClassNotFoundException | SQLException e) {

        }
    }

    /**
     * Gets all Models
     * @return List of Model objects
     * @throws BuildException 
     */
    public List<Model> getModels() throws BuildException {
        try {
            List<Model> models = new ArrayList();
            String query = "SELECT * FROM models;";
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                int project_id = rs.getInt("project_id");
                String title = rs.getString("title");

                models.add(new Model(id, project_id, title));
            }

            return models;

        } catch (SQLException ex) {
            throw new BuildException(ex.getMessage());
        }
    }
/**
 * Get a selected Blueprint for a Model
 * @param modelID
 * @return List of blueprint objects
 * @throws BuildException 
 */
    public List<Blueprint> getBlueprint(int modelID) throws BuildException {
        try {
            List<Blueprint> blueprint = new ArrayList();
            String query = "SELECT * FROM blueprints WHERE model_id = ?;";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, modelID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                int usage = rs.getInt("usage_id");
                int model_id = rs.getInt("model_id");
                int category_id = rs.getInt("category_id");
                int product_id = rs.getInt("product_id");
                String message = rs.getString("message");

                blueprint.add(new Blueprint(id, usage, model_id, category_id, product_id, message));
            }

            return blueprint;

        } catch (SQLException ex) {
            throw new BuildException(ex.getMessage());
        }
    }
/**
 * Update a Blueprint in database using a List of Blueprint objects. Can update category_id, product_id and message using the id for the Blueprint object.
 * @param blueprint
 * @return boolean
 */
    public boolean updateBlueprint(List<Blueprint> blueprint) {
        try {
            String query;
            PreparedStatement ps = null;
            for (Blueprint b : blueprint) {
                query = "UPDATE blueprints SET message = ? WHERE id = ?;";
                ps = conn.prepareStatement(query);
                ps.setString(1, b.getMessage());
                ps.setInt(2, b.getId());
                ps.executeUpdate();
            }
            return true;
        } catch (SQLException ex) {
            return false;
        }

    }

    /**
     * Gets a list with all variants of Product for building a Carport
     * @param categoryID
     * @param productID
     * @return List of Product objects
     */
    public List<Product> getProductsAllForBuild(int categoryID, int productID) {
        List<Product> products = new ArrayList();
        try {
            String query = "SELECT product_variants.product_id, product_variants.id, products_in_categories.category_id, categories.category_name, products.thickness, products.width, length, price, stock, product_variants.active, products.product_name FROM carports.product_variants\n"
                    + "JOIN products_in_categories ON product_variants.product_id = products_in_categories.product_id\n"
                    + "JOIN products ON product_variants.product_id = products.id\n"
                    + "JOIN categories ON products_in_categories.category_id = categories.id\n"
                    + "WHERE category_id = ? AND product_variants.product_id = ?;";
            PreparedStatement ps = conn.prepareStatement(query);
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

        } catch (SQLException ex) {
            ex.printStackTrace();

        }
        return products;
    }

    /**
     * Builds a Carport using a List of Orequest objects and the Order. List of Orequest objects is generated by a Blueprint and the Order in Builder.
     * @param request
     * @param order
     * @return List of Odetail objects
     */
    public List<Odetail> carportBuilder(List<Orequest> request, Order order) {
        PreparedStatement ps = null;
        ResultSet rs = null;

        List<Odetail> odetails = new ArrayList();
        String query;

        try {
            for (Orequest r : request) {
                query = "SELECT product_variants.product_id, product_variants.id, products_in_categories.category_id, categories.category_name, products.thickness, products.width, length, price, stock, product_variants.active, products.product_name FROM carports.product_variants\n"
                        + "JOIN products_in_categories ON product_variants.product_id = products_in_categories.product_id\n"
                        + "JOIN products ON product_variants.product_id = products.id\n"
                        + "JOIN categories ON products_in_categories.category_id = categories.id\n"
                        + "WHERE category_id = ? AND (length BETWEEN ? AND ?) AND product_variants.product_id = ?;";

                ps = conn.prepareStatement(query);
                ps.setInt(1, r.getProduct().getCategory_id());
                ps.setInt(2, r.getProduct().getLengthMin());
                ps.setInt(3, r.getProduct().getLengthMax());
                ps.setInt(4, r.getProduct().getId());
                rs = ps.executeQuery();

                if (rs.next()) {
                    Product product = logic.Builder.buildProduct(rs);

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

        } catch (SQLException ex) {
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
        }

        return odetails;
    }
}
