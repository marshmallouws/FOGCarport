/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import java.util.ArrayList;

/**
 *
 * @author Annika
 */
public interface ProductDAOInterface {
    public void insertNewProduct(Product product);
    public ArrayList<Product> getProducts();
    public Product getProduct();
}
