/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

/**
 *
 * @author Annika
 */
public class Product {
    private int id;
    private Category category;
    private int height;
    private int length;
    private int width;
    private double price;
    private boolean active;
    
    //For creating new product
    public Product (Category category, int height, int length, int width, double price) {
        this.category = category;
        this.height = height;
        this.length = length;
        this.width = width;
        this.price = price;
    }
    
    //For fetching products from database
    public Product (int id, Category category, int height, int length, 
            int width, double price, boolean active) {
        this(category, height, length, width, price);
        this.id = id;
        this.active = active;

    }

    public double getPrice() {
        return price;
    }
    
    public boolean isActive() {
        return active;
    }

    public int getId() {
        return id;
    }

    public Category getCategory() {
        return category;
    }

    public int getHeight() {
        return height;
    }

    public int getLength() {
        return length;
    }

    public int getWidth() {
        return width;
    }
}
