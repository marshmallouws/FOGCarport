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
public class Employee {
    private int id;
    private String initials;
    private String password;
    //private String password;
    
    public Employee(int id, String initials, String password) {
        this.id = id;
        this.initials = initials;
        this.password = password;
    }   

    public int getId() {
        return id;
    }

    public String getInitials() {
        return initials;
    }

    public String getPassword() {
        return password;
    }
}