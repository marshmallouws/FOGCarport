/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
    public Product getProductVariant(int id);
    public Product getProductMain(int id);
    public ArrayList<Category> getCategories();
    public ArrayList<Product> getProductVariantsList(int categoryID, int productID);
    public List<Product> getProductsInCategories(int categoryID);
    public int createProductVariant(Product product);
    public int createProduct(Product product);
    public boolean updateProductVariant(Product product);
    public List<Product> getRoofTypes();
}
