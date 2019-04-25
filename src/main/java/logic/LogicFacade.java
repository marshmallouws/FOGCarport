/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import entity.Order;
import entity.User;
import java.util.ArrayList;
import data.LogInException;

/**
 *
 * @author vl48
 */
public class LogicFacade {

    public boolean createOrder(Order order) {
        return new data.OrderMapper().createOrder(order);
    }

    public ArrayList<Order> getOrders() {
        return new data.OrderMapper().getOrders();
    }

    public Order getOrder(int id) {
        return new data.OrderMapper().getOrder(id);
    }

    public void assignOrder(User user, Order order) {
        new data.OrderMapper().assignOrder(user, order);
    }

    public void logIn(User user) throws LogInException {
        new data.UserMapper().logIn(user);
    }
}
