/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import entity.User;

/**
 *
 * @author Annika
 */
public interface UserInterface {
    public User logIn(String username, String password) throws LogInException;
}
