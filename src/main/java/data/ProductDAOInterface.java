/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import entity.Category;
import entity.Product;
import java.util.ArrayList;

/**
 *
 * @author Annika
 */
public interface ProductDAOInterface {
    public void insertNewProduct(Product product, Category category);
    public ArrayList<Product> getProducts();
    public ArrayList<Product> getProducts(Category category);
    public Product getProductMain(int id);
    public void setActivity(Product product);
    public ArrayList<Product> getProducts(boolean activity);
}
