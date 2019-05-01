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
    //private int customerId;
    private int height; //Will be calculated from the angle of the roof
    private int width;
    private int lenght;
    private int shedLength;
    private int shedWidth;
    private int roofAngle;
    private String date;
    private String status;
    private double salesPrice;
    
    //For creating new order in db
    public Order(int height, int width, int length, int shedLength, int shedWidth, int roofAngle) {
        this.height = height;
        this.width = width;
        this.lenght = length;
        this.shedLength = shedLength;
        this.shedWidth = shedWidth;
        this.roofAngle = roofAngle;
    }
    
    //For fetching order from db
    public Order(int id, int employeeId, int height, int width, int length,
            int shedLength, int shedWidth, int roofAngle, String date, String status, double salesPrice) {
        this(height, length, width, shedLength, shedWidth, roofAngle);
        this.id = id;
        this.employeeId = employeeId;
        this.date = date;
        this.status = status;
    }

    // No date (is it used?)
    public Order(int id, int employeeId, int height, int width, int lenght, int shedLength, int shedWidth, int roofAngle) {
        this.id = id;
        this.employeeId = employeeId;
        this.height = height;
        this.width = width;
        this.lenght = lenght;
        this.shedLength = shedLength;
        this.shedWidth = shedWidth;
        this.roofAngle = roofAngle;
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
    
    public String getStatus() {
        return status;
    }
    
    public double getSalesPrice() {
        return salesPrice;
    }

}
