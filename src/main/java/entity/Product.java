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
    private int lengthMin;
    private int lengthMax;
    
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
    public Product (int id, int variant_id, Category category, int height, int length, int width, double price, boolean active, int stock, String name) {
        this(category, height, length, width, price, stock);
        this.id = id;
        this.variant_id = variant_id;
        this.active = active;
        this.name = name;

    }
    
    // Product "Variant"
    public Product (int id, int variant_id, Category category, int thickness, int width, int length, double price, int stock, String name, boolean active)  {
        this.id = id;
        this.variant_id = variant_id;
        this.category = category;
        this.thickness = thickness;
        this.width = width;
        this.length = length;
        this.price = price;
        this.stock = stock;
        this.name = name;
        this.active = active;
    }
    
    // for showing types of roof (at the moment)
    public Product (int id, String name) {
        this.id = id;
        this.name = name;
    }
    
    // For building the Blueprint
    public Product (int category_id, int product_id, int lengthMin, int lengthMax, int width) {
        this.category_id = category_id;
        this.id = product_id;
        this.lengthMin = lengthMin;
        this.lengthMax = lengthMax;
        this.width = width;
    }
    
    // For displaying all products in a category (not all variants)
    public Product (int id, Category category, String name, boolean active) {
        this.id = id;
        this.category = category;
        this.name = name;
        this.active = active;
    }
    
    // Product "Main"
    public Product (int id, String name, int thickness, int width, boolean active) {
        this.id = id;
        this.name = name;
        this.thickness = thickness;
        this.width = width;
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

    public int getThickness() {
        return thickness;
    }

    public int getLengthMin() {
        return lengthMin;
    }

    public int getLengthMax() {
        return lengthMax;
    }
    
    
    
}
