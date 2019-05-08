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
public class Category {
    private int id;
    private String name;
    private boolean height;
    private boolean width;
    private boolean length; //boolean to know whether or not height, width and length are relevant for this category of products
    
    public Category(int id, String name, boolean height, boolean width, boolean length) {
        this.id = id;
        this.name = name;
        this.height = height;
        this.width = width;
        this.length = length;
    }
    
    public Category(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isHeight() {
        return height;
    }

    public boolean isWidth() {
        return width;
    }

    public boolean isLength() {
        return length;
    }
}
