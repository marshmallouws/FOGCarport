/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import entity.Order;
import entity.User;
import java.util.ArrayList;

/**
 *
 * @author Bitten
 */
public interface OrderInterface {
    public boolean createOrder(Order order);
    public ArrayList<Order> getOrders();
    public Order getOrder(int id);
    public void assignOrder(User user, Order order);
    public void assignOrder(int orderID, int employeeID);
    public Order updateOrder(Order order);
}
