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
    private int stock;
    
    private int variant_id;
    private int category_id;
    private int thickness;
    private String name;
    
    //For creating new product
    public Product (Category category, int height, int length, int width, double price, int stock) {
        this.category = category;
        this.height = height;
        this.length = length;
        this.width = width;
        this.price = price;
        this.stock = stock;
    }
    
    //For fetching products from database
    public Product (int id, Category category, int height, int length, 
            int width, double price, boolean active, int stock) {
        this(category, height, length, width, price, stock);
        this.id = id;
        this.active = active;

    }
    
    // new simple
    public Product (int id, int variant_id, Category category, int thickness, int width, int length, double price, int stock, String name)  {
        this.id = id;
        this.variant_id = variant_id;
        this.category = category;
        this.thickness = thickness;
        this.width = width;
        this.length = length;
        this.price = price;
        this.stock = stock;
        this.name = name;
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
    
    public int getStock() {
        return stock;
    }

    public String getName() {
        return name;
    }

    public int getVariant_id() {
        return variant_id;
    }

    public int getCategory_id() {
        return category_id;
    }
    
}
