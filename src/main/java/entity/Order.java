/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

/**
 *
 * @author Bitten
 */
public class Order {

    private int id;
    private int employeeId;
    private int height; //Will be calculated from the angle of the roof
    private int width;
    private int lenght;
    private int shedLength;
    private int shedWidth;
    private int roofAngle;
    private String date;

    public Order(int height, int width, int length, int shedLength, int shedWidth, int roofAngle) {
        this.height = height;
        this.width = width;
        this.lenght = length;
        this.shedLength = shedLength;
        this.shedWidth = shedWidth;
        this.roofAngle = roofAngle;
    }

    public Order(int id, int employeeId, int height, int width, int length,
            int shedLength, int shedWidth, int roofAngle, String date) {
        this(height, length, width, shedLength, shedWidth, roofAngle);
        this.id = id;
        this.employeeId = employeeId;
        this.date = date;
    }
    
    public int getId() {
        return id;
    }
    
    public int employeeId() {
        return employeeId;
    }
    
    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int getLenght() {
        return lenght;
    }

    public int getShedLength() {
        return shedLength;
    }

    public int getShedWidth() {
        return shedWidth;
    }

    public int getRoofAngle() {
        return roofAngle;
    }
    
    public String getDate() {
        return date;
    }

}
