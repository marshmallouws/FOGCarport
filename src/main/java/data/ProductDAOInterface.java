package data;

import entity.Category;
import entity.Product;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Annika
 */
public interface ProductDAOInterface {

    /**
     * Gets a specific product variant by a given ID.
     * @param id
     * @return Product object
     */
    public Product getProductVariant(int id);

     /**
     * Gets a specific product by a given ID.
     * @param id
     * @return Product object
     */
    public Product getProductMain(int id);

     /**
     * Gets all product categories.
     * @return Arraylist of Category objects
     */
    public ArrayList<Category> getCategories();

    /**
     * Gets a list of all product variants for a given category and product combination.
     * @param categoryID
     * @param productID
     * @return List of Product objects
     */
    public ArrayList<Product> getProductVariantsList(int categoryID, int productID);

    /**
     * Gets a list of all products in a given category.
     * @param categoryID
     * @return List of Product objects
     */
    public List<Product> getProductsInCategories(int categoryID);

    /**
     * Inserts a new product variant in DB
     * @param product Complete Product object
     * @return generated id
     */
    public int createProductVariant(Product product);

    /**
     * Inserts a new product in the DB
     * @param product Complete Product object
     * @return generated id
     */
    public int createProduct(Product product);

     /**
     * Updates a product stored in DB.
     * @param product Complete Product object
     * @return true if success, otherwise false
     */
    public boolean updateProductVariant(Product product);

    /**
     * Gets a list of all products with category Roof
     * @return List of Product objects
     */
    public List<Product> getRoofTypes();
}
