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
public class User {
    private int id;
    private String username;
    private String password;
    
    public User(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }   
}