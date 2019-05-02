/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import entity.Customer;
import entity.Order;
import entity.Employee;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Bitten
 */
public interface OrderInterface {
    public boolean createOrder(Order order, Customer customer);
    public List<Order> getOrders();
    public List<Order> getOrdersUnassigned();
    public ArrayList<Order> getUnfinishedOrders();
    public ArrayList<Order> getOldOrders();
    public Order getOrder(int id);
    public void assignOrder(Employee user, Order order);
    public void assignOrder(int orderID, int employeeID);
    public Order updateOrder(Order order) throws UpdateException;
    
    
}
