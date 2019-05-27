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

    /**
     * Inserts a new order using an object object and the given customer information. 
     * @param order the complete order object.
     * @param name
     * @param email
     * @param address
     * @param zip
     * @param phone
     * @return the generated id of the order
     */
    public int createOrder(Order order, String name, String email, String address, int zip, int phone);

    /**
     * Gets a list of all stored orders
     * @return List of order objects
     */
    public List<Order> getOrders();

    /**
     * Gets a list of all stored orders that hasn't been assigned to an employee.
     * @return List of order objects.
     */
    public List<Order> getOrdersUnassigned();

    /**
     * Gets all orders where the order status hasn't been set to 'delivered'.
     * @return List of order objects.
     */
    public List<Order> getUnfinishedOrders();

    /**
     *
     * @return
     */
    public List<Order> getOldOrders();

     /**
     * Gets a list of orders assigned to a specific employee by ID
     * @param id
     * @return List of order objects
     */
    public List<Order> getOwnOrders(int emplId);

    /**
     * Gets a specific order object
     * @param id the order id
     * @return order object with given id
     */
    public Order getOrder(int id);

    /**
     * Assigns an order to a specific employee.
     * @param user the employee to assign the order to
     * @param order the order to be assigned
     */
    public void assignOrder(Employee user, Order order);

    /**
     * Assigns an order to a specific employee
     * @param orderID the id of the order to be assigned
     * @param employeeID the id of the employee to assign the order to
     */
    public void assignOrder(int orderID, int employeeID);

    /**
     * Updates the stored order information with the new order information
     * @param order order object with new information
     * @return the updated order object
     * @throws UpdateException
     */
    public boolean updateOrder(Order order, List<Odetail> carport) throws UpdateException;

    /**
     * Inserts a list of build objects/odetails into a DB
     * @param odetails List of Odetail objects
     */
    public void createOdetail(List<Odetail> odetails);

    /**
     * Updates the db with new comments made in each odetail object 
     * @param detail List of Odetail objects
     */
    public void editOdetails(List<Odetail> detail);

    /**
     * Gets a list of products used in a build for a given order.
     * @param orderID
     * @return List of Odetail objects
     */
    public List<Odetail> getOdetails(int orderID);
    //public Product getProduct(int prod_id);
    //public Category getCategory(int prod_id);
    
    
}
