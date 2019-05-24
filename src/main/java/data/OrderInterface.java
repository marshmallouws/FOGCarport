/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import entity.Category;
import entity.Customer;
import entity.Order;
import entity.Employee;
import entity.Odetail;
import entity.Product;

import java.util.List;

/**
 *
 * @author Bitten
 */
public interface OrderInterface {
    public int createOrder(Order order, String name, String email, String address, int zip, int phone);
    public List<Order> getOrders();
    public List<Order> getOrdersUnassigned();
    public List<Order> getUnfinishedOrders();
    public List<Order> getOldOrders();
    public List<Order> getOwnOrders(int emplId);
    public Order getOrder(int id);
    public void assignOrder(Employee user, Order order);
    public void assignOrder(int orderID, int employeeID);
    public Order updateOrder(Order order) throws UpdateException;
    public void createOdetail(List<Odetail> odetails);
    public void editOdetails(List<Odetail> detail);
    public List<Odetail> getOdetails(int orderID);
    public Product getProduct(int prod_id);
    public Category getCategory(int prod_id);
    
    
}
