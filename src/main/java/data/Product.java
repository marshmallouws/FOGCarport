/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

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
    
    public Product (Category category, int height, int length, int width) {
        this.category = category;
        this.height = height;
        this.length = length;
        this.width = width;
    }
    
    public Product (int id, Category category, int height, int length, int width) {
        this(category, height, length, width);
        this.id = id;        
    }
    
    public Product (int id, Category category) {
        this.id = id;
        this.category = category;
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
