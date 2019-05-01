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
public class Customer {
    private int id;
    private String email;
    private String address;
    private int zip;
    private int phone;
    //private String password;
    
    public Customer(int id, String email, String address, int zip, int phone  /*String password*/) {
        this.id = id;
        this.email = email;
        this.address = address;
        this.zip = zip;
        this.phone = phone;
        //this.password = password;
        
    }   

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }
    
    public String getAddress() {
        return address;
    }

    /*public String getPassword() {
        return password;
    }*/

    public int getZip() {
        return zip;
    }

    public int getPhone() {
        return phone;
    }
}